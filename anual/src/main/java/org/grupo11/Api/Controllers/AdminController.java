package org.grupo11.Api.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Enums.UserTypes;
import org.grupo11.Services.Credentials;
import org.grupo11.Services.Meal;
import org.grupo11.Services.Contact.Contact;
import org.grupo11.Services.Contact.EmailContact;
import org.grupo11.Services.Contributions.MealDistribution;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributions.MoneyDonation;
import org.grupo11.Services.Contributions.PersonRegistration;
import org.grupo11.Services.Contributor.Individual;
import org.grupo11.Services.Reporter.Report;
import org.grupo11.Services.Reporter.Reporter;
import org.grupo11.Services.Rewards.RewardSystem;
import org.grupo11.Utils.CSVImput;
import org.grupo11.Utils.Crypto;
import org.grupo11.Utils.DataImporter;
import org.grupo11.Utils.DateUtils;
import org.grupo11.Utils.FieldValidator;
import org.hibernate.Session;
import org.hibernate.Transaction;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

public class AdminController {

    public static void getReportData(Context ctx) {

        String reportIdParam = ctx.queryParam("id");

        if (reportIdParam == null) {
            ctx.status(400).result("Missing 'id' query parameter");
            return;
        }

        try {
            int reportId = Integer.parseInt(reportIdParam);

            try (Session session = DB.getSessionFactory().openSession()) {

                Report report = session.get(Report.class, reportId);

                if (report == null) {
                    ctx.status(404).result("Report not found");
                    return;
                }

                ctx.json(report);

            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).result("Internal server error: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid 'id' query parameter");
        }
    }

    public static void handleCreateReport(Context ctx) {
        Reporter reporter = Reporter.getInstance();
        reporter.regenerateReports();
        ctx.redirect("/dash/home");
        ctx.status(200).result("Reports updated successfully");
    }

    public static void handleUpdateReportFrequency(Context ctx) {
        String frequencyParam = ctx.formParam("frequency");
        String unitParam = ctx.formParam("unit");

        if (frequencyParam == null) {
            ctx.status(400).result("Missing 'frequency' form parameter");
            return;
        }
        // Que sea un numero
        if (unitParam == null) {
            ctx.status(400).result("Missing 'unit' form parameter");
            return;
        }

        int frequency = Integer.parseInt(frequencyParam);

        if (frequency < 1) {
            ctx.status(400).result("Invalid 'frequency' form parameter");
            return;
        }

        if (!unitParam.equals("MINUTES") && 
            !unitParam.equals("HOURS") && 
            !unitParam.equals("DAYS") && 
            !unitParam.equals("WEEKS"))
        {
            ctx.status(400).result("Invalid 'unit' form parameter");
            return;
        }

        try {
            Reporter reporter = Reporter.getInstance();
            reporter.newReportInterval(frequency, unitParam);
            ctx.status(200).result("Report frequency updated successfully");
            ctx.redirect("/dash/home");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid 'frequency' form parameter");
            ctx.redirect("/dash/home");
        }
    }

