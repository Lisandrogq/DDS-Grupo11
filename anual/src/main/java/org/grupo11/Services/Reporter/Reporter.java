package org.grupo11.Services.Reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Services.Contributions.ContributionType;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Contributor.ContributorsManager;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgesManager;
import org.grupo11.Services.Fridge.Incident.Incident;
import org.grupo11.Utils.DateUtils;
import org.hibernate.Session;

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
        try (Session session = DB.getSessionFactory().openSession()) {
            List<Fridge> fridges = FridgesManager.getInstance().getFridges();
            Report report = new Report(DateUtils.getCurrentTimeInMs(), getFailuresPerFridgeReport(fridges),
                    getMealsPerFridgeReport(fridges), getMealPerCollaboratorReport());
            DB.create(report);
        } catch (Exception e) {
            Logger.error("Could not create report", e);
        }
    }

    public List<FailureReportRow> getFailuresPerFridgeReport(List<Fridge> fridges) {
        fridges.getFirst().getIncidents();
        List<FailureReportRow> failureReport = new ArrayList<FailureReportRow>();
        for (Fridge fridge : fridges) {
            // Filter incidents that have been detected more than a week ago
            long oneWeekAgoMs = DateUtils.getAWeekAgoFrom(DateUtils.getCurrentTimeInMs());
            List<Incident> incidents = fridge.getIncidents().stream()
                    .filter(incident -> incident.getDetectedAt() < oneWeekAgoMs)
                    .collect(Collectors.toList());
            FailureReportRow row = new FailureReportRow(fridge, incidents);
            DB.create(row);
            failureReport.add(row);
        }
        return failureReport;
    }

    List<MealsPerFridgeReportRow> getMealsPerFridgeReport(List<Fridge> fridges) {
        List<MealsPerFridgeReportRow> mealsPerFridgeReport = new ArrayList<MealsPerFridgeReportRow>();
        for (Fridge fridge : fridges) {
            MealsPerFridgeReportRow row = new MealsPerFridgeReportRow(fridge, fridge.getAddedMeals(),
                    fridge.getRemovedMeals());
            mealsPerFridgeReport.add(row);
            DB.create(row);
        }
        return mealsPerFridgeReport;
    }

    List<MealsPerContributorReportRow> getMealPerCollaboratorReport() {
        long oneWeekAgoMs = DateUtils.getAWeekAgoFrom(DateUtils.getCurrentTimeInMs());
        // get the meals contribution from a week ago
        List<MealsPerContributorReportRow> mealPerContributorReport = new ArrayList<MealsPerContributorReportRow>();
        List<Contributor> contributors = ContributorsManager.getInstance()
                .getContributors();

        for (Contributor contributor : contributors) {
            List<MealDonation> mealDonations = contributor.getContributions().stream()
                    .filter(contribution -> (contribution.getContributionType() == ContributionType.MEAL_DONATION
                            && contribution.getDate() < oneWeekAgoMs))
                    .map((contribution) -> (MealDonation) contribution)
                    .collect(Collectors.toList());
            Logger.info("meal donations {}", mealDonations.size());
            MealsPerContributorReportRow row = new MealsPerContributorReportRow(contributor, mealDonations);
            mealPerContributorReport.add(row);
            DB.create(row);
        }
        return mealPerContributorReport;
    }

    public void setupReporter() {
        Runnable task = () -> this.genReports();
        scheduler.scheduleAtFixedRate(task, 0, genReportsEvery, TimeUnit.SECONDS);
    }

    public List<Report> getReports() {
        return this.reports;
    }

}
