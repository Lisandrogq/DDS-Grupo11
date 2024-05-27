package org.grupo11.Services;

import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Fridge;

public class Meal {
    private String type;
    private int expirationDate;
    private Contributor contributor;
    private int donationDate;
    private Fridge fridge;
    private String state;
    private Integer calories;
    private Integer weight;

    public Meal(String type, int expirationDate, Contributor contributor, int donationDate, Fridge fridge,
            String state, Integer calories, Integer weight) {
        this.type = type;
        this.expirationDate = expirationDate;
        this.contributor = contributor;
        this.donationDate = donationDate;
        this.fridge = fridge;
        this.state = state;
        this.calories = calories;
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public int getExpirationDate() {
        return expirationDate;
    }

    public Contributor getContributor() {
        return contributor;
    }

    public int getDonationDate() {
        return donationDate;
    }

    public Fridge getFridge() {
        return fridge;
    }

    public String getState() {
        return state;
    }

    public Integer getCalories() {
        return calories;
    }

    public Integer getWeight() {
        return weight;
    }
}
