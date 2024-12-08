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
    private long createdAt;
    @OneToMany
    private List<FailureReportRow> failuresPerFridge;
    @OneToMany
    private List<MealsPerFridgeReportRow> mealsPerFridge;
    @OneToMany
    private List<MealsPerContributorReportRow> mealsPerContributor;

    public Report() {
    }

    public Report(long createdAt, List<FailureReportRow> failuresPerFridge,
            List<MealsPerFridgeReportRow> mealsPerFridgeReport,
            List<MealsPerContributorReportRow> mealsPerContributor) {
        this.createdAt = createdAt;
        this.failuresPerFridge = failuresPerFridge;
        this.mealsPerFridge = mealsPerFridgeReport;
        this.mealsPerContributor = mealsPerContributor;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public List<FailureReportRow> getFailuresPerFridge() {
        return this.failuresPerFridge;
    }

    public List<MealsPerFridgeReportRow> getMealsPerFridge() {
        return this.mealsPerFridge;
    }

    public List<MealsPerContributorReportRow> getMealsPerContributor() {
        return this.mealsPerContributor;
    }

    public Map<String, Object> toMap() {
        String date = DateUtils.epochToDate(createdAt).toString();
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> failuresPerFridgeReports = new ArrayList<>();
        List<Map<String, Object>> mealsPerFridgeReports = new ArrayList<>();
        List<Map<String, Object>> mealsPerContributorReports = new ArrayList<>();

        for (FailureReportRow row : failuresPerFridge) {
            failuresPerFridgeReports.add(row.toMap());
        }
        for (MealsPerFridgeReportRow row : mealsPerFridge) {
            mealsPerFridgeReports.add(row.toMap());
        }
        for (MealsPerContributorReportRow row : mealsPerContributor) {
            mealsPerContributorReports.add(row.toMap());
        }

        map.put("id", id);
        map.put("emoji", "🖨️");
        map.put("created_at", date);
        map.put("failures_per_fridge", failuresPerFridgeReports);
        map.put("failures_per_fridge", mealsPerFridgeReports);
        map.put("failures_per_contributor", mealsPerContributorReports);

        return map;
    }

}
