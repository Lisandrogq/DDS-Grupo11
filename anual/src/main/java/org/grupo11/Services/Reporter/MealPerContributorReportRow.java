package org.grupo11.Services.Reporter;

import java.util.List;

import org.grupo11.Services.Meal;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Fridge;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class MealPerContributorReportRow {
    private Contributor contributor;
    private List<MealDonation> donatedMeals;

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
