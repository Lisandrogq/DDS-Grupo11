package org.grupo11.Services.Fridge;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class FridgeNotification {
    @Id
    @GeneratedValue
    private Long id;
    private String subject;
    private String message;
    @Enumerated(EnumType.STRING)
    private FridgeNotifications type;
    @ManyToOne
    private Fridge fridge;

    public FridgeNotification() {
    }

    public FridgeNotification(FridgeNotifications type, String subject, String message) {
        this.subject = subject;
        this.message = message;
        this.type = type;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getMessage() {
        return this.message;
    }

    public FridgeNotifications getType() {
        return this.type;
    }
}
