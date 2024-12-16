package org.grupo11.Api;

import org.grupo11.Logger;
import org.grupo11.Metrics;

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
                staticFiles.directory = "src/main/resources/public";
                staticFiles.location = Location.EXTERNAL;
            });
        };
        api = Javalin.create(config);
        setupMiddlewares();
        Router.setup(api);
        this.port = port;
    }

    void setupMiddlewares() {
        api.before(ctx -> {
            if (ctx.path().startsWith("/public")) {
                return;
            }

            Metrics.getInstance().getOpenConnectionsGauge().inc();
        });
        api.after(ctx -> {
            if (ctx.path().startsWith("/public")) {
                return;
            }

            Metrics metrics = Metrics.getInstance();
            metrics.getOpenConnectionsGauge().dec();
            metrics.getRequestsServedCounter().inc();
            if (ctx.statusCode() >= 400) {
                metrics.getRequestsFailedCounter().inc();
            }
        });
        api.exception(Exception.class, (e, ctx) -> {
            Logger.error("An exception occurred while processing the request: ", e);
            ctx.status(500).result("Internal Server Error");
        });
    }

    public void start() {
        api.start("127.0.0.1", port);
    }
}
