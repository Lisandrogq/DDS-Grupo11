package org.grupo11.Api.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.ApiResponse;
import org.grupo11.DTOS.IndividualDTO;
import org.grupo11.Services.Contributor.Individual;
import org.hibernate.Session;
import org.hibernate.query.Query;

import io.javalin.http.Context;

public class PublicAPI {

    public static void renderFilterContributors(Context ctx) {
        ctx.render("templates/publicApi/paramsForm.html");
    }

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
            ctx.status(400).json(new ApiResponse(400, "Missing required parameter: min_points", null));
            return;
        }

        if (minMealsParam == null) {
            ctx.status(400).json(new ApiResponse(400, "Missing required parameter: min_meals", null));
            return;
        }

        try {
            minPoints = Integer.parseInt(minPointsParam);
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ApiResponse(400, "Invalid input: min_points must be an integer.", null));
            return;
        }

        try {
            minMeals = Integer.parseInt(minMealsParam);
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ApiResponse(400, "Invalid input: min_meals must be an integer.", null));
            return;
        }
 
        try { 
            if (sizeParam == null || sizeParam.isEmpty()) {
                size = DEFAULT_SIZE;
            } else {
                size = Integer.parseInt(sizeParam);
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ApiResponse(400, "Invalid input: size must be an integer.", null));
            return;
        }

        if (minPoints < 0) {
            ctx.status(400).json(new ApiResponse(400, "Invalid input: min_points must be a positive integer.", null));
            return;
        }

        if (minMeals < 0) {
            ctx.status(400).json(new ApiResponse(400, "Invalid input: min_meals must be a positive integer.", null));
            return;
        }

        if (size < 0) {
            ctx.status(400).json(new ApiResponse(400, "Invalid input: size must be a positive integer.", null));
            return;
        }

        List<IndividualDTO> individuals = new ArrayList<>();

        try (Session session = DB.getSessionFactory().openSession()) {

            String hql = "SELECT i, COUNT(md) " +
                         "FROM Individual i " +
                         "JOIN MealDonation md ON i.id = md.contributor.id " +
                         "WHERE i.points >= :minPoints " +
                         "GROUP BY i " +
                         "HAVING COUNT(md) >= :minMeals " +
                         "ORDER BY i.points DESC";

            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("minPoints", minPoints);
            query.setParameter("minMeals", minMeals);
            query.setMaxResults(size);

            List<Object[]> results = query.list();

            for (Object[] result : results) {
                Individual individual = (Individual) result[0];
                Long mealsDonated = (Long) result[1];

                IndividualDTO individualDTO = new IndividualDTO(
                        individual.getName(), individual.getSurname(),
                        individual.getAddress(), individual.getBirth(),
                        individual.getDocument(), individual.getDocumentType(),
                        Math.floor(individual.getPoints()), mealsDonated.intValue()
                );
                individuals.add(individualDTO);
            }

        } catch (Exception e) {
            Logger.error("Could not serve contributor recognitions {}", e);
            ctx.status(500).json(new ApiResponse(500));
            return;
        }

        ctx.json(new ApiResponse(200, individuals));
        Logger.info("Contributors recognitions served");

    }
}
