package org.grupo11.Services.Contributions;

import org.grupo11.Services.Meal;

public class MealDonation extends Contribution {
    private Meal meal;

    public MealDonation(Meal meal, long date) {
        super(date);
        this.meal = meal;
    }

    public ContributionType getContributionType() {
        return ContributionType.MEAL_DONATION;
    }

    public Meal getMeal() {
        return this.meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }
}
