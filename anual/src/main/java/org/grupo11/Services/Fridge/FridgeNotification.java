package org.grupo11.Services.Fridge;

public class FridgeNotification {
    private String subject;
    private String message;
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
