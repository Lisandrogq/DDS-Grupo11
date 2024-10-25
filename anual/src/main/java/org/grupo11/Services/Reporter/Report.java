package org.grupo11.Services.Reporter;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Report {
    @Id
    @GeneratedValue
    private Long id;
    private long createdAt;
    private List<FailureReportRow> failuresPerFridge;
    private List<MealsPerFridgeReportRow> mealsPerFridgeReport;
    private List<MealPerContributorReportRow> mealsPerContributor;

    public Report() {
    }

    public Report(long createdAt, List<FailureReportRow> failuresPerFridge,
            List<MealsPerFridgeReportRow> mealsPerFridgeReport,
            List<MealPerContributorReportRow> mealsPerContributor) {
        this.createdAt = createdAt;
        this.failuresPerFridge = failuresPerFridge;
        this.mealsPerFridgeReport = mealsPerFridgeReport;
        this.mealsPerContributor = mealsPerContributor;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public List<FailureReportRow> getFailuresPerFridge() {
        return this.failuresPerFridge;
    }

    public List<MealsPerFridgeReportRow> getMealsPerFridgeReport() {
        return this.mealsPerFridgeReport;
    }


    public  List<MealPerContributorReportRow> getMealsPerContributor() {
        return this.mealsPerContributor;
    }

}
