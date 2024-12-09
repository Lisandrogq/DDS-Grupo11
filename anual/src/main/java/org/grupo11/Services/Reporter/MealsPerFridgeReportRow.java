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

    private Integer mealsIn;

    private Integer mealsOut;

    public MealsPerFridgeReportRow() {
    }

    public MealsPerFridgeReportRow(Fridge fridge, Integer mealsIn, Integer mealsOut) {
        this.fridge = fridge;
        this.mealsIn = mealsIn;
        this.mealsOut = mealsOut;
    }

    public Fridge getFridge() {
        return this.fridge;
    }

    public Integer getMealsOut() {
        return this.mealsOut;
    }

    public Integer getMealsin() {
        return this.mealsIn;
    }
}
