package org.grupo11.Services.Reporter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributor.Contributor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class MealsPerContributorReportRow {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Contributor contributor;
    @OneToMany
    private List<MealDonation> donatedMeals;

    public MealsPerContributorReportRow() {
    }

    public MealsPerContributorReportRow(Contributor contributor, List<MealDonation> donatedMeals) {
        this.contributor = contributor;
        this.donatedMeals = donatedMeals;
    }

    public Contributor getContributor() {
        return this.contributor;
    }

    public List<MealDonation> getdonatedMeals() {
        return this.donatedMeals;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("id", id);
        map.put("contributor", contributor);
        map.put("donatedMeals", donatedMeals.size());

        return map;
    }
}
