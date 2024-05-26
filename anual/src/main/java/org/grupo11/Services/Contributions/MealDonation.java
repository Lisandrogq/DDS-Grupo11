package org.grupo11.Services.Contributions;

import org.grupo11.Services.Meal;

public class MealDonation extends Contribution {
    Meal meal;

    public MealDonation(Meal meal) {
        this.meal = meal;
    }

    public ContributionType getContributionType() {
        return ContributionType.MEAL_DONATION;
    }
}
