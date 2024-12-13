package org.grupo11.Api.Controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.ApiResponse;
import org.grupo11.Api.HttpUtils;
import org.grupo11.Api.Middlewares;
import org.grupo11.Enums.AuthProvider;
import org.grupo11.Enums.DocumentType;
import org.grupo11.Enums.UserTypes;
import org.grupo11.Services.Credentials;
import org.grupo11.Services.Contact.Contact;
import org.grupo11.Services.Contact.EmailContact;
import org.grupo11.Services.Contributor.Individual;
import org.grupo11.Services.Contributor.LegalEntity.LegalEntity;
import org.grupo11.Services.Contributor.LegalEntity.LegalEntityCategory;
import org.grupo11.Services.Contributor.LegalEntity.LegalEntityType;
import org.grupo11.Services.Technician.Technician;
import org.grupo11.Services.Technician.TechnicianType;
import org.grupo11.Utils.Crypto;
import org.grupo11.Utils.DateUtils;
import org.grupo11.Utils.FieldValidator;
import org.grupo11.Utils.JWTService;
import org.grupo11.Utils.OAuth.OAuthValidateResponse;
import org.hibernate.Session;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

enum Type {
    Contributor, Technician
}

class AddGoogleAuthProviderReqBody {
    private String token;

    public AddGoogleAuthProviderReqBody() {
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

public class Auth {
    public static void handleUserLogOut(Context ctx) {
        ctx.res().addCookie(HttpUtils.createHttpOnlyCookie("access-token", "", 3600));
    }

    public static void handleUserLogin(Context ctx) {
        BiConsumer<String, HttpStatus> sendFormError = (msg, status) -> {
            ctx.status(status).redirect("/register/login?error=" + msg);
        };
        try {
            String provider = ctx.formParam("provider");
            Credentials credentials;

            // if null, we default to mails pw authentication
            if (provider == null || provider == "FridgeBridge") {
                String mail = ctx.formParam("mail");
                String pw = ctx.formParam("password");

                if (!FieldValidator.isEmail(mail)) {
                    sendFormError.accept("Invalid email", HttpStatus.BAD_REQUEST);
                    return;
                }
                if (!FieldValidator.isString(pw)) {
                    sendFormError.accept("Invalid password", HttpStatus.BAD_REQUEST);
                    return;
                }
                credentials = AuthProvider.FridgeBridge.authenticate(mail, pw);

            } else {
                String token = ctx.formParam("token");
                if (!FieldValidator.isString(token)) {
                    sendFormError.accept("Invalid token.", HttpStatus.BAD_REQUEST);
                    return;
                }
                if (!FieldValidator.isValidEnumValue(AuthProvider.class, provider)) {
                    sendFormError.accept("Invalid provider, possible values are: FridgeBridge, Google, Github.",
                            HttpStatus.BAD_REQUEST);
                    return;
                }
                credentials = Enum.valueOf(AuthProvider.class, provider).authenticate(token);
            }

            if (credentials == null) {
                sendFormError.accept("Invalid credentials.", HttpStatus.UNAUTHORIZED);
                return;
            }

            Map<String, String> payload = new HashMap<>();
            payload.put("mail", credentials.getMail());
            payload.put("owner_id", credentials.getOwnerId().toString());
            payload.put("type", credentials.getUserType().toString());
            String jwtToken = JWTService.generate(payload, 3600);
            ctx.res().addCookie(HttpUtils.createHttpOnlyCookie("access-token", jwtToken, 3600));
            ctx.redirect("/dash/home");
        } catch (Exception e) {
            sendFormError.accept("Internal error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static void handleIndividualSignup(Context ctx) {
        String mail = ctx.formParam("mail");
        String type = ctx.formParam("type");
        String pw = ctx.formParam("password");
        String name = ctx.formParam("name");
        String birthdate = ctx.formParam("birthdate");
        String document = ctx.formParam("document");
        String address = ctx.formParam("address");

        Consumer<String> sendFormError = (msg) -> {
            ctx.status(400)
                    .json(new ApiResponse(400, msg, null));
            ctx.redirect("/register/signupIND?error=" + msg);
        };

        if (!FieldValidator.isEmail(mail)) {
            sendFormError.accept("Invalid email");
            return;
        }

        if (!FieldValidator.isValidEnumValue(Type.class, type)) {
            sendFormError.accept("Invalid type, possible values: Contributor, Technician");
            return;
        }

        if (!FieldValidator.isString(name)) {
            sendFormError.accept("Invalid name");
            return;
        }

        if (!FieldValidator.isDate(birthdate)) {
            sendFormError.accept("Invalid birthdate");
            return;
        }

        if (!FieldValidator.acceptablePassword(pw)) {
            sendFormError.accept(
                    "Invalid password format.<br> It must include uppercase, lowercase, digit and special character");
            return;
        }

        if (!FieldValidator.isInt(document)) {
            sendFormError.accept("Invalid document");
            return;
        }

        if (!FieldValidator.isString(address)) {
            sendFormError.accept("Invalid address");
            return;
        }

        try {
            Long birth = DateUtils.parseDateYMDString(birthdate);
            if (birth == null || !DateUtils.isOver18(birth)) {
                sendFormError.accept("Invalid birthdate, you must be at least 18 years old");
                return;
            }
        } catch (Exception e) {
            Logger.error("Unexpected error while creating user", e);
            sendFormError.accept("Unexpected error, try again...");
        }

        try {
            // check there isn't a registered account for that mail
            try (Session session = DB.getSessionFactory().openSession()) {
                String hql = "SELECT c " +
                        "FROM Credentials c " +
                        "WHERE c.mail = :mail";
                org.hibernate.query.Query<Credentials> query = session.createQuery(hql, Credentials.class);
                query.setParameter("mail", mail);

                if (query.uniqueResult() != null) {
                    sendFormError.accept("Mail already registered");
                    return;
                }
            } catch (Exception e) {
                Logger.error("Unexpected error while authenticating user", e);
                sendFormError.accept("Unexpected error, try again...");
            }

            String hashedPassword = Crypto.sha256Hash(pw.getBytes());
            Contact contact = new EmailContact(mail);
            if (Type.valueOf(type) == Type.Contributor) {

                Individual individual = new Individual(name, "", address, DateUtils.parseDateYMDString(birthdate),
                        Integer.parseInt(document),
                        DocumentType.DNI);
                Credentials credentials = new Credentials(mail, hashedPassword, UserTypes.Individual,
                        individual.getId(), AuthProvider.FridgeBridge);
                individual.addContact(contact);
                individual.addCredentials(credentials);
                DB.create(contact);
                DB.create(credentials);
                DB.create(individual);
            } else {
                Technician technician = new Technician(name, "", TechnicianType.ELECTRICIAN, Integer.parseInt(document),
                        "", address, contact);

                Credentials credentials = new Credentials(mail, hashedPassword, UserTypes.Technician,
                        technician.getId(), AuthProvider.FridgeBridge);
                technician.addCredentials(credentials);
                DB.create(contact);
                DB.create(credentials);
                DB.create(technician);
            }

            contact.SendNotification("Registered as new user", "You have registered in fridge bridge services!");
            ctx.redirect("/register/login");
        } catch (Exception e) {
            Logger.error("Unexpected error while creating user", e);
            sendFormError.accept("Unexpected error, try again...");
        }
    }

    public static void handleLegalEntitySignup(Context ctx) {
        String mail = ctx.formParam("mail");
        String name = ctx.formParam("name");
        String type = ctx.formParam("org-type");
        String category = ctx.formParam("org-category");
        String pw = ctx.formParam("password");

        Consumer<String> sendFormError = (msg) -> {
            ctx.status(400)
                    .json(new ApiResponse(400, msg, null));
            ctx.redirect("/register/signupLE?error=" + msg);
        };

        if (!FieldValidator.isEmail(mail)) {
            sendFormError.accept("Invalid email");
            return;
        }

        if (!FieldValidator.isValidEnumValue(LegalEntityType.class, type)) {
            sendFormError.accept("Invalid organization type");
            return;
        }

        if (!FieldValidator.isValidEnumValue(LegalEntityCategory.class, category)) {
            sendFormError.accept("Invalid organization category");
            return;
        }

        System.out.println("NAME:" + name);
        if (!FieldValidator.isString(name)) {
            sendFormError.accept("Invalid name");
            return;
        }

        if (!FieldValidator.acceptablePassword(pw)) {
            sendFormError.accept(
                    "Invalid password format.<br> It must include uppercase, lowercase, digit and special character");
            return;
        }

        try {
            // check there isn't a registered account for that mail
            try (Session session = DB.getSessionFactory().openSession()) {
                String hql = "SELECT c " +
                        "FROM Credentials c " +
                        "WHERE c.mail = :mail";
                org.hibernate.query.Query<Credentials> query = session.createQuery(hql, Credentials.class);
                query.setParameter("mail", mail);

                if (query.uniqueResult() != null) {
                    sendFormError.accept("Mail already registered");
                    return;
                }
            } catch (Exception e) {
                Logger.error("Unexpected error while authenticating user", e);
                sendFormError.accept("Unexpected error, try again...");
            }

            String hashedPassword = Crypto.sha256Hash(pw.getBytes());
            LegalEntity legalEntity = new LegalEntity(name, "", Enum.valueOf(LegalEntityType.class, type),
                    Enum.valueOf(LegalEntityCategory.class, category));
            Contact contact = new EmailContact(mail);
            Credentials credentials = new Credentials(mail, hashedPassword, UserTypes.LegalEntity, legalEntity.getId(),
                    AuthProvider.FridgeBridge);
            legalEntity.addContact(contact);
            legalEntity.addCredentials(credentials);

            DB.create(contact);
            DB.create(credentials);
            DB.create(legalEntity);

            contact.SendNotification("Registered as new user", "You have registered in fridge bridge services!");
            ctx.redirect("/register/login");
        } catch (Exception e) {
            Logger.error("Unexpected error while creating user", e);
            sendFormError.accept("Unexpected error, try again...");
        }
    }

    public static void handleAddGoogleProvider(Context ctx) {
        Credentials credentials = Middlewares.authenticated(ctx);
        if (credentials == null) {
            ctx.status(401).json(new ApiResponse(401));
            return;
        }

        AddGoogleAuthProviderReqBody body = ctx.bodyAsClass(AddGoogleAuthProviderReqBody.class);
        if (body == null) {
            ctx.status(400).json(new ApiResponse(400, "Invalid bod."));
            return;
        }
        String tokenId = body.getToken();
        if (!FieldValidator.isString(tokenId)) {
            ctx.status(400).json(new ApiResponse(400, "Invalid token_id.", null));
            return;
        }

        OAuthValidateResponse validationRes = AuthProvider.Google.validateToken(tokenId);
        if (validationRes == null) {
            ctx.status(401).json(new ApiResponse(401, "Token validation invalid."));
            return;
        }

        try (Session session = DB.getSessionFactory().openSession()) {
            Auth.addOAuthCredential(credentials.getMail(), credentials.getOwnerId(), credentials.getUserType(),
                    AuthProvider.Google);
            ctx.redirect("/dash/home");
            ctx.status(200).json(new ApiResponse(200));
        } catch (Exception e) {
            ctx.status(500).json(new ApiResponse(500));
        }
    }

    public static void handleAddGithubProvider(Context ctx) {
        Credentials credentials = Middlewares.authenticated(ctx);
        if (credentials == null) {
            ctx.redirect("/register/login");
            return;
        }

        String code = ctx.queryParam("code");
        if (!FieldValidator.isString(code)) {
            ctx.redirect("/dash/home?error=Invalid code");
            return;
        }

        OAuthValidateResponse validationRes = AuthProvider.Github.validateToken(code);
        if (validationRes == null) {
            ctx.redirect("/dash/home?error=Invalid code");
            return;
        }

        try (Session session = DB.getSessionFactory().openSession()) {
            Auth.addOAuthCredential(credentials.getMail(), credentials.getOwnerId(), credentials.getUserType(),
                    AuthProvider.Github);
            ctx.redirect("/dash/home");
        } catch (Exception e) {
            ctx.redirect("/dash/home?error=Could not connect github account");
        }
    }

    static void addOAuthCredential(String email, Long ownerId, UserTypes type, AuthProvider provider) throws Exception {
        Session session = DB.getSessionFactory().openSession();
        String hql = "SELECT c " +
                "FROM Credentials c " +
                "WHERE c.ownerId = :ownerId AND c.provider = :provider";
        org.hibernate.query.Query<Credentials> query = session.createQuery(hql, Credentials.class);
        query.setParameter("ownerId", ownerId);
        query.setParameter("provider", provider);

        try {
            // update if already exists
            Credentials c = query.getSingleResult();
            c.setMail(email);
            DB.update(c);
        } catch (Exception e) {
            // this means it did not exist, create it
            DB.create(new Credentials(email, null, type, ownerId, provider));
        }
    }
}
