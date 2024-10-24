package org.grupo11.Api;

import org.grupo11.Logger;

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
                    .http((ctx, executionTimeMs) -> Logger.info(
                            "Received http message: from: {} method: {} path: {}", ctx.ip(), ctx.method(),
                            ctx.path()));
            cfg.events.serverStarted(() -> Logger.info("Server started, listening at http://localhost:{}", port));
            cfg.fileRenderer(new JavalinFreemarker());
            cfg.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/public";
                staticFiles.directory = "/public";
                staticFiles.location = Location.CLASSPATH;
            });

        };
        api = Javalin.create(config);
        setupMiddlewares();
        Router.setup(api);
        this.port = port;
    }

    void setupMiddlewares() {

    }

    public void start() {
        // we'll be running the server behind a proxy in prod
        api.start("127.0.0.1", port);
    }
}
