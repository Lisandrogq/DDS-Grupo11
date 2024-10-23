package org.grupo11.Services.Fridge.Incident;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Alert extends Incident {
    @Id
    @GeneratedValue
    private long id;
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
