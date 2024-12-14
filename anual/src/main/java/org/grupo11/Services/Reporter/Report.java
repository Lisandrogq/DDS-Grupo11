package org.grupo11.Services.Reporter;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private long fromDate;
    private long toDate;
    @OneToMany
    private List<FridgeReportRow> fridgeReportRows = new ArrayList<>();
    @OneToMany
    private List<MealsPerContributorReportRow> contributorReportRows = new ArrayList<>();

    public Report() {
    }

    public Report(long fromDate, long toDate,
            List<FridgeReportRow> fridgeReportRows,
            List<MealsPerContributorReportRow> contributorReportRows) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fridgeReportRows = fridgeReportRows;
        this.contributorReportRows = contributorReportRows;
    }

    public long getId() {
        return id;
    }

    public long getFromDate() {
        return fromDate;
    }

    public long getToDate() {
        return toDate;
    }

    public List<FridgeReportRow> getFridgeReportRows() {
        return fridgeReportRows;
    }

    public List<MealsPerContributorReportRow> getContributorReportRows() {
        return contributorReportRows;
    }
}
