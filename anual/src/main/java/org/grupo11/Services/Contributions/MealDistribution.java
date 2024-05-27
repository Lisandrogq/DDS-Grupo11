package org.grupo11.Services.Contributions;

import org.grupo11.Services.Meal;
import org.grupo11.Services.Fridge.Fridge;

public class MealDistribution extends Contribution {
    private Fridge originFridge;
    private Fridge destinyFridge;
    private int quantity;
    private String reason;
    private Meal meal;

    // Constructor
    public MealDistribution(Fridge originFridge, Fridge destinyFridge, int quantity,
            String reason, Meal meal,
            long distributionDate) {
        super(distributionDate);
        this.originFridge = originFridge;
        this.destinyFridge = destinyFridge;
        this.quantity = quantity;
        this.reason = reason;
        this.meal = meal;
    }

    public ContributionType getContributionType() {
        return ContributionType.MEAL_DISTRIBUTION;
    }

    public Fridge getOriginFridge() {
        return this.originFridge;
    }

    public void setOriginFridge(Fridge originFridge) {
        this.originFridge = originFridge;
    }

    public Fridge getDestinyFridge() {
        return this.destinyFridge;
    }

    public void setDestinyFridge(Fridge destinyFridge) {
        this.destinyFridge = destinyFridge;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Meal getMeal() {
        return this.meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }
}
