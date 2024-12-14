package org.grupo11.Services.Fridge.Incident;

import org.grupo11.Services.Fridge.Fridge;

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

    public Alert(AlertType type, long detectedAt, Fridge fridge) {
        super(detectedAt, fridge);
        this.type = type;
    }

    public AlertType getType() {
        return this.type;
    }
}
