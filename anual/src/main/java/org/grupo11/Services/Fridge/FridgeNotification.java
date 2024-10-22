package org.grupo11.Services.Fridge;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class FridgeNotification {
    private String subject;
    private String message;
    @Enumerated(EnumType.STRING)
    private FridgeNotifications type;

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
