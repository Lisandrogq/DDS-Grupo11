package org.grupo11.Api.Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.grupo11.DataImporter;
import org.grupo11.Logger;
import org.grupo11.Services.Contributions.ContributionsManager;
import org.grupo11.Services.Contributor.ContributorsManager;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

public class AdminController {

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
}
