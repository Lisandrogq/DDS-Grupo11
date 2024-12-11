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
import org.grupo11.Enums.AuthProviders;
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

public class Auth {
    public static void handleUserLogOut(Context ctx) {
        ctx.res().addCookie(HttpUtils.createHttpOnlyCookie("access-token", "", 3600));
    }

    public static void handleUserLogin(Context ctx) {
        BiConsumer<String, HttpStatus> sendFormError = (msg, status) -> {
            ctx.status(status).redirect("/register/login?error=" + msg);
        };
        try {
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

            try (Session session = DB.getSessionFactory().openSession()) {
                String hashedPassword = Crypto.sha256Hash(pw.getBytes());
                String hql = "SELECT c " +
                        "FROM Credentials c " +
                        "WHERE c.password = :password AND c.mail = :mail";

                org.hibernate.query.Query<Credentials> query = session.createQuery(hql, Credentials.class);
                query.setParameter("mail", mail);
                query.setParameter("password", hashedPassword);

                Credentials credentials = query.getSingleResult();

                if (credentials == null) {
                    sendFormError.accept("Invalid credentials", HttpStatus.UNAUTHORIZED);
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
                Logger.error("Unexpected error while authenticating user", e);
                sendFormError.accept("Invalid credentials", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }
        } catch (Exception e) {
            sendFormError.accept("Invalid credentials", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    public static void handleIndividualSignup(Context ctx) {
        System.out.println(ctx.body());
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

                Individual individual = new Individual(name, "", address, DateUtils.parseDateYMDString(birthdate), Integer.parseInt(document),
                        DocumentType.DNI);
                Credentials credentials = new Credentials(mail, hashedPassword, UserTypes.Individual,
                        individual.getId());
                individual.addContact(contact);
                individual.setCredentials(credentials);
                DB.create(contact);
                DB.create(credentials);
                DB.create(individual);
            } else {
                Technician technician = new Technician(name, "", TechnicianType.ELECTRICIAN, Integer.parseInt(document),
                        "", address, contact);

                Credentials credentials = new Credentials(mail, hashedPassword, UserTypes.Technician,
                        technician.getId());
                technician.setCredentials(credentials);
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
            Credentials credentials = new Credentials(mail, hashedPassword, UserTypes.LegalEntity, legalEntity.getId());
            legalEntity.addContact(contact);
            legalEntity.setCredentials(credentials);

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

    public static void handleNewAuthProvider(Context ctx) {
        Credentials credentials = Middlewares.authenticated(ctx);
        if (credentials == null) {
            ctx.status(401).json(new ApiResponse(401));
            return;
        }

        String provider = ctx.formParam("provider");
        String tokenId = ctx.formParam("token_id");

        if (!FieldValidator.isValidEnumValue(AuthProviders.class, provider)) {
            ctx.status(400).json(new ApiResponse(400, "Invalid provider, possible values: google, github.", null));
            return;
        }
        if (!FieldValidator.isString(tokenId)) {
            ctx.status(400).json(new ApiResponse(400, "Invalid token_id.", null));
            return;
        }

        AuthProviders authProvider = Enum.valueOf(AuthProviders.class, provider);

        // Verify it isn't already created
        if (credentials.getProvidersByValue(AuthProviders.Google) != null) {
            ctx.status(400).json(new ApiResponse(400, "Provider already added.", null));
        }

        OAuthValidateResponse validationRes = authProvider.validateToken(tokenId);
        if (validationRes == null) {
            ctx.status(401).json(new ApiResponse(401, "Token validation invalid."));
            return;
        }
        if (credentials.getMail() != validationRes.getEmail()) {
            ctx.status(401).json(new ApiResponse(401, "Mail must be the same."));
            return;
        }

        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT c " +
                    "FROM Credentials c " +
                    "WHERE c.mail = :mail";
            org.hibernate.query.Query<Credentials> query = session.createQuery(hql, Credentials.class);
            query.setParameter("mail", validationRes.getEmail());

            if (query.uniqueResult() == null) {
                ctx.status(400).json(new ApiResponse(400, "Mail isn't registered."));
                return;
            }

            credentials.addProvider(authProvider);
            DB.update(credentials);
        } catch (Exception e) {
            Logger.error("Unexpected error while authenticating user", e);
            ctx.status(500).json(new ApiResponse(500));
        }
    }

    public static void handleProviderLogin(Context ctx) {
        String provider = ctx.formParam("provider");
        String tokenId = ctx.formParam("token_id");

        if (!FieldValidator.isValidEnumValue(AuthProviders.class, provider)) {
            ctx.status(400).json(new ApiResponse(400, "Invalid provider, possible values: google, github.", null));
            return;
        }
        if (!FieldValidator.isString(tokenId)) {
            ctx.status(400).json(new ApiResponse(400, "Invalid token_id.", null));
            return;
        }

        AuthProviders authProvider = Enum.valueOf(AuthProviders.class, provider);

        OAuthValidateResponse validationRes = authProvider.validateToken(tokenId);
        if (validationRes == null) {
            ctx.status(401).json(new ApiResponse(401, "Token validation invalid."));
            return;
        }

        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT c " +
                    "FROM Credentials c " +
                    "WHERE c.mail = :mail";
            org.hibernate.query.Query<Credentials> query = session.createQuery(hql, Credentials.class);
            query.setParameter("mail", validationRes.getEmail());

            if (query.uniqueResult() == null) {
                ctx.status(400).json(new ApiResponse(400, "Mail isn't registered."));
                return;
            }

            Credentials credentials = query.getSingleResult();
            if (credentials.getProvidersByValue(authProvider) == null) {
                credentials.addProvider(authProvider);
                DB.update(credentials);
            }

            Map<String, String> payload = new HashMap<>();
            payload.put("mail", credentials.getMail());
            payload.put("owner_id", credentials.getOwnerId().toString());
            payload.put("type", credentials.getUserType().toString());

            String jwtToken = JWTService.generate(payload, 3600);
            ctx.res().addCookie(HttpUtils.createHttpOnlyCookie("access-token", jwtToken, 3600));
            ctx.redirect("/dash/home");
        } catch (Exception e) {
            Logger.error("Unexpected error while authenticating user", e);
            ctx.status(500).redirect("/register/login?error=Unexpected error");
        }
    }
}
