package org.grupo11.Api.Controllers;

import java.util.ArrayList;
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
import jakarta.persistence.Tuple;

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
            size = sizeParam != null ? Integer.parseInt(sizeParam) : DEFAULT_SIZE;
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ApiResponse(400, "Invalid input: size must be an integer.", null));
            return;
        }

        List<IndividualDTO> individuals = new ArrayList<>();
        List<LegalEntityDTO> legalEntities = new ArrayList<>();

        // TODO(marcos): we should move this to the managers
        // get individuals
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT ind, COUNT(md) " +
                    "FROM Contribution c " +
                    "JOIN c.contributor contr " +
                    "JOIN Individual ind ON ind.id = contr.id " +
                    "JOIN MealDonation md ON c.id = md.id " +
                    "WHERE contr.points >= :minPoints " +
                    "GROUP BY ind " +
                    "HAVING COUNT(md) >= :minMeals";

            Query<Tuple> query = session.createQuery(hql, Tuple.class);
            query.setParameter("minPoints", minPoints);
            query.setParameter("minMeals", minMeals);
            query.setMaxResults(size);

            List<Tuple> results = query.getResultList();

            for (Tuple tuple : results) {
                Individual individual = tuple.get(0, Individual.class);
                Long mealDonationCount = tuple.get(1, Long.class); // Use the alias defined in HQL
                IndividualDTO individualDTO = new IndividualDTO(
                        individual.getName(),
                        individual.getSurname(),
                        individual.getAddress(),
                        individual.getBirth(),
                        individual.getDocument(),
                        individual.getDocumentType(),
                        individual.getPoints(), mealDonationCount.intValue());
                individuals.add(individualDTO);
            }

        } catch (Exception e) {
            Logger.error("Could not serve contributor recognitions {}", e);
            ctx.status(500).json(new ApiResponse(500));
            return;
        }

        // get legal entities
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT le, COUNT(md) " +
                    "FROM Contribution c " +
                    "JOIN c.contributor contr " +
                    "JOIN LegalEntity le ON le.id = contr.id " +
                    "JOIN MealDonation md ON c.id = md.id " +
                    "WHERE contr.points >= :minPoints " +
                    "GROUP BY le " +
                    "HAVING COUNT(md) >= :minMeals";

            Query<Tuple> query = session.createQuery(hql, Tuple.class);
            query.setParameter("minPoints", minPoints);
            query.setParameter("minMeals", minMeals);
            query.setMaxResults(size);

            List<Tuple> results = query.getResultList();

            for (Tuple tuple : results) {
                LegalEntity legalEntity = tuple.get(0, LegalEntity.class);
                Long mealDonationCount = tuple.get(1, Long.class); // Use the alias defined in HQL
                LegalEntityDTO legalEntityDTO = new LegalEntityDTO(
                        legalEntity.getType(),
                        legalEntity.getCategory(),
                        legalEntity.getPoints(), mealDonationCount.intValue());
                legalEntities.add(legalEntityDTO);
            }
        } catch (Exception e) {
            Logger.error("Could not serve contributor recognitions {}", e);
            ctx.status(500).json(new ApiResponse(500));
            return;
        }

        // TODO(marcos): this should be moved to another file as well, maybe we could
        // have a DTO mapping layer, given the amount of code it takes in java
        class ResponseData {
            // public List<IndividualDTO> individuals;
            // public List<LegalEntityDTO> legalEntities;
            @SuppressWarnings("unused")
            public List<Object> recommendedContributors = new ArrayList<>();

            public ResponseData(List<IndividualDTO> individuals, List<LegalEntityDTO> legalEntities) {
                List<RankedContributor> combinedList = new ArrayList<>();
                
                combinedList.addAll(individuals.stream()
                        .map(individualDTO -> new RankedContributor(individualDTO.points, individualDTO))
                        .collect(Collectors.toList()));
                
                combinedList.addAll(legalEntities.stream()
                        .map(legalEntityDTO -> new RankedContributor(legalEntityDTO.points, legalEntityDTO))
                        .collect(Collectors.toList()));
                
                combinedList.sort((c1, c2) -> Double.compare(c2.getPoints(), c1.getPoints()));

                if (combinedList.size() > size) {
                    combinedList = combinedList.subList(0, size);
                }

                recommendedContributors = combinedList.stream().map(RankedContributor::getContributor).collect(Collectors.toList());
            }
        }

        ctx.json(new ApiResponse(200, new ResponseData(individuals, legalEntities)));
        Logger.info("Contributors recognitions served");

    }

    static class RankedContributor {
        private double points;
        private Object contributor;

        public RankedContributor(double points, Object contributor) {
            this.points = points;
            this.contributor = contributor;
        }

        public double getPoints() {
            return points;
        }

        public Object getContributor() {
            return contributor;
        }
    }
}
