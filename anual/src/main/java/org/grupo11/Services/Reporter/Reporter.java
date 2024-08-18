package org.grupo11.Services.Reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.ContributionType;
import org.grupo11.Services.Contributions.ContributionsManager;
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
        Report report = new Report(DateUtils.getCurrentTimeInMs(), getFailuresPerFridgeStatistic(),
                getMealsInOutFridgeStatistic(), getMealPerCollaboratorStatistic());
        this.reports.add(report);
    }

    public float getFailuresPerFridgeStatistic() {
        List<Fridge> fridges = FridgesManager.getInstance().getFridges();
        int numberOfIncidentsThisWeek = 0;
        for (Fridge fridge : fridges) {
            // Filter incidents that have been detected more than a week ago
            long oneWeekAgoMs = DateUtils.getAWeekAgoFrom(DateUtils.getCurrentTimeInMs());
            List<Incident> incidents = fridge.getIncidents().stream()
                    .filter(incident -> incident.getDetectedAt() < oneWeekAgoMs)
                    .collect(Collectors.toList());
            numberOfIncidentsThisWeek += incidents.size();
        }
        return numberOfIncidentsThisWeek / fridges.size();

    }

    public float getMealsInOutFridgeStatistic() { //CREO QUE EL REPORTE TIENE QUE SER INDIVIDUAL POR CADA HELADERA, NO UN PROMEDIO
        int totalMeals = FridgesManager.getInstance().getFridges().stream()
                .mapToInt(fridge -> fridge.getMeals().size())
                .sum();

        return totalMeals / FridgesManager.getInstance().getFridges().size();
    }

    public float getMealPerCollaboratorStatistic() {
        long oneWeekAgoMs = DateUtils.getAWeekAgoFrom(DateUtils.getCurrentTimeInMs());
        // get the meals contribution from a week ago
        List<Contribution> mealContributions = ContributionsManager.getInstance()
                .getAllByType(ContributionType.MEAL_DONATION).stream()
                .filter(contribution -> contribution.getDate() < oneWeekAgoMs)
                .collect(Collectors.toList());

        return mealContributions.size() / ContributorsManager.getInstance().getContributors().size();

    }

    public void setupReporter() {
        Runnable task = () -> this.genReports();
        scheduler.scheduleAtFixedRate(task, 0, genReportsEvery, TimeUnit.DAYS);
    }

    public List<Report> getReports() {
        return this.reports;
    }

}
