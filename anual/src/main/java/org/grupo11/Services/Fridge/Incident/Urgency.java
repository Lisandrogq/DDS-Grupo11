package org.grupo11.Services.Fridge.Incident;


public enum Urgency {
    High,
    Medium,
    Low;

    public String toString() {
        switch (this) {
            case High:
                return "High";
            case Medium:
                return "Medium";
            case Low:
                return "Low";
            default:
                throw new IllegalArgumentException("Unknown urgency: " + this);
        }
    }
}
