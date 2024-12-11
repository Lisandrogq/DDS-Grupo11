package org.grupo11.Api.Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.grupo11.DB;
import org.grupo11.DataImporter;
import org.grupo11.Logger;
import org.grupo11.Api.JsonData.FridgeInfo.FridgeFullInfo;
import org.grupo11.Enums.UserTypes;
import org.grupo11.Services.Credentials;
import org.grupo11.Services.Meal;
import org.grupo11.Services.Contact.Contact;
import org.grupo11.Services.Contact.EmailContact;
import org.grupo11.Services.Contributions.ContributionsManager;
import org.grupo11.Services.Contributor.ContributorsManager;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.Incident.Alert;
import org.grupo11.Services.Fridge.Incident.Failure;
import org.grupo11.Services.Fridge.Incident.Incident;
import org.grupo11.Services.Reporter.Report;
import org.grupo11.Services.Reporter.Reporter;
import org.grupo11.Utils.Crypto;
import org.grupo11.Utils.FieldValidator;
import org.hibernate.Hibernate;
import org.hibernate.Session;

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
        reporter.genReport();
        ctx.status(200).result("Report created successfully");
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

        ctx.redirect("/dash/home?error=Importing data is not available yet");
        return;

        /*
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

        try {
            ContributionsManager contributionsManager = ContributionsManager.getInstance();
            ContributorsManager contributorManager = ContributorsManager.getInstance();
            DataImporter dataImporter = new DataImporter(contributionsManager, contributorManager);

            // dataImporter.loadContributors(file.filename());



            
            ctx.status(200).result("Archivo CSV procesado exitosamente");
        } catch (IOException e) {
            ctx.status(500).result("Error al procesar el archivo");
        }
        */
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

        try (Session session = DB.getSessionFactory().openSession()) {
            session.beginTransaction();
            Credentials credentials = new Credentials(email, hashedPassword, UserTypes.Admin, Crypto.genId());
            DB.create(credentials);
            session.getTransaction().commit();
            ctx.status(200).result("Admin created successfully");
            ctx.redirect("/dash/home");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Internal server error: " + e.getMessage());
        }
    }
}