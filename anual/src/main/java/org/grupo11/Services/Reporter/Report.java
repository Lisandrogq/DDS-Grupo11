package org.grupo11.Services.Reporter;

public class Report {
    private long createdAt;
    private float failuresPerFridge;
    private float mealsInOutPerFridge;
    private float mealsOutPerFridge;
    private float mealsPerCollaborator;

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
