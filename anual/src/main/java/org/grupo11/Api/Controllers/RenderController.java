package org.grupo11.Api.Controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.ApiResponse;
import org.grupo11.Api.HttpStatus;
import org.grupo11.Services.Credentials;
import org.grupo11.Services.Meal;
import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.FridgeAdmin;
import org.grupo11.Services.Contributions.MealDistribution;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributions.MoneyDonation;
import org.grupo11.Services.Contributions.PersonRegistration;
import org.grupo11.Services.Contributions.RewardContribution;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.Subscription;
import org.grupo11.Services.Reporter.Report;
import org.grupo11.Services.Reporter.Reporter;
import org.grupo11.Services.Rewards.Reward;
import org.grupo11.Services.Technician.Technician;
import org.grupo11.Services.Technician.TechnicianVisit;
import org.grupo11.Utils.GetNearestTech;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.grupo11.Api.Middlewares;
import org.grupo11.Enums.AuthProvider;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Contributor.Individual;
import org.grupo11.Services.Contributor.LegalEntity.LegalEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import io.javalin.http.Context;

public class RenderController {
    public static void landing(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        Session session = DB.getSessionFactory().openSession();
        model.put("meals_donated", DB.getEntityCount(session, Meal.class));
        model.put("num_fridges", DB.getEntityCount(session, Fridge.class));
        model.put("num_contributors", DB.getEntityCount(session, Contributor.class));
        ctx.render("templates/landing.html", model);
    }

