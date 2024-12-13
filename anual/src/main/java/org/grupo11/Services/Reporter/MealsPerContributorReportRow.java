package org.grupo11.Services.Reporter;

import java.util.List;
import java.util.stream.Collectors;

import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.ContributionType;
import org.grupo11.Services.Contributor.Individual;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class MealsPerContributorReportRow {
    @Id
    @GeneratedValue
    private Long id;
    private Long contributorId;
    private String contributorName;
    private int donatedMeals;
    private int totalDonatedMeals;

    public MealsPerContributorReportRow() {
    }

    public MealsPerContributorReportRow(Individual contributor, Long fromDate, long toDate) {
        this.contributorId = contributor.getId();
        this.contributorName = contributor.getName();
        List<Contribution> contributions = contributor.getContributions().stream()
                    .filter(contribution -> (contribution.getContributionType() == ContributionType.MEAL_DONATION  && contribution.getDate() < toDate))
                    .collect(Collectors.toList());
        this.totalDonatedMeals = contributions.size();
        this.donatedMeals = (contributions.stream()
                    .filter(contribution -> contribution.getDate() >= fromDate)
                    .collect(Collectors.toList()))
                    .size();
    }

    public Long getId() {
        return id;
    }

    public Long getContributorId() {
        return contributorId;
    }

    public String getContributorName() {
        return contributorName;
    }

    public int getDonatedMeals() {
        return donatedMeals;
    }

    public int getTotalDonatedMeals() {
        return totalDonatedMeals;
    }
}
