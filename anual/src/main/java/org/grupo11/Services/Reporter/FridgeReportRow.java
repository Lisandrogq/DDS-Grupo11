package org.grupo11.Services.Reporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.grupo11.DB;
import org.grupo11.Logger;
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

    public FridgeReportRow(Fridge fridge, long lastReport) {
        fridgeId = fridge.getId();
        setHowManyFailures(fridge, lastReport);
        totalAddedMeals = fridge.getAddedMeals();
        totalRemovedMeals = fridge.getRemovedMeals();
        setMealsQuantities(fridge, lastReport);
    }

    public void setHowManyFailures(Fridge fridge, long lastReport) {
        List<Incident> incidents = fridge.getIncidents();
        totalFailures = incidents.size();
        List<Incident> incidentsFiltered = incidents.stream()
                .filter(incident -> incident.getDetectedAt() > lastReport)
                .collect(Collectors.toList());
        failures = incidentsFiltered.size();
    }

    public void setMealsQuantities(Fridge fridge, long lastReport) {
        int fridgeId = fridge.getId();
        
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT r FROM Report r WHERE r.createdAt = :lastReport";
            org.hibernate.query.Query<Report> origin_query = session.createQuery(hql, Report.class);
            origin_query.setParameter("lastReport", lastReport);
            List<Report> reports = origin_query.getResultList();

            if (reports.size() == 0) {
                addedMeals = fridge.getAddedMeals();
                removedMeals = fridge.getRemovedMeals();
            } else if (reports.size() > 1) {
                Logger.error("More than one report with the same creation date");
            } else {
                Report report = reports.get(0);
                List<FridgeReportRow> fridges = report.getFridgeReportRows();
                FridgeReportRow fridgeReportRow = fridges.stream()
                        .filter(row -> row.fridgeId == fridgeId)
                        .findFirst()
                        .orElse(null);
                if (fridgeReportRow == null) {
                    addedMeals = fridge.getAddedMeals();
                    removedMeals = fridge.getRemovedMeals();
                } else {
                    addedMeals = fridge.getAddedMeals() - fridgeReportRow.totalAddedMeals;
                    removedMeals = fridge.getRemovedMeals() - fridgeReportRow.totalRemovedMeals;
                }
            }
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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", Long.toString(id));
        map.put("fridgeId", Integer.toString(fridgeId));
        map.put("failures", Integer.toString(failures));
        map.put("totalFailures", Integer.toString(totalFailures));
        map.put("addedMeals", Integer.toString(addedMeals));
        map.put("totalAddedMeals", Integer.toString(totalAddedMeals));
        map.put("removedMeals", Integer.toString(removedMeals));
        map.put("totalRemovedMeals", Integer.toString(totalRemovedMeals));
        return map;
    }

}
