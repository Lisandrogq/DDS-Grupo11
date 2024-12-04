package org.grupo11.Api.Controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.ApiResponse;
import org.grupo11.Services.Meal;
import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.FridgeAdmin;
import org.grupo11.Services.Contributions.MealDistribution;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributions.MoneyDonation;
import org.grupo11.Services.Contributions.PersonRegistration;
import org.grupo11.Services.Contributions.RewardContribution;
import org.grupo11.Services.Contributor.Individual;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Rewards.Reward;
import org.grupo11.Services.Rewards.RewardCategory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.grupo11.Api.HttpUtils;
import org.grupo11.Api.Middlewares;
import org.grupo11.Services.Contributor.Contributor;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;

import io.javalin.http.Context;

public class RenderController {
    public static void renderRegisterPages(Context ctx) {
        try {
            String filename = ctx.pathParam("filename");
            Path filePath = Paths.get("src/main/resources/templates/register/", filename + ".html");
            String error = ctx.queryParam("error");
            Map<String, Object> model = new HashMap<>();
            model.put("error", error);

            if (Files.exists(filePath)) {
                ctx.render("templates/register/" + filename + ".html", model);
            } else {
                ctx.status(404);
            }
        } catch (Exception e) {
            ctx.status(500);
        }
    }

