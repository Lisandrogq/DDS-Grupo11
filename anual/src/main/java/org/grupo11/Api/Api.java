package org.grupo11.Api;

import org.grupo11.Utils.DateUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinFreemarker;

public class Api {
    Javalin api;
    int port;

    public Api(int port) {
        Consumer<JavalinConfig> config = cfg -> {
            cfg.http.generateEtags = true;
            cfg.requestLogger
                    .http((ctx, executionTimeMs) -> System.out
                            .println("Received http message \nfrom: " + ctx.ip() + "\nbody: " + ctx.body() + "\nat: " +
                                    DateUtils.now()));
            cfg.events.serverStarted(() -> System.out.println("Server started"));
            cfg.fileRenderer(new JavalinFreemarker());
            cfg.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/public";
                staticFiles.directory = "/public";
                staticFiles.location = Location.CLASSPATH;
            });

        };
        api = Javalin.create(config);
        setupMiddlewares();
        setupRoutes();
        this.port = port;
    }

    void setupMiddlewares() {

    }

    void setupRoutes() {
        api.get("/", ctx -> {
            ctx.render("templates/landing.html");
        });

        api.get("/register/{filename}", ctx -> {
            try {
                String filename = ctx.pathParam("filename");
                Path filePath = Paths.get("src/main/resources/templates/register/", filename + ".html");

                if (Files.exists(filePath)) {
                    ctx.render("templates/static/register/" + filename + ".html");
                } else {
                    ctx.status(404);
                }
            } catch (Exception e) {
                ctx.status(500);
            }
        });

        api.get("/dash/{filename}", ctx -> {
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
        });
    }

    public void start() {
        // we'll be running the server behind a proxy in prod
        api.start("127.0.0.1", port);
    }
}
