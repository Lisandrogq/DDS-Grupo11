package org.grupo11.Api.Controllers;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.Middlewares;
import org.grupo11.Services.Meal;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Contributor.ContributorsManager;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.Incident.Urgency;
import org.grupo11.Services.Fridge.Incident.Failure;
import org.grupo11.Utils.DateUtils;
import org.grupo11.Utils.FieldValidator;
import org.hibernate.Session;

import io.javalin.http.Context;

public class FridgeController {
    public static void handleSubmitFailure(Context ctx) {
        System.out.println(ctx.body());

        Contributor contributor = Middlewares.isAuthenticated(ctx);
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
}
