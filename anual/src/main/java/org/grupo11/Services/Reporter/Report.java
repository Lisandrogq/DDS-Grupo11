package org.grupo11.Services.Reporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.grupo11.Utils.DateUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Report {
    @Id
    @GeneratedValue
    private Long id;
    private long lastCreatedAt;
    private long createdAt; // Time
    @OneToMany
    private List<FridgeReportRow> fridgeReportRows;
    @OneToMany
    private List<MealsPerContributorReportRow> contributorReportRows;

    public Report() {
    }

    public Report(long createdAt, long lastCreatedAt,
                List<FridgeReportRow> fridgeReportRows,
                List<MealsPerContributorReportRow> contributorReportRows) {
        this.createdAt = createdAt;
        this.lastCreatedAt = lastCreatedAt;
        this.fridgeReportRows = fridgeReportRows;
        this.contributorReportRows = contributorReportRows;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public List<FridgeReportRow> getFridgeReportRows() {
        return fridgeReportRows;
    }

    public Map<String, Object> toMap() {
        String date = DateUtils.epochToDate(createdAt).toString();
        String lastDate = DateUtils.epochToDate(lastCreatedAt).toString();
        Map<String, Object> map = new HashMap<>();

        map.put("id", Long.toString(id));
        map.put("emoji", "🖨️");
        map.put("created_at", date);
        map.put("last_created_at", lastDate);
        /*
        List<Map<String, Object>> failuresPerFridgeReports = new ArrayList<>();
        List<Map<String, Object>> mealsPerFridgeReports = new ArrayList<>();
        List<Map<String, Object>> mealsPerContributorReports = new ArrayList<>();

        for (FridgeReportRow row : failuresPerFridge) {
            failuresPerFridgeReports.add(row.toMap());
        }
        for (MealsPerFridgeReportRow row : mealsPerFridge) {
            mealsPerFridgeReports.add(row.toMap());
        }
        for (MealsPerContributorReportRow row : mealsPerContributor) {
            mealsPerContributorReports.add(row.toMap());
        }
        map.put("failures_per_fridge", failuresPerFridgeReports);
        map.put("failures_per_fridge", mealsPerFridgeReports);
        map.put("failures_per_contributor", mealsPerContributorReports);
        */

        return map;
    }

}
