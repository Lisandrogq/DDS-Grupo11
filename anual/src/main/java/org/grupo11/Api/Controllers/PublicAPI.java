package org.grupo11.Api.Controllers;

import java.util.List;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.ApiResponse;
import org.grupo11.Services.Contributor.Contributor;
import org.hibernate.Session;
import org.hibernate.query.Query;

import io.javalin.http.Context;

public class PublicAPI {

    public static void handleContributorRecognitions(Context ctx) {
        String minPointsParam = ctx.queryParam("min_points");
        String minMealsParam = ctx.queryParam("min_meals");
        String sizeParam = ctx.queryParam("size");
        final int DEFAULT_SIZE = 10;

        int minPoints;
        int minMeals;
        int size;

        // validate input
        if (minPointsParam == null) {
            ctx.status(400).json(new ApiResponse(400, "Missing required parameter: minPoints", null));
            return;
        }

        if (minMealsParam == null) {
            ctx.status(400).json(new ApiResponse(400, "Missing required parameter: minMeals", null));
            return;
        }

        try {
            minPoints = Integer.parseInt(minPointsParam);
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ApiResponse(400, "Invalid input: minPoints must be an integer.", null));
            return;
        }

        try {
            minMeals = Integer.parseInt(minMealsParam);
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ApiResponse(400, "Invalid input: minMeals must be an integer.", null));
            return;
        }

        try {
            size = sizeParam != null ? Integer.parseInt(sizeParam) : DEFAULT_SIZE;
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ApiResponse(400, "Invalid input: size must be an integer.", null));
            return;
        }

        // Run query
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
            ctx.json(new ApiResponse(200, contributors));

            Logger.info("Contributors recognitions served");
        } catch (Exception e) {
            Logger.error("Could not serve contributor recognitions {}", e);
            ctx.status(500).json(new ApiResponse(500));
        }

    }
}
