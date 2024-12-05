package org.grupo11.Services.Fridge.Incident;


public enum Urgency {
    High,
    Medium,
    Low;

    public String toString() {
        switch (this) {
            case High:
                return "high";
            case Medium:
                return "medium";
            case Low:
                return "low";
            default:
                throw new IllegalArgumentException("Unknown urgency: " + this);
        }
    }
}
