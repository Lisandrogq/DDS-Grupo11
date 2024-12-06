package org.grupo11.Api.JsonData.FridgeInfo;

import java.util.List;

import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Contributor.Individual;
import org.grupo11.Services.Contributor.LegalEntity.LegalEntity;
import org.grupo11.Services.Fridge.Incident.AlertType;
import org.grupo11.Services.Fridge.Incident.Urgency;

public class FridgeFullInfo {

    private int fridgeId;
    private List<MealsData> meals;
    private List<IncidentsData> incidents;

    public int getFridgeId() {
        return fridgeId;
    }

    public void setFridgeId(int fridgeId) {
        this.fridgeId = fridgeId;
    }

    public List<MealsData> getMeals() {
        return meals;
    }

    public void setMeals(List<MealsData> meals) {
        this.meals = meals;
    }

    public List<IncidentsData> getFailures() {
        return incidents;
    }

    public void setIncidents(List<IncidentsData> incidents) {
        this.incidents = incidents;
    }

    public static class MealsData {
        private Long id;
        private String type;
        private long expirationDate;
        private String state;
        private int weight;
        private int calories;

        public void setId(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setExpirationDate(long expirationDate) {
            this.expirationDate = expirationDate;
        }

        public long getExpirationDate() {
            return expirationDate;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getState() {
            return state;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }

        public void setCalories(int calories) {
            this.calories = calories;
        }

        public int getCalories() {
            return calories;
        }


    }

    public static class IncidentsData {
        private Long id;
        private String description;
        private long detectedAt;
        private String hasBeenFixed;

        public void setId(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setFailureDescription(Contributor reportedBy, Urgency urgencyEnum, String description) {
            String reporterName;
            if (reportedBy instanceof Individual) {
                Individual individual = (Individual) reportedBy;
                reporterName = individual.getName();
            } else if (reportedBy instanceof LegalEntity) {
                LegalEntity legalEntity = (LegalEntity) reportedBy;
                reporterName = legalEntity.getName();
            } else {
                reporterName = "Unknown";
            }
            String urgency = urgencyEnum.toString();
            this.description = description + " (from " + reporterName + " with " + urgency + " priority)";
        }

        public void setAlertDescription(AlertType type) {
            this.description = "Automatic alert of type: " + type.toString();
        }

        public String getDescription() {
            return description;
        }

        public void setDetectedAt(long detectedAt) {
            this.detectedAt = detectedAt;
        }

        public long getDetectedAt() {
            return detectedAt;
        }

        public void setHasBeenFixed(boolean hasBeenFixed) {
            if(hasBeenFixed) this.hasBeenFixed = "Yes";
            else this.hasBeenFixed = "No";
        }

        public String getHasBeenFixed() {
            return hasBeenFixed;
        }
    }
}
