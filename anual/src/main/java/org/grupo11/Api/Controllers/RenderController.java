package org.grupo11.Api.Controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.ApiResponse;
import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.FridgeAdmin;
import org.grupo11.Services.Contributions.MealDistribution;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributions.MoneyDonation;
import org.grupo11.Services.Contributions.PersonRegistration;
import org.grupo11.Services.Contributions.RewardContribution;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.Subscription;
import org.grupo11.Services.Rewards.Reward;
import org.grupo11.Services.Technician.Technician;
import org.grupo11.Services.Technician.TechnicianVisit;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.grupo11.Api.Middlewares;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Contributor.Individual;
import org.grupo11.Services.Contributor.LegalEntity.LegalEntity;

import java.util.ArrayList;
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
            Map<String, Object> model = null;

            if (Middlewares.authenticatedAsAdmin(ctx)) {
                model = getAdminModel(ctx);
                renderDashboard(ctx, model, "ADMIN");
                return;
            }

            Contributor contributor = Middlewares.contributorIsAuthenticated(ctx);
            if (contributor != null) {
                model = getContributorModel(contributor, ctx);
                renderDashboard(ctx, model, contributor.isIndividual() ? "IND" : "LE");
                return;
            }

            Technician technician = Middlewares.technicianIsAuthenticated(ctx);
            if (technician != null) {
                model = getTechnicianModel(technician, ctx);
                renderDashboard(ctx, model, "TECH");
                return;
            }

            ctx.redirect("/register/login");
        } catch (Exception e) {
            Logger.error("Error while rendering dashboard", e);
            ctx.redirect("/register/login");
        }
    }

    public static void renderDashboard(Context ctx, Map<String, Object> model, String pageSuffix) {
        if (model == null) {
            ctx.redirect("/register/login");
            return;
        }
        String page = "templates/dash/home" + pageSuffix;
        ctx.render(page + ".html", model);
    }

    public static Map<String, Object> getContributorModel(Contributor contributor, Context ctx) {
        String name;
        if (contributor instanceof Individual) {
            name = ((Individual) contributor).getName();
        } else {
            name = ((LegalEntity) contributor).getName();
        }

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        int points = contributor.getPointsAsInt();
        String pointsAsString = String.valueOf(points);
        user.put("points", pointsAsString);

        List<Map<String, Object>> donations = new ArrayList<>();
        Map<String, Object> donatedFridge = new HashMap<>();
        donatedFridge.put("name", "");
        donatedFridge.put("temp", 0);
        donatedFridge.put("reserved", 0);
        donatedFridge.put("state", "");
        donatedFridge.put("meals", 0);

        List<Map<String, Object>> fridges = new ArrayList<>();
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "FROM Fridge f ";
            Query<Fridge> query = session.createQuery(hql, Fridge.class);
            List<Fridge> results = query.getResultList();
            System.out.println("results.size(): " + results.size());
            for (Fridge fridge : results) {
                Map<String, Object> fridgeMap = fridge.toMap();
                if (fridge.isSubscribed(contributor)) {
                    fridgeMap.put("subscribed", "true");
                } else {
                    fridgeMap.put("subscribed", "false");
                }
                fridges.add(fridgeMap);
            }
            session.close();

        } catch (Exception e) {
            Logger.error("Could not serve contributor recognitions {}", e);
            ctx.status(500).json(new ApiResponse(500));
            return null;
        }

        List<Map<String, Object>> rewards = new ArrayList<>();
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "FROM Reward r ";
            Query<Reward> query = session.createQuery(hql, Reward.class);
            List<Reward> results = query.getResultList();
            System.out.println("results.size(): " + results.size());
            for (Reward reward : results) {
                if (reward.getQuantity() > 0) {
                    rewards.add(reward.toMap());
                }
            }
            session.close();

        } catch (Exception e) {
            Logger.error("Could not serve contributor recognitions {}", e);
            ctx.status(500).json(new ApiResponse(500));
            return null;
        }

        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT c FROM Contribution c WHERE contributor = :contributor";
            Query<Contribution> query = session.createQuery(hql, Contribution.class);
            query.setParameter("contributor", contributor);
            List<Contribution> results = query.getResultList();
            System.out.println("results.size(): " + results.size());
            for (Contribution contribution : results) {
                Map<String, Object> donation = new HashMap<>();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String formattedContributionDate = sdf.format(contribution.getDate());

                if (contribution instanceof MealDonation) {
                    MealDonation mealDonation = (MealDonation) contribution;
                    donation.put("emoji", "🍕");
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
                    donation.put("type", "Person Registration");
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
            session.close();
        } catch (Exception e) {
            return null;
        }

        // subscriptions
        List<Map<String, Object>> notifications = new ArrayList<>();

        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "FROM Subscription s WHERE s.contributor = :contributor";
            Query<Subscription> query = session.createQuery(hql, Subscription.class);
            query.setParameter("contributor", contributor);
            List<Subscription> results = query.getResultList();
            System.out.println("subscriptions.size(): " + results.size());

            for (Subscription subscription : results) {
                for (String notification_msg : subscription.getNotifications()) {
                    Map<String, Object> notificacion = new HashMap<>();
                    notificacion.put("description", notification_msg);
                    notifications.add(notificacion);
                }
            }
            session.close();

        } catch (Exception e) {
            return null;
        }

        Map<String, Object> model = new HashMap<>();
        String error = ctx.queryParam("error");

        model.put("user", user);
        model.put("notifications", notifications);
        model.put("donations", donations);
        model.put("fridges", fridges);
        model.put("rewards", rewards);
        model.put("error", error);

        return model;
    }

    public static Map<String, Object> getTechnicianModel(Technician technician, Context ctx) {
        Map<String, Object> model = new HashMap<>();

        Map<String, Object> user = new HashMap<>();
        user.put("name", technician.getName());

        // fridges
        List<Map<String, Object>> fridges = new ArrayList<>();

        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "FROM Fridge f ";
            Query<Fridge> query = session.createQuery(hql, Fridge.class);
            List<Fridge> results = query.getResultList();
            System.out.println("results.size(): " + results.size());
            for (Fridge fridge : results) {
                fridges.add(fridge.toMap());
            }
            session.close();

        } catch (Exception e) {

            return null;
        }

        // fridges
        List<Map<String, Object>> visits = new ArrayList<>();

        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "FROM TechnicianVisit v ";
            Query<TechnicianVisit> query = session.createQuery(hql, TechnicianVisit.class);
            List<TechnicianVisit> results = query.getResultList();
            System.out.println("results.size(): " + results.size());
            for (TechnicianVisit visit : results) {
                visits.add(visit.toMap());
            }
            session.close();

        } catch (Exception e) {
            return null;
        }

        model.put("user", user);
        model.put("visits", visits);
        model.put("fridges", fridges);
        return model;
    }

    public static Map<String, Object> getAdminModel(Context ctx) {
        Map<String, Object> model = new HashMap<>();

        return model;
    }
}
