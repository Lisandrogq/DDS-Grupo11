package org.grupo11.Api.Controllers;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.Middlewares;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Utils.DateUtils;
import java.util.List;
import org.grupo11.Api.JsonData.FridgeInfo.FridgeFullInfo;
import org.grupo11.Services.Technician.Technician;
import org.grupo11.Services.Technician.TechnicianVisit;
import org.grupo11.Utils.FieldValidator;
import org.hibernate.Session;
import org.grupo11.Services.Fridge.Incident.Alert;
import org.grupo11.Services.Fridge.Incident.Failure;
import org.grupo11.Services.Fridge.Incident.Incident;
import org.grupo11.Services.Fridge.Incident.Urgency;
import org.grupo11.Services.Meal;
import org.grupo11.Services.Fridge.Fridge;

import io.javalin.http.Context;

public class FridgeController {
    public static void handleAddVisit(Context ctx) {
        System.out.println(ctx.body());
        // Obtengo el Technician
        Technician technician = Middlewares.technicianIsAuthenticated(ctx);
        if (technician == null) {
            ctx.redirect("/register/login");
            return;
        }

        String incident_id = ctx.formParam("incident_id");
        String is_fixed = ctx.formParam("fixed");
        String description = ctx.formParam("description");
        String fridge_id = ctx.formParam("fridge");

        try (Session session = DB.getSessionFactory().openSession()) {
            if (!FieldValidator.isInt(incident_id)) {
                throw new IllegalArgumentException("invalid incident_id");
            }
            if (!FieldValidator.isBool(is_fixed)) {
                throw new IllegalArgumentException("invalid is_fixed");
            }
            if (!FieldValidator.isString(description)) {
                throw new IllegalArgumentException("invalid description");
            }
            if (!FieldValidator.isInt(fridge_id)) {
                throw new IllegalArgumentException("invalid fridge_id");
            }

            String hql = "SELECT f " +
                    "FROM Fridge f " +
                    "WHERE f.id = :fridge_id";
            org.hibernate.query.Query<Fridge> query = session.createQuery(hql, Fridge.class);
            query.setParameter("fridge_id", fridge_id);
            Fridge fridge = query.uniqueResult();
            if (fridge == null) {
                throw new IllegalArgumentException("fridge_id inexistente");
            }
            Incident incident = fridge.getIncidentById(Integer.parseInt(incident_id));
            if (incident == null || incident.hasBeenFixed() == true) {
                throw new IllegalArgumentException("incident_id inexistente o ya arreglado");
            }
            TechnicianVisit visit = new TechnicianVisit(technician, null, description, fridge.getName(),
                    fridge.getAddress(), DateUtils.now(), Boolean.parseBoolean(is_fixed));
            incident.addVisits(visit);
            if (Boolean.parseBoolean(is_fixed)) {
                incident.markAsFixed();
                if (fridge.getActiveIncidents().size() == 0)
                    fridge.setIsActive(true);
                // aca no se esta teniendo en cuenta si los incidentes que quedan fueron
                // marcados para desactivar el fridge :p pero la consigna no
                // lo pedia asi q no pasa nada
            }
            DB.create(visit);
            DB.update(incident);
            DB.update(fridge);
            ctx.redirect("/dash/home");

        } catch (Exception e) {
            Logger.error("Exception ", e);
            // ditto
            ctx.json("TODO: make front error message - " + e.getMessage());
            return;
        }

    }

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
            if (Boolean.parseBoolean(set_inactive)) // se chequea pq si no se podría activar una heladera mediante un
                                                    // reporte de falla
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

                // Incidents
                String incidentsHQL = "FROM Incident i WHERE i.fridge.id = :fridgeId";
                org.hibernate.query.Query<Incident> incidentsQuery = session.createQuery(incidentsHQL);
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
                        incidentData.setFailureDescription(failure.getReportedBy(), failure.getUrgency(), failure.getDescription());
                    }
                    else if (incident instanceof Alert) {
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
}
