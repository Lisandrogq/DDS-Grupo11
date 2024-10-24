package org.grupo11.Services.Fridge.Incident;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Alert extends Incident {
    @Enumerated(EnumType.STRING)
    private AlertType type;

    public Alert() {
        super();
    }

    public Alert(AlertType type, long detectedAt) {
        super(detectedAt);
        this.type = type;
    }

    public AlertType getType() {
        return this.type;
    }
}
