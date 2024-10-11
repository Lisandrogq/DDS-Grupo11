package org.grupo11.Api;

import org.grupo11.Utils.DateUtils;

import java.util.HashMap;
import java.util.Map;
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
        setupRoutes();
        this.port = port;
    }

    void setupRoutes() {
        api.get("/", ctx -> ctx.result("Server running!"));

        api.get("/home", ctx -> {
            Map<String, Object> model = new HashMap<>();
            model.put("firstName", "John");
            model.put("lastName", "Doe");
            ctx.render("templates/home.html", model);
        });
    }

    public void start() {
        // we'll be running the server behind a proxy in prod
        api.start("127.0.0.1", port);
    }
}
