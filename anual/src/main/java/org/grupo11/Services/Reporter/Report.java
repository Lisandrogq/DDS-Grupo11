package org.grupo11.Services.Reporter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Report {
    @Id
    @GeneratedValue
    private Long id;
    private long createdAt;
    private float failuresPerFridge;
    private float mealsInOutPerFridge;
    private float mealsOutPerFridge;
    private float mealsPerCollaborator;

    public Report() {
    }

    public Report(long createdAt, float failuresPerFridge, float mealsInOutPerFridge,
            float mealsPerCollaborator) {
        this.createdAt = createdAt;
        this.failuresPerFridge = failuresPerFridge;
        this.mealsInOutPerFridge = mealsInOutPerFridge;
        this.mealsPerCollaborator = mealsPerCollaborator;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public float getFailuresPerFridge() {
        return this.failuresPerFridge;
    }

    public float getMealsInOutPerFridge() {
        return this.mealsInOutPerFridge;
    }

    public float getMealsOutPerFridge() {
        return this.mealsOutPerFridge;
    }

    public float getMealsPerCollaborator() {
        return this.mealsPerCollaborator;
    }

}