    public static void favicon(Context ctx) {
        try {
            Path faviconPath = Paths.get("src/main/resources/public/assets/favicon.ico");
            ctx.contentType("image/x-icon");
            ctx.result(Files.newInputStream(faviconPath));
        } catch (Exception e) {
            ctx.status(404).result(HttpStatus.fromCode(404).getMessage());
        }
    }

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
                List<Map<String, Object>> providers = getProvidersModel(ctx);
                model.put("providers", providers);
                renderDashboard(ctx, model, contributor.isIndividual() ? "IND" : "LE");
                return;
            }

            Technician technician = Middlewares.technicianIsAuthenticated(ctx);
            if (technician != null) {
                model = getTechnicianModel(technician, ctx);
                List<Map<String, Object>> providers = getProvidersModel(ctx);
                model.put("providers", providers);
                renderDashboard(ctx, model, "TECH");
                return;
            }

            Logger.error("No user authenticated");
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

    public static List<Map<String, Object>> getProvidersModel(Context ctx) {
        Credentials credentials = Middlewares.authenticated(ctx);
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "Select c.provider FROM Credentials c WHERE c.ownerId = :ownerId";
            Query<AuthProvider> query = session.createQuery(hql, AuthProvider.class);
            query.setParameter("ownerId", credentials.getOwnerId());

            List<AuthProvider> connectedProviders = query.getResultList();
            List<AuthProvider> allProviders = Arrays.asList(AuthProvider.values());

            List<Map<String, Object>> providers = new ArrayList<>();
            for (AuthProvider authProvider : allProviders) {
                if (authProvider.equals(AuthProvider.FridgeBridge))
                    continue;

                Map<String, Object> provider = new HashMap<>();
                provider.put("provider", authProvider.toString());
                provider.put("connected", connectedProviders.contains(authProvider));
                provider.put("img", "/public/assets/brands/" + authProvider.toString().toLowerCase() + ".png");
                providers.add(provider);
            }

            return providers;

        } catch (Exception e) {
            Logger.error("Could not serve contributor recognitions {}", e);
            ctx.status(500).json(new ApiResponse(500));
            return null;
        }
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
            Logger.info("Fridges results.size(): " + results.size());
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
            Logger.info("Rewards results.size(): " + results.size());
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
            Logger.info("Contributions results.size(): " + results.size());
            for (Contribution contribution : results) {
                Map<String, Object> donation = new HashMap<>();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String formattedContributionDate = sdf.format(contribution.getDate());

                if (contribution instanceof MealDonation) {
                    MealDonation mealDonation = (MealDonation) contribution;
                    donation.put("emoji", "🍕");
                    donation.put("type", "Meal Donation");

                    if (mealDonation.getFridge() != null) {
                        Logger.info("No esta null");
                        donation.put("desc",
                                "On " + formattedContributionDate + " you have donated "
                                        + mealDonation.getMeal().getType() + " to "
                                        + mealDonation.getFridge().getName());
                    } else {
                        Logger.info("Esta null");
                        donation.put("desc",
                                "On " + formattedContributionDate + " you have donated "
                                        + mealDonation.getMeal().getType());
                    }

                } else if (contribution instanceof MealDistribution) {

                    MealDistribution mealDistribution = (MealDistribution) contribution;
                    donation.put("emoji", "📦");
                    donation.put("type", "Meal Distribution");

                    if (mealDistribution.getDestinyFridge() != null && mealDistribution.getOriginFridge() != null) {
                        donation.put("desc",
                                "On " + formattedContributionDate + " you have moved " + mealDistribution.getQuantity()
                                        + " meals from "
                                        + mealDistribution.getOriginFridge().getName() + " to "
                                        + mealDistribution.getDestinyFridge().getName());
                    } else {
                        donation.put("desc",
                                "On " + formattedContributionDate + " you have moved " + mealDistribution.getQuantity()
                                        + " meals");
                    }

                } else if (contribution instanceof FridgeAdmin) {

                    FridgeAdmin fridgeAdmin = (FridgeAdmin) contribution;
                    donation.put("emoji", "🧞‍♂️");
                    donation.put("type", "Fridge administration");
                    donation.put("desc", "You are administrating " + fridgeAdmin.getFridge().getName() + " since "
                            + formattedContributionDate);

                } else if (contribution instanceof MoneyDonation) {

                    MoneyDonation moneyDonation = (MoneyDonation) contribution;
                    donation.put("emoji", "💰");
                    donation.put("type", "Money Donation");
                    donation.put("desc", "On " + formattedContributionDate + " you have donated "
                            + moneyDonation.getAmount() + "$");

                } else if (contribution instanceof PersonRegistration) {

                    PersonRegistration personRegistration = (PersonRegistration) contribution;
                    donation.put("emoji", "👲🏽");
                    donation.put("type", "Person Registration");
                    if (personRegistration.getPerson() != null) {
                        donation.put("desc", "On " + formattedContributionDate + " you have registered "
                                + personRegistration.getPerson().getName());
                    } else {
                        donation.put("desc", "On " + formattedContributionDate + " you have registered a person");
                    }

                } else if (contribution instanceof RewardContribution) {
                    RewardContribution rewardContribution = (RewardContribution) contribution;
                    donation.put("emoji", "🏆");
                    donation.put("type", "Reward Contribution");
                    donation.put("desc", "On " + formattedContributionDate + " you have offered "
                            + rewardContribution.getReward().getName() + " as a reward");
                }
                donations.add(donation);
            }
            Logger.info("Hasta aca no llego");
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
            Logger.info("Subscriptions results.size(): " + results.size());

            for (Subscription subscription : results) {
                Map<String, Object> notificacion = new HashMap<>();
                switch (subscription.getType()) {
                    case LowInventory:
                        if (subscription.getThreshold() >= subscription.getFridge().getMeals().size()) {
                            notificacion.put("description",
                                    subscription.getFridge().getName() + " Fridge almost empty");
                            notifications.add(notificacion);
                        }
                        break;
                    case NearFullInventory:
                        if (subscription.getThreshold() <= subscription.getFridge().getMeals().size()) {
                            notificacion.put("description", subscription.getFridge().getName() + " Fridge almost full");
                            notifications.add(notificacion);
                        }
                        break;
                    case Malfunction:
                        if (subscription.getFridge().getActiveIncidents().size() > 0) {
                            notificacion.put("description",
                                    subscription.getFridge().getName() + " fridge is malfunctioning");
                            notifications.add(notificacion);
                        }
                        break;
                    default:
                        break;
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
        String error = ctx.queryParam("error");

        Map<String, Object> user = new HashMap<>();
        user.put("name", technician.getName());

        List<Map<String, Object>> fridges = new ArrayList<>();
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "FROM Fridge f ";
            Query<Fridge> query = session.createQuery(hql, Fridge.class);
            List<Fridge> results = query.getResultList();
            for (Fridge fridge : results) {
                fridges.add(fridge.toMap());
            }
            session.close();

        } catch (Exception e) {
            return null;
        }

        List<Map<String, Object>> visits = new ArrayList<>();
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "FROM TechnicianVisit v WHERE v.technician = :technician";
            Query<TechnicianVisit> query = session.createQuery(hql, TechnicianVisit.class);
            query.setParameter("technician", technician);

            List<TechnicianVisit> results = query.getResultList();
            for (TechnicianVisit visit : results) {
                visits.add(visit.toMap());
            }
            session.close();
        } catch (Exception e) {
            return null;
        }

        List<String> alerts = new ArrayList<String>();
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "FROM Fridge f ";
            Query<Fridge> query = session.createQuery(hql, Fridge.class);
            List<Fridge> results = query.getResultList();
            Logger.info("Fridges results.size(): " + results.size());
            for (Fridge fridge : results) {
                HashMap<String, Object> map = GetNearestTech.getNearestTechnician(fridge.getLat(), fridge.getLon());
                Technician nearest_technician = (Technician) map.get("technician");
                int distance = ((Double) map.get("distance")).intValue();
                if (fridge.getActiveIncidents().size() > 0 && nearest_technician.getId().equals(technician.getId())) {
                    alerts.add(fridge.getName() + " fridge is malfunctioning, its " + distance + "mts away");
                }
            }
            session.close();

        } catch (Exception e) {
            Logger.error("Could not serve contributor recognitions {}", e);
            ctx.status(500).json(new ApiResponse(500));
            return null;
        }

        model.put("user", user);
        model.put("visits", visits);
        model.put("fridges", fridges);
        model.put("error", error);
        model.put("alerts", alerts);
        return model;
    }

    public static Map<String, Object> getAdminModel(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        String error = ctx.queryParam("error");
        model.put("error", error);

        List<Map<String, Object>> reports = new ArrayList<>();

        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "FROM Report r ";
            Query<Report> query = session.createQuery(hql, Report.class);
            List<Report> results = query.getResultList();

            for (Report report : results) {
                Map<String, Object> map = new HashMap<>();
                map.put("reportId", Long.toString(report.getId()));
                map.put("fromDate", Long.toString(report.getFromDate()));
                map.put("toDate", Long.toString(report.getToDate()));
                reports.add(map);
            }
            session.close();
        } catch (Exception e) {
            Logger.error("ERROR ", e);
            return null;
        }

        Map<String, Object> reportsFrequency = new HashMap<>();
        reportsFrequency.put("frequency", Reporter.getInstance().getGenReportsEvery());
        reportsFrequency.put("unit", Reporter.getInstance().getGenReportsEveryUnit());
        Logger.info("Frequency: " + Reporter.getInstance().getGenReportsEvery());
        Logger.info("Unit: " + Reporter.getInstance().getGenReportsEveryUnit());

        model.put("reports", reports);
        model.put("Reportsemoji", "🖨️");
        model.put("reportsFrequency", reportsFrequency);
        return model;
    }
}
