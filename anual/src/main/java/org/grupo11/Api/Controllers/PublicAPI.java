package org.grupo11.Api.Controllers;

import java.util.List;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Services.Contributor.Contributor;
import org.hibernate.Session;
import org.hibernate.query.Query;

import io.javalin.http.Context;

public class PublicAPI {
    public static void handleContributorRecognitions(Context ctx) {
        String minPointsParam = ctx.queryParam("min_points");
        String minMealsParam = ctx.queryParam("min_meals");
        String sizeParam = ctx.queryParam("size");

        int minPoints = minPointsParam != null ? Integer.parseInt(minPointsParam) : 0;
        int minMeals = minMealsParam != null ? Integer.parseInt(minMealsParam) : 0;
        int size = sizeParam != null ? Integer.parseInt(sizeParam) : 10;

        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT c " +
                    "FROM Contributor c " +
                    "JOIN c.contributions contr " +
                    "JOIN MealDonation md ON contr.id = md.id " +
                    "WHERE c.points >= :minPoints " +
                    "GROUP BY c " +
                    "HAVING COUNT(md) >= :minMeals";
            Query<Contributor> query = session.createQuery(hql, Contributor.class);
            query.setParameter("minPoints", minPoints);
            query.setParameter("minMeals", (long) minMeals);
            query.setMaxResults(size);

            List<Contributor> contributors = query.getResultList();
            ctx.json(contributors);

            Logger.info("Contributors recognitions served");
        } catch (Exception e) {
            Logger.error("Could not serve contributor recognitions {}", e);
            ctx.status(500);
        }

    }
}
