package org.grupo11.Api.Controllers;

import java.util.ArrayList;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.Middlewares;
import org.grupo11.Services.Contributions.FridgeAdmin;
import org.grupo11.Services.Contributions.MoneyDonation;
import org.grupo11.Services.Contributions.RewardContribution;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Contributor.LegalEntity.LegalEntity;
import org.grupo11.Services.Contributor.LegalEntity.LegalEntityCategory;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.Sensor.MovementSensorManager;
import org.grupo11.Services.Fridge.Sensor.TemperatureSensorManager;
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

    public static void handlFridgeAdministrationContribution(Context ctx) {// TODO: only allow this contribution to
                                                                           // legalentities
        // name address capacity isactive
        System.err.println(ctx.body());

        Contributor contributor = Middlewares.isAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }

        String name = ctx.formParam("name");
        String address = ctx.formParam("address");
        String capacity = ctx.formParam("capacity");
        String isactive = ctx.formParam("isActive");
        try {
            if (!FieldValidator.isString(name)) {
                throw new IllegalArgumentException("invalid name");
            }
            if (!FieldValidator.isString(address)) {
                throw new IllegalArgumentException("invalid address");
            }
            if (!FieldValidator.isInt(capacity)) {
                throw new IllegalArgumentException("invalid capacity");
            }
            if (!FieldValidator.isBool(isactive)) {
                throw new IllegalArgumentException("invalid isactive");
            }

            Fridge fridge = new Fridge(0, 0, address, name, Integer.parseInt(capacity), 0, new ArrayList<>(), null,
                    null);
            LegalEntity le = new LegalEntity(); // this should be retrieved from the db using the contributor that islogged                           
            TemperatureSensorManager tManager = new TemperatureSensorManager(fridge,-1,60);
            MovementSensorManager mManager = new MovementSensorManager(fridge);
            fridge.setIsActive(Boolean.parseBoolean(isactive));
            fridge.setTempManager(tManager);
            fridge.setMovManager(mManager);
            tManager.setFridge(fridge);
            mManager.setFridge(fridge);
            FridgeAdmin fridgeAdmin = new FridgeAdmin(le, fridge, DateUtils.now());
            fridgeAdmin.setContributor(contributor);// TODO: only allow this contribution to legalentities
            DB.create(le);
            DB.create(fridge);
            ctx.redirect("/dash/home");
        } catch (Exception e) {
            Logger.error("Exception ", e);
            // ditto
            ctx.json("TODO: make front error message ");
            return;
        }
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
            ctx.json("TODO: make front error message ");
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
            throw new IllegalArgumentException("invalid name");
        }
        if (!FieldValidator.isString(description)) {
            throw new IllegalArgumentException("invalid description");
        }
        if (!FieldValidator.isInt(stock)) {
            throw new IllegalArgumentException("invalid stock");
        }
        if (!FieldValidator.isInt(points)) {
            throw new IllegalArgumentException("invalid points");

        }
        if (!FieldValidator.isValidEnumValue(RewardCategory.class, category)) {
            throw new IllegalArgumentException("invalid category");
        }
        try {
            Reward reward = new Reward(name, Float.parseFloat(points), "", RewardCategory.valueOf(category));
            RewardContribution rewardContribution = new RewardContribution(reward, DateUtils.now());
            rewardContribution.setContributor(contributor);
            DB.create(reward);
            DB.create(rewardContribution);
            ctx.redirect("/dash/home");
        } catch (Exception e) {
            Logger.error("Exception ", e);
            // ditto
            ctx.json("TODO: make front error message ");

            return;
        }
    }
}
