package org.grupo11.Services.Reporter;

import java.util.List;
import java.util.stream.Collectors;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Services.Contributions.MealDistribution;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.Incident.Incident;
import org.hibernate.Session;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class FridgeReportRow {
    @Id
    @GeneratedValue
    private Long id;
    private int fridgeId;
    private int totalFailures;
    private int failures;
    private int totalAddedMeals;
    private int addedMeals;
    private int totalRemovedMeals;
    private int removedMeals;

    public FridgeReportRow() {
    }

    public FridgeReportRow(Fridge fridge, long fromDate, long toDate) {
        fridgeId = fridge.getId();
        setHowManyFailures(fridge, fromDate, toDate);
        setMealsQuantities(fridge, fromDate, toDate);
    }

    public void setHowManyFailures(Fridge fridge, long fromDate, long toDate) {
        List<Incident> incidents = fridge.getIncidents();

        List<Incident> totalIncidents = incidents.stream()
                .filter(incident -> incident.getDetectedAt() < toDate)
                .collect(Collectors.toList());
        totalFailures = totalIncidents.size();

        List<Incident> filteredIncidents = totalIncidents.stream()
                .filter(incident -> incident.getDetectedAt() >= fromDate)
                .collect(Collectors.toList());
        failures = filteredIncidents.size();
    }

    public void setMealsQuantities(Fridge fridge, long fromDate, long toDate) {

        try (Session session = DB.getSessionFactory().openSession()) {

            int addedMealscounter = 0;
            int removedMealscounter = 0;
            int totalAddedMealscounter = 0;
            int totalRemovedMealscounter = 0;

            // Checking for meal donations
            
            String mealDonationsHql = "SELECT md FROM MealDonation md WHERE md.fridge.id = :fridgeId AND md.date < :toDate";
            org.hibernate.query.Query<MealDonation> mealDonationsQuery = session.createQuery(mealDonationsHql, MealDonation.class);
            mealDonationsQuery.setParameter("fridgeId", fridge.getId());
            mealDonationsQuery.setParameter("toDate", toDate);
            List<MealDonation> mealDonations = mealDonationsQuery.getResultList();

            totalAddedMealscounter = mealDonations.size();

            addedMealscounter = mealDonations.stream()
                    .filter(mealDonation -> mealDonation.getDate() >= fromDate)
                    .collect(Collectors.toList())
                    .size();
            
            // Checking for meal distributions

            String mealDistributionHql = "SELECT md FROM MealDistribution md " +
                    "WHERE (md.originFridge.id = :fridgeId OR md.destinyFridge.id = :fridgeId) AND md.date < :toDate";
            org.hibernate.query.Query<MealDistribution> mealDistributionsQuery = session.createQuery(mealDistributionHql, MealDistribution.class);
            mealDistributionsQuery.setParameter("fridgeId", fridge.getId());
            mealDistributionsQuery.setParameter("toDate", toDate);
            List<MealDistribution> mealDistributions = mealDistributionsQuery.getResultList();

            // Para las de origen se sacan
            
            List<MealDistribution> mdOrigin = mealDistributions.stream()
                    .filter(mealDistribution -> mealDistribution.getOriginFridge().getId() == fridge.getId())
                    .collect(Collectors.toList());

            totalRemovedMealscounter = mdOrigin.size();

            removedMealscounter = mdOrigin.stream()
                    .filter(mealDistribution -> mealDistribution.getDate() >= fromDate)
                    .collect(Collectors.toList())
                    .size();
            
            // Para las de destino se agregan

            List<MealDistribution> mdDestiny = mealDistributions.stream()
                    .filter(mealDistribution -> mealDistribution.getDestinyFridge().getId() == fridge.getId())
                    .collect(Collectors.toList());

            totalAddedMealscounter += mdDestiny.size();

            addedMealscounter += mdDestiny.stream()
                    .filter(mealDistribution -> mealDistribution.getDate() >= fromDate)
                    .collect(Collectors.toList())
                    .size();

            addedMeals = addedMealscounter;
            removedMeals = removedMealscounter;
            totalAddedMeals = totalAddedMealscounter;
            totalRemovedMeals = totalRemovedMealscounter;

        } catch (Exception e) {
            Logger.error("Could not get last report", e);
        }
    }

    public long getId() {
        return id;
    }

    public int getFridgeId() {
        return fridgeId;
    }

    public int getTotalFailures() {
        return totalFailures;
    }

    public int getFailures() {
        return failures;
    }

    public int getTotalAddedMeals() {
        return totalAddedMeals;
    }

    public int getAddedMeals() {
        return addedMeals;
    }

    public int getTotalRemovedMeals() {
        return totalRemovedMeals;
    }

    public int getRemovedMeals() {
        return removedMeals;
    }
}
