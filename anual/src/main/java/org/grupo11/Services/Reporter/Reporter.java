package org.grupo11.Services.Reporter;

import java.util.ArrayList;
import java.util.Arrays;
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
        
        while (lastReport <= ( DateUtils.now() - intervalMillis() )) {
            Logger.info("Entro al while");
            long fromDate = lastReport;
            long toDate = lastReport + intervalMillis();
            genReport(fromDate, toDate);
        }

        Runnable task = () -> this.genReport(lastReport, lastReport + intervalMillis());
        long initialDelay = intervalMillis() - (DateUtils.now() - lastReport);
        Logger.info("Initial delay: " + initialDelay);
        initialDelay = (initialDelay < 0) ? 0 : initialDelay;
        scheduler.scheduleAtFixedRate(task, initialDelay, intervalMillis(), TimeUnit.MILLISECONDS);
    }

    public void updateLastReport() {
        try (Session session = DB.getSessionFactory().openSession()) {
            
            String hql = "SELECT MAX(r.toDate) FROM Report r";
            Long lastReportDate = session.createQuery(hql, Long.class).uniqueResult();

            Logger.info("Last report date: " + lastReportDate);
    
            lastReport = (lastReportDate != null) ? lastReportDate : getFirstDataDate();
            Logger.info("Last report: " + lastReport);
        } catch (Exception e) {
            Logger.error("Could not update last report", e);
            lastReport = getFirstDataDate();
        }
    }

    public long getFirstDataDate() {
        try (Session session = DB.getSessionFactory().openSession()) {

            String incidentHql = "SELECT MIN(i.detectedAt) FROM Incident i";
            Long firstIncidentDate = session.createQuery(incidentHql, Long.class).uniqueResult();
            Logger.info("First incident date: " + firstIncidentDate);
    
            String mealDonationHql = "SELECT MIN(md.date) FROM MealDonation md";
            Long firstContributionDate = session.createQuery(mealDonationHql, Long.class).uniqueResult();
            Logger.info("First contribution date: " + firstContributionDate);
    
            long now = DateUtils.now();
            long earliestIncident = (firstIncidentDate != null) ? firstIncidentDate : now;
            long earliestContribution = (firstContributionDate != null) ? firstContributionDate : now;
    
            return Math.min(earliestIncident, earliestContribution);
        } catch (Exception e) {
            Logger.error("Could not get first data date", e);
            return DateUtils.now();
        }
    }

    public void genReport(long fromDate, long toDate) {

        Logger.info("Generating report from " + DateUtils.epochToDate(fromDate) + " to " + DateUtils.epochToDate(toDate));

        try (Session session = DB.getSessionFactory().openSession()) {
            
            String hql = "FROM Fridge";
            List<Fridge> fridges = session.createQuery(hql, Fridge.class).getResultList();
            List<FridgeReportRow> fridgeReportRows = new ArrayList<FridgeReportRow>(); // 1
            for (Fridge fridge : fridges) {
                FridgeReportRow row = new FridgeReportRow(fridge, fromDate, toDate);
                DB.create(row);
                fridgeReportRows.add(row);
            }

            String hql2 = "FROM Individual";
            List<Individual> contributors = session.createQuery(hql2, Individual.class).getResultList();
            List<MealsPerContributorReportRow> contributorReportRows = new ArrayList<MealsPerContributorReportRow>(); // 2
            for (Individual contributor : contributors) {
                MealsPerContributorReportRow row = new MealsPerContributorReportRow(contributor, fromDate, toDate);
                DB.create(row);
                contributorReportRows.add(row);
            }

            Report report = new Report(fromDate, toDate, fridgeReportRows, contributorReportRows);
            DB.create(report);
            this.reports.add(report);
            lastReport = toDate;

        } catch (Exception e) {
            Logger.error("Could not create report", e);
        }
    }

    public void newReportInterval(int frequency, String unit) {

        if (frequency <= 0) {
            throw new IllegalArgumentException("Frequency must be greater than 0.");
        }
        if (!Arrays.asList("MINUTES", "HOURS", "DAYS", "WEEKS").contains(unit)) {
            throw new IllegalArgumentException("Invalid unit. Allowed values are: MINUTES, HOURS, DAYS, WEEKS.");
        }

        try {
            scheduler.shutdown();
            scheduler = Executors.newScheduledThreadPool(1);

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

            cleanReports();
            setupReporter();

        } catch (Exception e) {
            Logger.error("Could not change report interval", e);
            return;
        }
    }

    public void cleanReports() {
        try (Session session = DB.getSessionFactory().openSession()) {
            session.beginTransaction();

            String hql = "DELETE FROM Report";
            session.createQuery(hql).executeUpdate();
            
            session.getTransaction().commit();
        } catch (Exception e) {
            Logger.error("Could not delete reports", e);
        }
    }

    public void regenerateReports() {
        newReportInterval(genReportsEvery, genReportsEveryUnit.toString());
    }

    public long intervalMillis() {
        return genReportsEveryUnit.toMillis(genReportsEvery);
    }

    public String getGenReportsEvery() {
        return genReportsEvery.toString();
    }

    public String getGenReportsEveryUnit() {
        return genReportsEveryUnit.toString();
    }
}

