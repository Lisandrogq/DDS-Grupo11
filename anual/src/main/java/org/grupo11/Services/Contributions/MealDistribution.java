package org.grupo11.Services.Contributions;

import org.grupo11.Services.Meal;
import org.grupo11.Services.Fridge.Fridge;

public class MealDistribution extends Contribution {
    private Fridge originFridge;
    private Fridge destinyFridge;
    private int quantity;
    private String reason;
    private Meal meal;
    private int distributionDate;

    // Constructor
    public MealDistribution(Fridge originFridge, Fridge destinyFridge, int quantity, String reason, Meal meal,
            int distributionDate) {
        this.originFridge = originFridge;
        this.destinyFridge = destinyFridge;
        this.quantity = quantity;
        this.reason = reason;
        this.meal = meal;
        this.distributionDate = distributionDate;
    }

    public ContributionType getContributionType() {
        return ContributionType.MEAL_DISTRIBUTION;
    }
}
