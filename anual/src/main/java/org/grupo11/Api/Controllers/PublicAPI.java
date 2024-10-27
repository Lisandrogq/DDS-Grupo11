package org.grupo11.Api.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.ApiResponse;
import org.grupo11.DTOS.IndividualDTO;
import org.grupo11.DTOS.LegalEntityDTO;
import org.grupo11.Services.Contributor.Individual;
import org.grupo11.Services.Contributor.LegalEntity.LegalEntity;
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

        List<Individual> individuals;
        List<LegalEntity> legalEntities;

        // TODO(marcos): we should move this to the managers
        // get individuals
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT ind " +
                    "FROM Contribution c " +
                    "JOIN c.contributor contr " +
                    "JOIN Individual ind ON ind.id = contr.id " +
                    "JOIN MealDonation md ON c.id = md.id " +
                    "WHERE contr.points >= :minPoints " +
                    "GROUP BY ind " +
                    "HAVING COUNT(md) >= :minMeals";

            Query<Individual> query = session.createQuery(hql, Individual.class);
            query.setParameter("minPoints", minPoints);
            query.setParameter("minMeals", minMeals);
            query.setMaxResults(size);

            individuals = query.getResultList();

            Logger.info("Contributors recognitions served");
        } catch (Exception e) {
            Logger.error("Could not serve contributor recognitions {}", e);
            ctx.status(500).json(new ApiResponse(500));
            return;
        }

        // get legal entities
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT le " +
                    "FROM Contribution c " +
                    "JOIN c.contributor contr " +
                    "JOIN LegalEntity le ON le.id = le.id " +
                    "JOIN MealDonation md ON c.id = md.id " +
                    "WHERE contr.points >= :minPoints " +
                    "GROUP BY le " +
                    "HAVING COUNT(md) >= :minMeals";

            Query<LegalEntity> query = session.createQuery(hql, LegalEntity.class);
            query.setParameter("minPoints", minPoints);
            query.setParameter("minMeals", minMeals);
            query.setMaxResults(size);

            legalEntities = query.getResultList();

            Logger.info("Contributors recognitions served");
        } catch (Exception e) {
            Logger.error("Could not serve contributor recognitions {}", e);
            ctx.status(500).json(new ApiResponse(500));
            return;
        }

        // TODO(marcos): this should be moved to another file as well, maybe we could
        // have a DTO mapping layer, given the amount of code it takes in java
        class ResponseData {
            public List<IndividualDTO> individuals;
            public List<LegalEntityDTO> legalEntities;

            public ResponseData(List<Individual> individuals, List<LegalEntity> legalEntities) {
                this.individuals = mapIndividualsToDTO(individuals);
                this.legalEntities = mapLegalEntitiesToDTO(legalEntities);
            }

            List<IndividualDTO> mapIndividualsToDTO(List<Individual> individuals) {
                return individuals.stream()
                        .map(individual -> new IndividualDTO(
                                individual.getName(),
                                individual.getSurname(),
                                individual.getAddress(),
                                individual.getBirth(),
                                individual.getDocument(),
                                individual.getDocumentType(),
                                individual.getPoints()))
                        .collect(Collectors.toList());
            }

            List<LegalEntityDTO> mapLegalEntitiesToDTO(List<LegalEntity> legalEntities) {
                return legalEntities.stream()
                        .map(legalEntity -> new LegalEntityDTO(
                                legalEntity.getType(),
                                legalEntity.getCategory(),
                                legalEntity.getPoints()))
                        .collect(Collectors.toList());
            }
        }

        ctx.json(new ApiResponse(200, new ResponseData(individuals, legalEntities)));

    }
}
