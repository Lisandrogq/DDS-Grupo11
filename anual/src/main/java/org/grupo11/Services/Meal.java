package org.grupo11.Services;

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
    private long expirationDate;
    private long donationDate;
    @ManyToOne
    private Fridge fridge;
    private String state;
    private int calories;
    private int weight;

    public Meal() {
    }

    public Meal(String type, long expirationDate, long donationDate, Fridge fridge,
            String state, Integer calories, Integer weight) {
        this.type = type;
        this.expirationDate = expirationDate;
        this.donationDate = donationDate;
        this.state = state;
        this.fridge = fridge;
        this.calories = calories;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public long getDonationDate() {
        return donationDate;
    }

    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
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