    public static void renderDashboardPage(Context ctx) {
        try {
            DecodedJWT jwt = Middlewares.isAuthenticated(ctx);
            if (jwt == null) {
                ctx.redirect("/register/login");
                return;
            }
            Contributor contributor = HttpUtils.getContributorFromAccessToken(jwt);
            if (contributor == null) {
                ctx.redirect("/register/login");
                return;
            }

            // user
            Map<String, Object> user = new HashMap<>();
            user.put("name", "John");
            user.put("points", 1213);

            // temperature
            List<Map<String, Object>> temperatures = new ArrayList<>();
            Map<String, Object> fridge1Temp = new HashMap<>();
            fridge1Temp.put("fridge", "Medrano");
            fridge1Temp.put("temp", 10);

            Map<String, Object> fridge2Temp = new HashMap<>();
            fridge2Temp.put("fridge", "Lugano");
            fridge2Temp.put("temp", 15);

            temperatures.add(fridge1Temp);
            temperatures.add(fridge2Temp);

            // donations
            List<Map<String, Object>> donations = new ArrayList<>();
            Map<String, Object> donation1 = new HashMap<>();
            Map<String, Object> donatedFridge = new HashMap<>();
            donatedFridge.put("name", "Medrano");
            donatedFridge.put("temp", 3);
            donatedFridge.put("reserved", 40);
            donatedFridge.put("state", "Active");
            donatedFridge.put("meals", 120);

            donation1.put("emoji", "🥘");
            donation1.put("type", "Food");
            donation1.put("desc", "You have donated “musaka”");
            donation1.put("fridge", donatedFridge);

            Map<String, Object> donation2 = new HashMap<>();
            donation2.put("emoji", "👨");
            donation2.put("type", "Registered person");
            donation2.put("desc", "You have registered “valentina” as part of our community");
            donation2.put("fridge", donatedFridge);

            /*
             * donations.add(donation1);
             * donations.add(donation2);
             */
            // fridges
            List<Map<String, Object>> fridges = new ArrayList<>();

            try (Session session = DB.getSessionFactory().openSession()) {
                String hql = "FROM Fridge f ";// faltaría el where con el userid cuando marquitos haga el sso
                Query<Fridge> query = session.createQuery(hql, Fridge.class);
                List<Fridge> results = query.getResultList();
                System.out.println("results.size(): " + results.size());
                for (Fridge fridge : results) {
                    fridges.add(fridge.toMap());
                }

            } catch (Exception e) {
                Logger.error("Could not serve contributor recognitions {}", e);
                ctx.status(500).json(new ApiResponse(500));
                return;
            }

            // rewards
            List<Map<String, Object>> rewards = new ArrayList<>();

            try (Session session = DB.getSessionFactory().openSession()) {
                String hql = "FROM Reward r ";// faltaría el where con el userid cuando marquitos haga el sso
                Query<Reward> query = session.createQuery(hql, Reward.class);
                List<Reward> results = query.getResultList();
                System.out.println("results.size(): " + results.size());
                for (Reward reward : results) {
                    rewards.add(reward.toMap());
                }

            } catch (Exception e) {
                Logger.error("Could not serve contributor recognitions {}", e);
                ctx.status(500).json(new ApiResponse(500));
                return;
            }

            try (Session session = DB.getSessionFactory().openSession()) {
                String hql = "FROM Contribution c ";// faltaría el where con el userid cuando marquitos haga el sso
                Query<Contribution> query = session.createQuery(hql, Contribution.class);
                List<Contribution> results = query.getResultList();
                System.out.println("results.size(): " + results.size());
                for (Contribution contribution : results) {
                    Map<String, Object> donation = new HashMap<>();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String formattedContributionDate = sdf.format(new Date(contribution.getDate()));

                    if (contribution instanceof MealDonation) {
                        MealDonation mealDonation = (MealDonation) contribution;
                        donation.put("emoji", "🍕"); // cambie el emogi pq el otro es muy grande y se coje la alineacion
                                                     // (🥘)
                        donation.put("type", "Meal Donation");
                        donation.put("desc",
                                "On " + formattedContributionDate + " you have donated "
                                        + mealDonation.getMeal().getType() + " to "
                                        + mealDonation.getMeal().getFridge().getName());
                        donation.put("fridge", mealDonation.getMeal().getFridge().toMap());
                    } else if (contribution instanceof MealDistribution) {
                        MealDistribution mealDistribution = (MealDistribution) contribution;
                        donation.put("emoji", "📦");
                        donation.put("type", "Meal Distribution");
                        donation.put("desc",
                                "On " + formattedContributionDate + " you have moved a meal from "
                                        + mealDistribution.getOriginFridge().getName() + " to "
                                        + mealDistribution.getDestinyFridge().getName());
                        donation.put("fridge", donatedFridge);
                    } else if (contribution instanceof FridgeAdmin) {
                        FridgeAdmin fridgeAdmin = (FridgeAdmin) contribution;
                        donation.put("emoji", "🧞‍♂️");
                        donation.put("type", "Fridge administration");
                        donation.put("desc", "You are administrating " + fridgeAdmin.getFridge().getName() + " since "
                                + formattedContributionDate);
                        donation.put("fridge", fridgeAdmin.getFridge().toMap());
                    } else if (contribution instanceof MoneyDonation) {
                        MoneyDonation moneyDonation = (MoneyDonation) contribution;
                        donation.put("emoji", "💰");
                        donation.put("type", "Money Donation");
                        donation.put("desc", "On " + formattedContributionDate + " you have donated "
                                + moneyDonation.getAmount() + "$");
                        donation.put("fridge", donatedFridge);
                    } else if (contribution instanceof PersonRegistration) {
                        PersonRegistration personRegistration = (PersonRegistration) contribution;
                        donation.put("emoji", "👲🏽");
                        donation.put("type", "Person Registrarion");
                        donation.put("desc", "On " + formattedContributionDate + " you have registered "
                                + personRegistration.getPerson().getName());
                        donation.put("fridge", donatedFridge);
                    } else if (contribution instanceof RewardContribution) {
                        RewardContribution rewardContribution = (RewardContribution) contribution;
                        donation.put("emoji", "🏆");
                        donation.put("type", "Reward Contribution");
                        donation.put("desc", "On " + formattedContributionDate + " you have offered "
                                + rewardContribution.getReward().getName() + " as a reward");
                        donation.put("fridge", donatedFridge);
                    }
                    donations.add(donation);
                }

            } catch (Exception e) {
                Logger.error("Could not serve contributor recognitions {}", e);
                ctx.status(500).json(new ApiResponse(500));
                return;
            }

            // final model
            Map<String, Object> model = new HashMap<>();
            model.put("user", user);
            model.put("temperatures", temperatures);
            model.put("donations", donations);
            model.put("fridges", fridges);
            model.put("rewards", rewards);

            ctx.render("templates/dash/home.html", model);
        } catch (

        Exception e) {
            ctx.status(500);
        }
    }
}
