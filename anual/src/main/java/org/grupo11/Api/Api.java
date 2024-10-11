package org.grupo11.Api;

import org.grupo11.Utils.DateUtils;
import java.util.function.Consumer;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;

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
            cfg.router.apiBuilder(() -> {

            });
        };
        api = Javalin.create(config);
        setupRoutes();
        this.port = port;
    }

    void setupRoutes() {
        api.get("/", ctx -> ctx.result("Server running!"));
    }

    public void start() {
        // we'll be running the server behind a proxy in prod
        api.start("127.0.0.1", port);
    }
}
