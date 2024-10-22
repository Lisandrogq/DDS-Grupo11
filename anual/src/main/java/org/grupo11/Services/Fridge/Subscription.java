package org.grupo11.Services.Fridge;

import org.grupo11.Services.Contributor.Contributor;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;

@Entity
public class Subscription {
    @ManyToOne
    private Contributor contributor;
    @Enumerated(EnumType.STRING)
    private FridgeNotifications type;

    public Subscription() {
    }

    public Subscription(Contributor contributor, FridgeNotifications fridgeNotificationsPreferences) {
        this.contributor = contributor;
        this.type = fridgeNotificationsPreferences;
    }

    public Contributor getContributor() {
        return this.contributor;
    }

    public FridgeNotifications getType() {
        return this.type;
    }

    public void sendFridgeNotification(String subject, String message) {
        this.contributor.getContacts().get(0).SendNotification(subject, message);
    }
}
