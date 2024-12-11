package org.grupo11.Services.Reporter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.grupo11.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.ContributionType;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributor.Contributor;
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

    public MealsPerContributorReportRow(Individual contributor, Long lastReport) {
        this.contributorId = contributor.getId();
        this.contributorName = contributor.getName();
        Logger.info("Contributor name: {}", contributorName);
        Logger.info("Contributor id: {}", contributorId);
        Logger.info("Contributor contributions: {}", contributor.getContributions().size());
        List<Contribution> contributions = contributor.getContributions().stream()
                    .filter(contribution -> (contribution.getContributionType() == ContributionType.MEAL_DONATION))
                    .collect(Collectors.toList());
        Logger.info("Contributor meal donations: {}", contributions.size());
        this.totalDonatedMeals = contributions.size();
        this.donatedMeals = (contributions.stream()
                    .filter(contribution -> contribution.getDate() > lastReport)
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
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("id", Long.toString(id));
        map.put("contributorId", Long.toString(contributorId));
        map.put("contributorName", contributorName);
        map.put("donatedMeals", Integer.toString(donatedMeals));
        map.put("totalDonatedMeals", Integer.toString(totalDonatedMeals));

        return map;
    }
}
