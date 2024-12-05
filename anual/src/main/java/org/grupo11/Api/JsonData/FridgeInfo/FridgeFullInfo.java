package org.grupo11.Api.JsonData.FridgeInfo;

import java.util.List;

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
        /*
        Incident
        private Long id;
        private List<TechnicianVisit> visits;
        private boolean hasBeenFixed;
        private long detectedAt;

        Alert
        private AlertType type;
        public enum AlertType {
            TEMPERATUREALERT,
            FRAUDALERT,
            CONNECTIONFAIULERALERT,
        }

        Failure
        private private Contributor reportedBy;
        private String description;
        private Urgency urgency;

        id
        Description
        detectedAt
        fixeado?
        */

        private Long reportedBy;
        private String description;
        private String urgency;

        public void setReportedBy(Long reportedBy) {
            this.reportedBy = reportedBy;
        }

        public Long getReportedBy() {
            return reportedBy;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setUrgency(String urgency) {
            this.urgency = urgency;
        }

        public String getUrgency() {
            return urgency;
        }
    }
}