    public static void handleImportData(Context ctx) {
        UploadedFile file = ctx.uploadedFile("CSVfile");
        if (file == null) {
            ctx.redirect("/dash/home?error=No file was uploaded");
            return;
        }
        Logger.info("Uploaded file: " + file.filename());
        Logger.info("Uploaded file type: " + file.contentType());
        Logger.info("Uploaded file size: " + file.size());
        Logger.info("Uploaded file extension: " + file.extension());
        Logger.info("Uploaded file content: " + file.content());

        DataImporter dataImporter = new DataImporter();
        try {
            List<String> fields = dataImporter.readCSV(file);
            List<CSVImput> csvImputs = new ArrayList<>();
            for (String field : fields) {
                Logger.info("Processing field: " + field);
                CSVImput csvImput = CSVImput.processField(field);
                if (csvImput == null) {
                    ctx.redirect("/dash/home?error=Error importing data");
                    return;
                }
                csvImputs.add(csvImput);
            }
            
            try (Session session = DB.getSessionFactory().openSession()) {

                Logger.info("Arranca");

                for (CSVImput csvImput : csvImputs) {

                    // Transaction
                    Transaction transaction = session.beginTransaction();

                    Logger.info("Processing CSV input: " + csvImput.toString());

                    String hql = "SELECT i FROM Individual i WHERE i.credentials.mail = :mail";
                    List<Individual> individuals = session.createQuery(hql, Individual.class)
                        .setParameter("mail", csvImput.getMail())
                        .getResultList();
                    
                    Individual individual = individuals.size() > 0 ? individuals.get(0) : null;

                    if (individuals.size() > 1) {
                        throw new Exception("Multiple individuals with the same email");
                    }

                    if (individual == null) {
                        Long birth = csvImput.getContributionDate() - 20 * 365 * 24 * 60 * 60 * 1000;
                        individual = new Individual(csvImput.getName() + " " + csvImput.getSurname(), "", "", birth, csvImput.getDocument(), csvImput.getDocumentType());
                        Contact contact = new EmailContact(csvImput.getMail());
                        individual.addContact(contact);
                        Credentials credentials = new Credentials(csvImput.getMail(), Crypto.sha256Hash(Integer.toString(individual.getDocument()).getBytes()), UserTypes.Individual, individual.getId());
                        individual.setCredentials(credentials);
                        DB.create(contact);
                        DB.create(credentials);
                        DB.create(individual);
                        contact.SendNotification("Welcome to Fridge Bridge", "You have been registered as a contributor to the community. " +
                            "You can log in with your email and document number as password. Please change your password before continuing.");
                    }

                    Logger.info("Llega al switch");

                    switch (csvImput.getContributionType()) {

                        case DINERO:
                            MoneyDonation moneyDonation = new MoneyDonation(csvImput.getQuantity(), csvImput.getContributionDate(), "To help the community");
                            moneyDonation.setContributor(individual);
                            individual.addContribution(moneyDonation);
                            RewardSystem.assignPoints(individual, moneyDonation);
                            DB.create(moneyDonation);
                            break;

                        case DONACION_VIANDAS:
                            for (int i = 0; i < csvImput.getQuantity(); i++) {
                                Meal meal = new Meal("Food", DateUtils.getAWeewAheadFrom(csvImput.getContributionDate()), csvImput.getContributionDate(), null, "", 400, 200);
                                MealDonation mealDonation = new MealDonation(meal, csvImput.getContributionDate());
                                mealDonation.setContributor(individual);
                                individual.addContribution(mealDonation);
                                RewardSystem.assignPoints(individual, mealDonation);
                                DB.create(meal);
                                DB.create(mealDonation);
                            }
                            break;

                        case REDISTRIBUCION_VIANDAS:
                            MealDistribution mealDistribution = new MealDistribution(null, null, csvImput.getQuantity(), 
                                "To help the community", csvImput.getContributionDate());
                            mealDistribution.setContributor(individual);
                            individual.addContribution(mealDistribution);
                            RewardSystem.assignPoints(individual, mealDistribution);
                            DB.create(mealDistribution);
                            break;

                        case ENTREGA_TARJETAS:
                            for (int i = 0; i < csvImput.getQuantity(); i++) {
                                PersonRegistration personRegistration = new PersonRegistration(null, csvImput.getContributionDate(), individual);
                                personRegistration.setContributor(individual);
                                individual.addContribution(personRegistration);
                                RewardSystem.assignPoints(individual, personRegistration);
                                DB.create(personRegistration);
                            }
                            break;
                    }
                    DB.update(individual);

                    transaction.commit();
                }

            } catch (Exception e) {
                e.printStackTrace();
                ctx.redirect("/dash/home?error=Error importing data into database");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            ctx.redirect("/dash/home?error=Error importing data");
            return;
        }
    
        ctx.status(200).result("Data imported successfully");
        ctx.redirect("/dash/home");
    }

    public static void handleAdminSignup(Context ctx) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        if (!FieldValidator.isEmail(email)) {
            ctx.redirect("/dash/home?error=Invalid email");
            return;
        }

        if (!FieldValidator.acceptablePassword(password)) {
            ctx.redirect("/dash/home?error=Invalid password format. It must include uppercase, lowercase, digit and special character");
            return;
        }

        String hashedPassword = Crypto.sha256Hash(password.getBytes());
        Contact contact = new EmailContact(email);
        try (Session session = DB.getSessionFactory().openSession()) {
            session.beginTransaction();
            Credentials credentials = new Credentials(email, hashedPassword, UserTypes.Admin, Crypto.genId());
            DB.create(credentials);
            DB.create(contact);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Internal server error: " + e.getMessage());
        }
        contact.SendNotification("You have been registered as an admin", "You can log in with your email and password");
        ctx.status(200).result("Admin created successfully");
        ctx.redirect("/dash/home");
    }
}