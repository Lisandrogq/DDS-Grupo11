package org.grupo11.Services;

import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Fridge;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Meal {
    @Id
    @GeneratedValue
    private Long id;

    private String type;
    private int expirationDate;
    @ManyToOne
    private Contributor contributor;
    private int donationDate;
    @ManyToOne
    private Fridge fridge;
    private String state;
    private int calories;
    private int weight;

    public Meal() {
    }

    public Meal(String type, int expirationDate, int donationDate, Fridge fridge,
            String state, Integer calories, Integer weight) {
        this.type = type;
        this.expirationDate = expirationDate;
        this.donationDate = donationDate;
        this.state = state;
        this.fridge = fridge;
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

    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
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
