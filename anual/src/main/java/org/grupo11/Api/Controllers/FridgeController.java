package org.grupo11.Api.Controllers;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.Middlewares;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Contributor.ContributorsManager;
import org.grupo11.Utils.DateUtils;
import java.util.List;
import org.grupo11.Api.ApiResponse;
import org.grupo11.Api.JsonData.ExchangeRewards.RedeemRequest;
import org.grupo11.Api.JsonData.FridgeInfo.FridgeFullInfo;
import org.grupo11.Services.Rewards.Reward;
import org.grupo11.Utils.FieldValidator;
import org.hibernate.Session;
import org.grupo11.Services.Fridge.Incident.Failure;
import org.grupo11.Services.Fridge.Incident.Urgency;
import org.grupo11.Services.Meal;
import org.grupo11.Services.Fridge.Fridge;

import io.javalin.http.Context;

public class FridgeController {
    public static void handleSubmitFailure(Context ctx) {
        System.out.println(ctx.body());

        Contributor contributor = Middlewares.contributorIsAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }

        String urgency = ctx.formParam("urgency");
        String description = ctx.formParam("description");
        String fridge_id = ctx.formParam("fridge");
        String set_inactive = ctx.formParam("set_inactive");

        System.err.println(ctx.body());
        try (Session session = DB.getSessionFactory().openSession()) {
            if (!FieldValidator.isValidEnumValue(Urgency.class, urgency)) {
                throw new IllegalArgumentException("invalid urgency");
            }
            if (!FieldValidator.isInt(fridge_id)) {
                throw new IllegalArgumentException("invalid fridge_id");
            }
            if (!FieldValidator.isString(description)) {
                throw new IllegalArgumentException("invalid description");
            }
            if (!FieldValidator.isBool(set_inactive)) {
                throw new IllegalArgumentException("invalid set_inactive");
            }

            String hql = "SELECT f " +
                    "FROM Fridge f " +
                    "WHERE f.id = :fridge_id";
            org.hibernate.query.Query<Fridge> query = session.createQuery(hql, Fridge.class);
            query.setParameter("fridge_id", fridge_id);
            Fridge fridge = query.uniqueResult();
            if (fridge == null) {
                throw new IllegalArgumentException("id inexistente");
            }
            Failure failure = new Failure(fridge, contributor, description, Urgency.valueOf(urgency), DateUtils.now());
            fridge.addIncident(failure);
            if (Boolean.parseBoolean(set_inactive)) //se chequea pq si no se podría activar una heladera mediante un reporte de falla
                fridge.setIsActive(false);
            DB.create(failure);
            DB.update(fridge);
            ctx.redirect("/dash/home");

        } catch (Exception e) {
            Logger.error("Exception ", e);
            // ditto
            ctx.json("TODO: make front error message - " + e.getMessage());
            return;
        }
    }

    public static void handleSubscription(Context ctx) {
    }

    public static void handleUnSubscription(Context ctx) {
    }

    public static void getFridgeInfo(Context ctx) {
        String fridgeIdParam = ctx.queryParam("id");

        if (fridgeIdParam == null) {
            ctx.status(400).result("Missing 'id' query parameter");
            return;
        }

        try {
            int fridgeId = Integer.parseInt(fridgeIdParam);
            org.hibernate.Transaction transaction = null;

            try (Session session = DB.getSessionFactory().openSession()) {

                // Existe fridge?
                Fridge fridge = session.get(Fridge.class, fridgeId);

                if (fridge == null) {
                    ctx.status(404).result("Fridge not found");
                    return;
                }

                // Fridge info y ID
                FridgeFullInfo fridgeFullInfo = new FridgeFullInfo();
                fridgeFullInfo.setFridgeId(fridgeId);

                // Meals
                String mealsHQL = "FROM Meal m WHERE m.fridge.id = :fridgeId";
                org.hibernate.query.Query<Meal> mealsQuery = session.createQuery(mealsHQL);
                mealsQuery.setParameter("fridgeId", fridgeId);
                List<Meal> meals = mealsQuery.getResultList();

                List<FridgeFullInfo.MealsData> mealsData = new java.util.ArrayList<>();
                for (Meal meal : meals) {
                    FridgeFullInfo.MealsData mealData = new FridgeFullInfo.MealsData();
                    mealData.setId(meal.getId());
                    mealData.setType(meal.getType());
                    mealData.setExpirationDate(meal.getExpirationDate());
                    mealData.setState(meal.getState());
                    mealData.setWeight(meal.getWeight());
                    mealData.setCalories(meal.getCalories());
                    mealsData.add(mealData);
                }
                fridgeFullInfo.setMeals(mealsData);

                // Failures
                String incidentsHQL = "FROM Incident i WHERE i.fridge.id = :fridgeId";

                List<FridgeFullInfo.IncidentsData> incidentsData = new java.util.ArrayList<>();
                fridgeFullInfo.setIncidents(incidentsData);

                ctx.json(fridgeFullInfo);
            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).result("Internal server error: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid 'id' query parameter");
         }


    }
}