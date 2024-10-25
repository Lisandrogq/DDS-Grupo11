package org.grupo11.Services.Reporter;

import java.util.List;

import org.grupo11.Services.Meal;
import org.grupo11.Services.Fridge.Fridge;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class MealsPerFridgeReportRow {
    private Fridge fridge;
    private List<Meal> mealsIn;
    private List<Meal> mealsOut;

    public MealsPerFridgeReportRow(Fridge fridge, List<Meal> mealsIn,List<Meal> mealsOut) {
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
