package org.grupo11.Services.Fridge.Incident;

public class Alert extends Incident {
    private AlertType type;

    public Alert(AlertType type, long detectedAt) {
        super(detectedAt);
        this.type = type;
    }

    public AlertType getType() {
        return this.type;
    }
}
