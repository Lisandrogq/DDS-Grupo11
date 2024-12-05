package org.grupo11.Api.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.Middlewares;
import org.grupo11.Services.Credentials;
import org.grupo11.Services.Meal;
import org.grupo11.Services.Contributions.FridgeAdmin;
import org.grupo11.Services.Contributions.MealDistribution;
import org.grupo11.Services.Contributions.MealDonation;
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
import org.hibernate.Session;

import io.javalin.http.Context;

public class ContributionsController {
    public static void handleMealContribution(Context ctx) {
        Contributor contributor = Middlewares.isAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }

        String type = ctx.formParam("type");
        String expirationDate = ctx.formParam("expirationDate");
        String fridge_address = ctx.formParam("fridge_address");
        String calories = ctx.formParam("calories");
        String weight = ctx.formParam("weight");

        System.err.println(ctx.body());
        try (Session session = DB.getSessionFactory().openSession()) {
            if (!FieldValidator.isString(type)) {
                throw new IllegalArgumentException("invalid type");
            }
            if (!FieldValidator.isString(fridge_address)) {
                throw new IllegalArgumentException("invalid fridge_address");
            }
            if (!FieldValidator.isDate(expirationDate)) {
                throw new IllegalArgumentException("invalid expirationDate");
            }
            if (!FieldValidator.isInt(calories)) {
                throw new IllegalArgumentException("invalid calories");
            }
            if (!FieldValidator.isInt(weight)) {
                throw new IllegalArgumentException("invalid weight");
            }
            String hql = "SELECT f " +
                    "FROM Fridge f " +
                    "WHERE f.address = :fridge_address";
            org.hibernate.query.Query<Fridge> query = session.createQuery(hql, Fridge.class);
            query.setParameter("fridge_address", fridge_address);
            Fridge fridge = query.uniqueResult();
            if (fridge == null) {
                throw new IllegalArgumentException("address inexistente");
            }
            Meal meal = new Meal(type, DateUtils.parseDateString(expirationDate), DateUtils.now(), fridge, "",
                    Integer.parseInt(calories), Integer.parseInt(weight));
            MealDonation mealDonation = new MealDonation(meal, DateUtils.now());
            mealDonation.setContributor(contributor);
            fridge.addMeal(meal);
            DB.create(meal);
            DB.create(mealDonation);
            DB.update(fridge);
            ctx.redirect("/dash/home");

        } catch (Exception e) {
            Logger.error("Exception ", e);
            // ditto          
              ctx.json("TODO: make front error message - "+ e.getMessage());
            return;
        }
    }

    public static void handleMealDistributionContribution(Context ctx) {
        System.out.println(ctx.body());
        // meal_0=id&reason=reason&origin_address=origin&destiny_address=destiny

        Contributor contributor = Middlewares.isAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }

        String reason = ctx.formParam("reason");
        String origin_address = ctx.formParam("origin_address");
        String destiny_address = ctx.formParam("destiny_address");

        System.err.println(ctx.body());
        try (Session session = DB.getSessionFactory().openSession()) {

            if (!FieldValidator.isString(reason)) {
                throw new IllegalArgumentException("invalid reason");
            }
            if (!FieldValidator.isString(origin_address)) {
                throw new IllegalArgumentException("invalid origin_address");
            }
            if (!FieldValidator.isString(destiny_address)) {
                throw new IllegalArgumentException("invalid destiny_address");
            }

            String origin_hql = "SELECT f " +
                    "FROM Fridge f " +
                    "WHERE f.address = :origin_address"; 
            org.hibernate.query.Query<Fridge> origin_query = session.createQuery(origin_hql, Fridge.class);
            origin_query.setParameter("origin_address", origin_address);
            String destiny_hql = "SELECT f " +
                    "FROM Fridge f " +
                    "WHERE f.address = :destiny_address";
            org.hibernate.query.Query<Fridge> destiny_query = session.createQuery(destiny_hql, Fridge.class);
            destiny_query.setParameter("destiny_address", destiny_address);

            Fridge origin_fridge = origin_query.getSingleResult();
            Fridge destiny_fridge = destiny_query.getSingleResult();
            if (origin_fridge == null || destiny_fridge == null) {
                throw new IllegalArgumentException("alguna address inexistente");
            }

            for (int i = 0; i < 10; i++) { // primero se valida que todas las comidas estén . se podría hacer con transacciones pero notiempo
                String meal_type = ctx.formParam("meal_" + i);
                if (meal_type != null) {
                    Meal meal = origin_fridge.getMealByType(meal_type);

                    if (meal == null) {
                        throw new IllegalArgumentException(meal_type+" no existe en la heladera de origen");
                    }
                }
            }
            int i;
            for (i = 0; i < 10; i++) { // luego se realiza el movimiento.
                String meal_type = ctx.formParam("meal_" + i);
                if (meal_type != null) {
                    Meal meal = origin_fridge.getMealByType(meal_type);
                    System.out.println("adad: "+meal_type+" - "+meal.getType());
                    
                        origin_fridge.removeMeal(meal);
                        destiny_fridge.addMeal(meal);
                        meal.setFridge(destiny_fridge);
                        DB.update(meal);
                    
                }
            }
            DB.update(origin_fridge);
            DB.update(destiny_fridge);

            MealDistribution mealDistribution = new MealDistribution(origin_fridge, destiny_fridge, i + 1, reason,
                    DateUtils.now());
            mealDistribution.setContributor(contributor);
            DB.create(mealDistribution);

            ctx.redirect("/dash/home");

        } catch (Exception e) {
            Logger.error("Exception ", e);
            // ditto
            ctx.json("TODO: make front error message - "+ e.getMessage());
            return;
        }

    }

    public static void handlFridgeAdministrationContribution(Context ctx) {// TODO: only allow this contribution to
                                                                           // legal entities
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
            LegalEntity le = new LegalEntity(); // this should be retrieved from the db using the contributor that
                                                // islogged
            TemperatureSensorManager tManager = new TemperatureSensorManager(fridge, -1, 60);
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
            DB.create(fridgeAdmin);
            ctx.redirect("/dash/home");
        } catch (Exception e) {
            Logger.error("Exception ", e);
            // ditto
            ctx.json("TODO: make front error message - "+ e.getMessage());
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
            ctx.json("TODO: make front error message - "+ e.getMessage());
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
            reward.setDescription(description);
            reward.setQuantity(Integer.parseInt(stock));
            RewardContribution rewardContribution = new RewardContribution(reward, DateUtils.now());
            rewardContribution.setContributor(contributor);
            DB.create(reward);
            DB.create(rewardContribution);
            ctx.redirect("/dash/home");
        } catch (Exception e) {
            Logger.error("Exception ", e);
            // ditto
            ctx.json("TODO: make front error message - "+ e.getMessage());
            return;
        }
    }
}
