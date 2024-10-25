package org.grupo11.Services.Reporter;

import java.util.List;

import org.grupo11.Services.Meal;
import org.grupo11.Services.Fridge.Fridge;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class MealsPerFridgeReportRow {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Fridge fridge;
    @OneToMany
    private List<Meal> mealsIn;
    @OneToMany
    private List<Meal> mealsOut;

    public MealsPerFridgeReportRow() {
    }

    public MealsPerFridgeReportRow(Fridge fridge, List<Meal> mealsIn, List<Meal> mealsOut) {
        this.fridge = fridge;
        this.mealsIn = mealsIn;
        this.mealsOut = mealsOut;
    }

    public Fridge getFridge() {
        return this.fridge;
    }

    public List<Meal> getMealsOut() {
        return this.mealsOut;
    }

    public List<Meal> getMealsin() {
        return this.mealsIn;
    }
}
