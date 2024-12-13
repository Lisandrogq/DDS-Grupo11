package org.grupo11.Api;

import org.grupo11.Api.Controllers.ContributionsController;
import org.grupo11.Api.Controllers.FridgeController;
import org.grupo11.Api.Controllers.PublicAPI;
import org.grupo11.Api.Controllers.RenderController;
import org.grupo11.Api.Controllers.RewardsController;
import org.grupo11.Api.Controllers.AdminController;
import org.grupo11.Api.Controllers.Auth;

import io.javalin.Javalin;

public class Router {
    public static void setup(Javalin api) {
        clientRoutes(api);
        userRoutes(api);
        contributionRoutes(api);
        fridgeRoutes(api);
        publicApi(api);
        rewardRoutes(api);
        adminRoutes(api);
    }

    static void clientRoutes(Javalin api) {
        api.get("/", ctx -> {
            ctx.render("templates/landing.html");
        });
        api.get("/register/{filename}", RenderController::renderRegisterPages);
        api.get("/dash/home", RenderController::renderDashboardPage);
    }

    static void userRoutes(Javalin api) {
        api.get("/user/logout", Auth::handleUserLogOut);
        api.post("/user/login", Auth::handleUserLogin);
        api.post("/user/individual", Auth::handleIndividualSignup);
        api.post("/user/legal-entity", Auth::handleLegalEntitySignup);
        api.post("/user/provider/google", Auth::handleAddGoogleProvider);
        api.get("/user/provider/github", Auth::handleAddGithubProvider);
    }

    static void contributionRoutes(Javalin api) {
        api.post("/contribution/meal", ContributionsController::handleMealContribution);
        api.post("/contribution/meal/distribution", ContributionsController::handleMealDistributionContribution);
        api.post("/contribution/money", ContributionsController::handleMoneyContribution);
        api.post("/contribution/fridge_admin", ContributionsController::handlFridgeAdministrationContribution);
        api.post("/contribution/registration", ContributionsController::handlePersonRegistrationContribution);
        api.post("/contribution/reward", ContributionsController::handleRewardContribution);
    }

    static void publicApi(Javalin api) {
        api.get("/api/contributors", PublicAPI::renderFilterContributors);
        api.get("/api/contributors/recognitions", PublicAPI::handleContributorRecognitions);
    }

    static void fridgeRoutes(Javalin api) {
        api.post("/fridge/add_visit", FridgeController::handleAddVisit);
        api.post("/fridge/failure", FridgeController::handleSubmitFailure);
        api.post("/fridge/subscribe", FridgeController::handleSubscription);
        api.post("/fridge/unsubscribe", FridgeController::handleUnsubscription);
        api.get("/fridge/info", FridgeController::getFridgeInfo);
    }

    static void rewardRoutes(Javalin api) {
        api.post("/rewards", RewardsController::handleUpdateRewards);
    }

    static void adminRoutes(Javalin api) {
        api.post("/admin/importData", AdminController::handleImportData);
        api.get("/admin/report", AdminController::getReportData);
        api.post("/admin/report/create", AdminController::handleCreateReport);
        api.post("/admin/report/updateFrequency", AdminController::handleUpdateReportFrequency);
        api.post("/admin/signAdmin", AdminController::handleAdminSignup);
    }
}
