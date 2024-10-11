package org.grupo11.Api;

import org.grupo11.Api.Controllers.RenderController;
import org.grupo11.Api.Controllers.UserController;

import io.javalin.Javalin;

public class Router {
    public static void setup(Javalin api) {
        clientRoutes(api);
        userRoutes(api);
    }

    static void clientRoutes(Javalin api) {
        api.get("/", ctx -> {
            ctx.render("templates/landing.html");
        });
        api.get("/register/{filename}", RenderController::renderRegisterPages);
        api.get("/dash/{filename}", RenderController::renderDashboardPages);
    }

    static void userRoutes(Javalin api) {
        api.post("/user/login", UserController::handleUserLogin);
        api.post("/user/", UserController::handleUserSignup);
    }

}
