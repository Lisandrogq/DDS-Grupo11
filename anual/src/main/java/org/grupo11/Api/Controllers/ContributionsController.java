package org.grupo11.Api.Controllers;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.Middlewares;
import org.grupo11.Services.Contributions.MoneyDonation;
import org.grupo11.Services.Contributions.RewardContribution;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Rewards.Reward;
import org.grupo11.Services.Rewards.RewardCategory;
import org.grupo11.Utils.DateUtils;
import org.grupo11.Utils.FieldValidator;

import io.javalin.http.Context;

public class ContributionsController {
    public static void handleMealContribution(Context ctx) {
    }

    public static void handleMealDistributionContribution(Context ctx) {
    }

    public static void handlFridgeAdministrationContribution(Context ctx) {
        //name address capacity isactive
        System.err.println(ctx.body());

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
        Contributor contributor = Middlewares.isAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }

        String name = ctx.formParam("name");
        String stock = ctx.formParam("stock");
        String description = ctx.formParam("description");
        String points = ctx.formParam("points");
        String category = ctx.formParam("category");
        if (!FieldValidator.isString(name)) {
            throw new Error("invalid name");
        }
        if (!FieldValidator.isString(description)) {
            throw new Error("invalid description");
        }
        if (!FieldValidator.isInt(stock)) {
            throw new Error("invalid stock");            
        }
        if (!FieldValidator.isInt(points)) {
            throw new Error("invalid points");

        }
        if (!FieldValidator.isValidEnumValue(RewardCategory.class,category)) {
            throw new Error("invalid category");
        }
        try {
            Reward reward = new Reward(name,Float.parseFloat(points),"",RewardCategory.valueOf(category));
            RewardContribution rewardContribution = new RewardContribution(reward,DateUtils.now());
            rewardContribution.setContributor(contributor);
            DB.create(reward);
            DB.create(rewardContribution);
            ctx.redirect("/dash/home");
        } catch (Exception e) {
            Logger.error("Exception ", e);
            // ditto
            ctx.redirect("/dash/home");
            return;
        } 
    }
}
