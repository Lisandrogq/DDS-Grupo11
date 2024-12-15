package org.grupo11.Api.Controllers;

import java.util.List;
import java.util.Map;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.ApiResponse;
import org.grupo11.Api.HttpUtils;
import org.grupo11.Api.Middlewares;
import org.grupo11.Api.JsonData.FridgeInfo.FridgeFullInfo;
import org.grupo11.Broker.Rabbit;
import org.grupo11.DTOS.FridgeMovementDTO;
import org.grupo11.DTOS.FridgeTempDTO;
import org.grupo11.Services.Meal;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Contributor.Individual;
import org.grupo11.Services.Contributor.LegalEntity.LegalEntity;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeNotification;
import org.grupo11.Services.Fridge.FridgeNotifications;
import org.grupo11.Services.Fridge.FridgesManager;
import org.grupo11.Services.Fridge.Subscription;
import org.grupo11.Services.Fridge.Incident.Alert;
import org.grupo11.Services.Fridge.Incident.Failure;
import org.grupo11.Services.Fridge.Incident.Incident;
import org.grupo11.Services.Fridge.Incident.Urgency;
import org.grupo11.Services.Technician.Technician;
import org.grupo11.Services.Technician.TechnicianVisit;
import org.grupo11.Utils.DateUtils;
import org.grupo11.Utils.FieldValidator;
import org.grupo11.Utils.JSON;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.auth0.jwt.interfaces.DecodedJWT;

import io.javalin.http.Context;

public class FridgeController {

    public static void handleAddVisit(Context ctx) {
        Technician technician = Middlewares.technicianIsAuthenticated(ctx);
        if (technician == null) {
            ctx.redirect("/register/login");
            return;
        }

        String incident_id = ctx.formParam("incident_id");
        String is_fixed = ctx.formParam("fixed");
        String description = ctx.formParam("description");
        String fridge_id = ctx.formParam("fridge");

        if (!FieldValidator.isInt(incident_id)) {
            ctx.redirect("/dash/home?error=Invalid incident_id");
            return;
        }
        if (!FieldValidator.isBool(is_fixed)) {
            ctx.redirect("/dash/home?error=Invalid is_fixed");
            return;
        }
        if (!FieldValidator.isString(description)) {
            ctx.redirect("/dash/home?error=Invalid description");
            return;
        }
        if (!FieldValidator.isInt(fridge_id)) {
            ctx.redirect("/dash/home?error=Invalid fridge_id");
            return;
        }

        try (Session session = DB.getSessionFactory().openSession()) {

            String hql = "SELECT f " +
                    "FROM Fridge f " +
                    "WHERE f.id = :fridge_id";
            org.hibernate.query.Query<Fridge> query = session.createQuery(hql, Fridge.class);
            query.setParameter("fridge_id", fridge_id);
            Fridge fridge = query.uniqueResult();
            if (fridge == null) {
                ctx.redirect("/dash/home?error=Invalid fridge_id");
                return;
            }
            Incident incident = fridge.getIncidentById(Integer.parseInt(incident_id));
            if (incident == null || incident.hasBeenFixed() == true) {
                ctx.redirect("/dash/home?error=Invalid incident");
                return;
            }
            TechnicianVisit visit = new TechnicianVisit(technician, null, description, fridge.getName(),
                    fridge.getAddress(), DateUtils.now(), Boolean.parseBoolean(is_fixed));
            incident.addVisits(visit);
            if (Boolean.parseBoolean(is_fixed)) {
                incident.markAsFixed();
                if (fridge.getActiveIncidents().size() == 0)
                    fridge.setIsActive(true);
            }
            DB.create(visit);
            DB.update(incident);
            DB.update(fridge);
            ctx.redirect("/dash/home");

        } catch (Exception e) {
            Logger.error("Exception ", e);
            ctx.redirect("/dash/home?error=" + e.getMessage());
            return;
        }

    }

    public static void handleSubmitFailure(Context ctx) {
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
                ctx.redirect("/dash/home?error=Invalid urgency");
                return;
            }
            if (!FieldValidator.isInt(fridge_id)) {
                ctx.redirect("/dash/home?error=Invalid fridge_id");
                return;
            }
            if (!FieldValidator.isString(description)) {
                ctx.redirect("/dash/home?error=Invalid description");
                return;
            }
            if (!FieldValidator.isBool(set_inactive)) {
                ctx.redirect("/dash/home?error=Invalid set_inactive");
                return;
            }

