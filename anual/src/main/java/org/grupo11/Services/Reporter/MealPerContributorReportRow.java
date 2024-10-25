package org.grupo11.Services.Reporter;

import java.util.List;

import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributor.Contributor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class MealPerContributorReportRow {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Contributor contributor;
    @OneToMany
    private List<MealDonation> donatedMeals;

    public MealPerContributorReportRow() {
    }

    public MealPerContributorReportRow(Contributor contributor, List<MealDonation> donatedMeals) {
        this.contributor = contributor;
        this.donatedMeals = donatedMeals;
    }

    public Contributor getContributor() {
        return this.contributor;
    }

    public List<MealDonation> getdonatedMeals() {
        return this.donatedMeals;
    }
}
