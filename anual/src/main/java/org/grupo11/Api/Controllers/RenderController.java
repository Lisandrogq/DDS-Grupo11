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
            Contributor contributor = Middlewares.contributorIsAuthenticated(ctx);
            Technician technician = Middlewares.technicianIsAuthenticated(ctx);
            if (contributor == null && technician == null) {
                Logger.info("Contributor or techinician not found");
                ctx.redirect("/register/login");
                return;
            }
            Map<String, Object> model = null;
            if (contributor != null) {
                model = getContributorModel(contributor, ctx);
                if (model == null)
                    return;// caso de error en la generacion del modelo (TODO: estaría bueno manejar
                // errores con throw como en ContributionsController pero notime)

            }
            if (technician != null) {
                model = getTechnicianModel(technician, ctx);
                if (model == null)
                    return;
            }
            String page = "templates/dash/home"
                    + (technician != null ? "TECH" : contributor.isIndividual() ? "IND" : "LE");
            ctx.render(page + ".html", model);
        } catch (Exception e) {
            Logger.error("Error while rendering dashboard", e);
            ctx.status(500);
        }
    }

    public static Map<String, Object> getContributorModel(Contributor contributor, Context ctx) {
        String name;
        if (contributor instanceof Individual) {
            name = ((Individual) contributor).getName();
        } else {
            name = ((LegalEntity) contributor).getName();
        }

        // user
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("points", contributor.getPoints());

        // temperature
        List<Map<String, Object>> temperatures = new ArrayList<>();

        // donations
        List<Map<String, Object>> donations = new ArrayList<>();
        Map<String, Object> donatedFridge = new HashMap<>();
        donatedFridge.put("name", "Medrano");
        donatedFridge.put("temp", 3);
        donatedFridge.put("reserved", 40);
        donatedFridge.put("state", "Active");
        donatedFridge.put("meals", 120);

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
            session.close();

        } catch (Exception e) {
            Logger.error("Could not serve contributor recognitions {}", e);
            ctx.status(500).json(new ApiResponse(500));
            return null;
        }

        // rewards
        List<Map<String, Object>> rewards = new ArrayList<>();

        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "FROM Reward r ";// faltaría el where con el userid cuando marquitos haga el sso
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
            session.close();
        } catch (Exception e) {
            Logger.error("Could not serve contributor recognitions {}", e);
            ctx.status(500).json(new ApiResponse(500));
            return null;
        }
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("temperatures", temperatures);
        model.put("donations", donations);
        model.put("fridges", fridges);
        model.put("rewards", rewards);
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
            Logger.error("Could not serve contributor recognitions {}", e);
            ctx.status(500).json(new ApiResponse(500));
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
             Logger.error("Could not serve contributor recognitions {}", e);
             ctx.status(500).json(new ApiResponse(500));
             return null;
         }

        model.put("user", user);
        model.put("visits", visits);
        model.put("fridges", fridges);
        return model;
    }
}
