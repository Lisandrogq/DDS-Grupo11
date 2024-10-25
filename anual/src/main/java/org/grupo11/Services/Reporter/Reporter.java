package org.grupo11.Services.Reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.grupo11.Services.Contributions.ContributionType;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Contributor.ContributorsManager;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgesManager;
import org.grupo11.Services.Fridge.Incident.Incident;
import org.grupo11.Utils.DateUtils;

public class Reporter {
    private List<Report> reports;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    // unit is in days
    private Integer genReportsEvery = 7;

    private static Reporter instance = null;

    private Reporter() {
        this.reports = new ArrayList<>();
    }

    public static synchronized Reporter getInstance() {
        if (instance == null)
            instance = new Reporter();

        return instance;
    }

    public void genReports() {
        Report report = new Report(DateUtils.getCurrentTimeInMs(), getFailuresPerFridgeReport(),
                getMealsPerFridgeReport(), getMealPerCollaboratorReport());
        this.reports.add(report);
    }

    public List<FailureReportRow> getFailuresPerFridgeReport() {
        List<FailureReportRow> failureReport = new ArrayList<FailureReportRow>();
        List<Fridge> fridges = FridgesManager.getInstance().getFridges();
        for (Fridge fridge : fridges) {
            // Filter incidents that have been detected more than a week ago
            long oneWeekAgoMs = DateUtils.getAWeekAgoFrom(DateUtils.getCurrentTimeInMs());
            List<Incident> incidents = fridge.getIncidents().stream()
                    .filter(incident -> incident.getDetectedAt() < oneWeekAgoMs)
                    .collect(Collectors.toList());
            FailureReportRow row = new FailureReportRow(fridge, incidents);
            failureReport.add(row);
        }
        return failureReport;

    }

    public List<MealsPerFridgeReportRow> getMealsPerFridgeReport() {
        List<MealsPerFridgeReportRow> mealsPerFridgeReport = new ArrayList<MealsPerFridgeReportRow>();

        List<Fridge> fridges = FridgesManager.getInstance().getFridges();
        for (Fridge fridge : fridges) {
            MealsPerFridgeReportRow row = new MealsPerFridgeReportRow(fridge, fridge.getAddedMeals(),
                    fridge.getRemovedMeals());
            mealsPerFridgeReport.add(row);
        }
        return mealsPerFridgeReport;
    }

    public List<MealPerContributorReportRow> getMealPerCollaboratorReport() {
        long oneWeekAgoMs = DateUtils.getAWeekAgoFrom(DateUtils.getCurrentTimeInMs());
        // get the meals contribution from a week ago
        List<MealPerContributorReportRow> mealPerContributorReport = new ArrayList<MealPerContributorReportRow>();

        List<Contributor> contributors = ContributorsManager.getInstance()
                .getContributors();
        for (Contributor contributor : contributors) {
            List<MealDonation> mealDonations = contributor.getContributions().stream()
                    .filter(contribution -> (contribution.getContributionType() == ContributionType.MEAL_DONATION
                            && contribution.getDate() < oneWeekAgoMs))
                    .map((contribution) -> (MealDonation) contribution)
                    .collect(Collectors.toList());
            MealPerContributorReportRow row = new MealPerContributorReportRow(contributor, mealDonations);
            mealPerContributorReport.add(row);
        }
        return mealPerContributorReport;
    }

    public void setupReporter() {
        Runnable task = () -> this.genReports();
        scheduler.scheduleAtFixedRate(task, 0, genReportsEvery, TimeUnit.DAYS);
    }

    public List<Report> getReports() {
        return this.reports;
    }

}