            String hql = "SELECT f " +
                    "FROM Fridge f " +
                    "WHERE f.id = :fridge_id";
            Query<Fridge> query = session.createQuery(hql, Fridge.class);
            query.setParameter("fridge_id", fridge_id);
            Fridge fridge = query.uniqueResult();
            if (fridge == null) {
                ctx.redirect("/dash/home?error=Invalid fridge_id");
                return;
            }
            Failure failure = new Failure(fridge, contributor, description, Urgency.valueOf(urgency), DateUtils.now());
            Map<String, Object> return_map = fridge.addIncident(failure);
            FridgeNotification notification = (FridgeNotification) return_map.get("fridge_notification");
            Technician technician = (Technician) return_map.get("selected_technician");
            if (Boolean.parseBoolean(set_inactive)) // se chequea pq si no se podría activar una heladera mediante un
                                                    // reporte de falla
                fridge.setIsActive(false);
            if (technician != null)
                DB.update(technician);
            DB.create(notification);
            DB.create(failure);
            DB.update(fridge);
            ctx.redirect("/dash/home");

        } catch (Exception e) {
            Logger.error("Exception ", e);
            ctx.redirect("/dash/home?error=" + e.getMessage());
            return;
        }
    }

    public static void handleSubscription(Context ctx) {

        Contributor contributor = Middlewares.contributorIsAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }

        String type = ctx.formParam("type");
        String quantity = ctx.formParam("quantity");
        String fridge_id = ctx.formParam("fridge");

        if (type == null || fridge_id == null
                || (!"Malfunction".equals(type) && (quantity == null || quantity.isEmpty()))) {
            ctx.status(400).result("Missing or invalid parameters");
            return;
        }
        if (type.equals("Malfunction")) {
            quantity = "0";
        }
        if (!FieldValidator.isValidEnumValue(FridgeNotifications.class, type)) {
            ctx.status(400).result("Invalid type");
            return;
        }
        if (!FieldValidator.isInt(fridge_id)) {
            ctx.status(400).result("Invalid fridge_id");
            return;
        }
        if (!FieldValidator.isInt(quantity)) {
            ctx.status(400).result("Invalid quantity");
            return;
        }

        try (Session session = DB.getSessionFactory().openSession()) {

            String hql = "SELECT f " +
                    "FROM Fridge f " +
                    "WHERE f.id = :fridge_id";
            org.hibernate.query.Query<Fridge> query = session.createQuery(hql, Fridge.class);
            query.setParameter("fridge_id", fridge_id);
            Fridge fridge = query.uniqueResult();
            if (fridge == null) {
                ctx.status(404).result("Fridge not found");
                return;
            }

            Subscription subscription = new Subscription();
            subscription.setContributor(contributor);
            subscription.setType(FridgeNotifications.valueOf(type));
            subscription.setThreshold(Integer.parseInt(quantity));
            fridge.addNotificationSubscription(subscription);
            subscription.setFridge(fridge);
            DB.create(subscription);

            DB.update(fridge);

            ctx.redirect("/dash/home");
        } catch (Exception e) {
            Logger.error("Exception ", e);
            ctx.json(500).result("Internal server error: " + e.getMessage());
            return;
        }
    }

    public static void handleUnsubscription(Context ctx) {
        Contributor contributor = Middlewares.contributorIsAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }

        String fridgeId = ctx.queryParam("fridgeId");

        if (fridgeId == null || !FieldValidator.isInt(fridgeId)) {
            Logger.error("Invalid or missing subscription ID: " + fridgeId);
            ctx.status(400).result("Invalid or missing subscription ID.");
            return;
        }

        try (Session session = DB.getSessionFactory().openSession()) {
            session.beginTransaction();

            Fridge fridge = session.get(Fridge.class, Integer.parseInt(fridgeId));
            if (fridge == null) {
                Logger.error("Fridge not found: " + fridgeId);
                ctx.status(404).result("Fridge not found.");
                return;
            }
            if (!fridge.isSubscribed(contributor)) {
                Logger.error("User " + contributor.getId() + " is not subscribed to fridge " + fridge.getId());
                ctx.status(400).result("You are not subscribed to this fridge.");
                return;
            }

            fridge.getSubscriptions(contributor).forEach(subscription -> session.remove(subscription));
            fridge.removeSubscriber(contributor);
            session.merge(fridge);

            session.getTransaction().commit();

            ctx.status(200).result("Unsubscribed successfully.");
        } catch (Exception e) {
            Logger.error("Error while unsubscribing: ", e);
            ctx.status(500).result("Internal server error. Please try again later.");
        }

    }

    public static void getFridgeInfo(Context ctx) {
        String fridgeIdParam = ctx.queryParam("id");

        if (fridgeIdParam == null) {
            ctx.status(400).result("Missing 'id' query parameter");
            return;
        }

        try {
            int fridgeId = Integer.parseInt(fridgeIdParam);

            try (Session session = DB.getSessionFactory().openSession()) {
                Fridge fridge = session.get(Fridge.class, fridgeId);
                if (fridge == null) {
                    ctx.status(404).result("Fridge not found");
                    return;
                }

                FridgeFullInfo fridgeFullInfo = new FridgeFullInfo();
                fridgeFullInfo.setFridgeId(fridgeId);

                String mealsHQL = "FROM Meal m WHERE m.fridge.id = :fridgeId";
                org.hibernate.query.Query<Meal> mealsQuery = session.createQuery(mealsHQL, Meal.class);
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

                String incidentsHQL = "FROM Incident i WHERE i.fridge.id = :fridgeId";
                org.hibernate.query.Query<Incident> incidentsQuery = session.createQuery(incidentsHQL, Incident.class);
                incidentsQuery.setParameter("fridgeId", fridgeId);
                List<Incident> incidents = incidentsQuery.getResultList();

                List<FridgeFullInfo.IncidentsData> incidentsData = new java.util.ArrayList<>();
                for (Incident incident : incidents) {
                    FridgeFullInfo.IncidentsData incidentData = new FridgeFullInfo.IncidentsData();
                    incidentData.setId(incident.getId());
                    incidentData.setDetectedAt(incident.getDetectedAt());
                    incidentData.setHasBeenFixed(incident.hasBeenFixed());
                    if (incident instanceof Failure) {
                        Failure failure = (Failure) incident;
                        incidentData.setFailureDescription(failure.getReportedBy(), failure.getUrgency(),
                                failure.getDescription());
                    } else if (incident instanceof Alert) {
                        Alert alert = (Alert) incident;
                        incidentData.setAlertDescription(alert.getType());
                    } else {
                        Logger.error("Unknown incident type: " + incident.getClass().getName());
                    }
                    incidentsData.add(incidentData);
                }
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

    public static void handleSensorTemperatureUpdate(Context ctx) {
        DecodedJWT token = Middlewares.authenticatedFromHeader(ctx);
        if (token == null) {
            ctx.status(401).json(new ApiResponse(401));
            return;
        }
        Contributor contributor = HttpUtils.getContributorFromAccessToken(token);
        if (contributor instanceof Individual) {
            ctx.status(401).json(new ApiResponse(401, "Only legal entities can administrate fridges"));
            return;
        }
        LegalEntity legalEntity = (LegalEntity) contributor;

        try {
            FridgeTempDTO body = ctx.bodyAsClass(FridgeTempDTO.class);
            if (body == null) {
                ctx.status(400).json(new ApiResponse(400, "Invalid body."));
                return;
            }
            boolean isOwner = FridgesManager.getInstance().isFridgeOwnerByFridgeId(legalEntity, body.fridge_id);
            if (!isOwner) {
                ctx.status(401).json(new ApiResponse(401, "You are not the administrator of this fridge"));
                return;
            }

            Rabbit.getInstance().send("temp_update", "", JSON.stringify(body));
            ctx.status(200).json(new ApiResponse(200));
        } catch (Exception e) {
            ctx.status(500).json(new ApiResponse(500));
        }
    }

    public static void handleSensorMovementUpdate(Context ctx) {
        DecodedJWT token = Middlewares.authenticatedFromHeader(ctx);
        if (token == null) {
            ctx.status(401).json(new ApiResponse(401));
            return;
        }
        Contributor contributor = HttpUtils.getContributorFromAccessToken(token);
        if (contributor instanceof Individual) {
            ctx.status(401).json(new ApiResponse(401, "Only legal entities can administrate fridges"));
            return;
        }
        LegalEntity legalEntity = (LegalEntity) contributor;

        try {
            FridgeMovementDTO body = ctx.bodyAsClass(FridgeMovementDTO.class);
            if (body == null) {
                ctx.status(400).json(new ApiResponse(400, "Invalid body."));
                return;
            }
            boolean isOwner = FridgesManager.getInstance().isFridgeOwnerByFridgeId(legalEntity, body.fridge_id);
            if (!isOwner) {
                ctx.status(401).json(new ApiResponse(401, "You are not the administrator of this fridge"));
                return;
            }

            Rabbit.getInstance().send("movement_update", "", JSON.stringify(body));
            ctx.status(200).json(new ApiResponse(200));
        } catch (Exception e) {
            ctx.status(500).json(new ApiResponse(500));
        }
    }
}
