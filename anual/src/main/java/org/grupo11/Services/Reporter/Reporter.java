package org.grupo11.Services.Reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Services.Contributor.Individual;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Utils.DateUtils;
import org.hibernate.Session;

public class Reporter {
    private List<Report> reports;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Integer genReportsEvery = 7;
    private TimeUnit genReportsEveryUnit = TimeUnit.DAYS;
    private static Reporter instance = null;
    private long lastReport = 0;

    private Reporter() {
        this.reports = new ArrayList<>();
    }

    public static synchronized Reporter getInstance() {
        if (instance == null)
            instance = new Reporter();
        return instance;
    }

    public void setupReporter() {
        updateLastReport();
        Runnable task = () -> this.genReport();
        scheduler.scheduleAtFixedRate(task, genReportsEvery, genReportsEvery, genReportsEveryUnit);
    }

    public void updateLastReport() {
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT r FROM Report r ORDER BY r.createdAt DESC";
            org.hibernate.query.Query<Report> origin_query = session.createQuery(hql, Report.class);
            List<Report> reports = origin_query.getResultList();
            if (reports.size() == 0) {
                lastReport = DateUtils.now();
            } else {
                lastReport = reports.get(0).getCreatedAt();
            }
        } catch (Exception e) {
            Logger.error("Could not get last report", e);
        }
    }

    public void genReport() {
        Logger.info("Generating reports");

        try (Session session = DB.getSessionFactory().openSession()) {
            
            String hql = "FROM Fridge";
            List<Fridge> fridges = session.createQuery(hql, Fridge.class).getResultList();
            List<FridgeReportRow> fridgeReportRows = new ArrayList<FridgeReportRow>(); // 1
            for (Fridge fridge : fridges) {
                FridgeReportRow row = new FridgeReportRow(fridge, lastReport);
                DB.create(row);
                fridgeReportRows.add(row);
            }

            String hql2 = "FROM Individual";
            List<Individual> contributors = session.createQuery(hql2, Individual.class).getResultList();
            List<MealsPerContributorReportRow> contributorReportRows = new ArrayList<MealsPerContributorReportRow>(); // 2
            for (Individual contributor : contributors) {
                MealsPerContributorReportRow row = new MealsPerContributorReportRow(contributor, lastReport);
                DB.create(row);
                contributorReportRows.add(row);
            }

            Long now = DateUtils.now();
            Report report = new Report(now, lastReport, fridgeReportRows, contributorReportRows);
            DB.create(report);
            this.reports.add(report);
            lastReport = now;
            session.close();

        } catch (Exception e) {
            Logger.error("Could not create report", e);
        }
    }

    public void newReportInterval(int frequency, String unit) {
        switch (unit) {
            case "MINUTES":
                this.genReportsEveryUnit = TimeUnit.MINUTES;
                break;
            case "HOURS":
                this.genReportsEveryUnit = TimeUnit.HOURS;
                break;
            case "DAYS":
                this.genReportsEveryUnit = TimeUnit.DAYS;
                break;
            case "WEEKS":
                this.genReportsEveryUnit = TimeUnit.DAYS;
                frequency = frequency * 7;
                break;
            default:
                this.genReportsEveryUnit = TimeUnit.DAYS;
                break;
        }
        this.genReportsEvery = frequency;
        scheduler.shutdown();
        scheduler = Executors.newScheduledThreadPool(1);
        genReport();
        setupReporter();
    }

    public String getGenReportsEvery() {
        return genReportsEvery.toString();
    }

    public String getGenReportsEveryUnit() {
        return genReportsEveryUnit.toString();
    }
}

