package org.grupo11.Api;

import io.javalin.Javalin;

public class Router {
    public static void setup(Javalin api) {
        clientRoutes(api);
    }

    static void clientRoutes(Javalin api) {
        api.get("/", ctx -> {
            ctx.render("templates/landing.html");
        });
        api.get("/register/{filename}", Controllers::renderRegisterPages);
        api.get("/dash/{filename}", Controllers::renderDashboardPages);
    }
}
