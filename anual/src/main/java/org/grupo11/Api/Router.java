package org.grupo11.Api;

import org.grupo11.Api.Controllers.AlertsController;
import org.grupo11.Api.Controllers.ContributionsController;
import org.grupo11.Api.Controllers.PublicAPI;
import org.grupo11.Api.Controllers.RenderController;
import org.grupo11.Api.Controllers.Auth;

import io.javalin.Javalin;

public class Router {
    public static void setup(Javalin api) {
        clientRoutes(api);
        userRoutes(api);
        contributionRoutes(api);
        alertRoutes(api);
        publicApi(api);
    }

    static void clientRoutes(Javalin api) {
        api.get("/", ctx -> {
            ctx.render("templates/landing.html");
        });
        api.get("/register/{filename}", RenderController::renderRegisterPages);
        api.get("/dash/home", RenderController::renderDashboardPage);
    }

    static void userRoutes(Javalin api) {
        api.post("/user/login", Auth::handleUserLogin);
        api.post("/user/individual", Auth::handleIndividualSignup);
        api.post("/user/legal-entity", Auth::handleLegalEntitySignup);
    }

    static void contributionRoutes(Javalin api) {
        api.post("/contribution/meal", ContributionsController::handleMealContribution);
        api.post("/contribution/meal/distribution", ContributionsController::handleMealDistributionContribution);
        api.post("/contribution/money", ContributionsController::handleMoneyContribution);
        api.post("/contribution/registration", ContributionsController::handlePersonRegistrationContribution);
        api.post("/contribution/reward", ContributionsController::handleRewardContribution);
    }

    static void publicApi(Javalin api) {
        api.get("/api/contributors/recognitions", PublicAPI::handleContributorRecognitions);
    }

    static void alertRoutes(Javalin api) {
        api.post("/alerts/failure", AlertsController::handleFailureAlert);
    }
}
