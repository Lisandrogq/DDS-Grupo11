package org.grupo11.Api.Controllers;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.Middlewares;
import org.grupo11.Services.Contributions.MoneyDonation;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Utils.DateUtils;
import org.grupo11.Utils.FieldValidator;

import io.javalin.http.Context;

public class ContributionsController {
    public static void handleMealContribution(Context ctx) {
    }

    public static void handleMealDistributionContribution(Context ctx) {
    }

    public static void handleMoneyContribution(Context ctx) {
        Contributor contributor = Middlewares.isAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }
        String amount = ctx.formParam("amount");
        String message = ctx.formParam("message");

        if (!FieldValidator.isInt(amount)) {
            // see how to show err in client
            ctx.redirect("/dash/home");
            return;
        }
        if (!FieldValidator.isString(message)) {
            // ditto
            ctx.redirect("/dash/home");
            return;
        }
        try {
            MoneyDonation moneyDonation = new MoneyDonation(Integer.parseInt(amount), DateUtils.now(), message);
            moneyDonation.setContributor(contributor);
            DB.create(moneyDonation);
            ctx.redirect("/dash/home");
        } catch (Exception e) {
            Logger.error("Exception ", e);
            // ditto
            ctx.redirect("/dash/home");
            return;
        }
    }

    public static void handlePersonRegistrationContribution(Context ctx) {
    }

    public static void handleRewardContribution(Context ctx) {
    }
}
