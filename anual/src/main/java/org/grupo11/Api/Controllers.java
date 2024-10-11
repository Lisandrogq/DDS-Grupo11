package org.grupo11.Api;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.javalin.http.Context;

public class Controllers {

    static void renderRegisterPages(Context ctx) {
        try {
            String filename = ctx.pathParam("filename");
            Path filePath = Paths.get("src/main/resources/templates/register/", filename + ".html");

            if (Files.exists(filePath)) {
                ctx.render("templates/register/" + filename + ".html");
            } else {
                ctx.status(404);
            }
        } catch (Exception e) {
            ctx.status(500);
        }
    }

    static void renderDashboardPages(Context ctx) {
        try {
            String filename = ctx.pathParam("filename");
            Path filePath = Paths.get("src/main/resources/templates/dash/", filename + ".html");

            if (Files.exists(filePath)) {
                ctx.render("templates/dash/" + filename + ".html");
            } else {
                ctx.status(404);
            }
        } catch (Exception e) {
            ctx.status(500);
        }
    }

}
