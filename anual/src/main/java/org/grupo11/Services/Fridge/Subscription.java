package org.grupo11.Services.Fridge;

import org.grupo11.Services.Contributor.Contributor;

public class Subscription {
    private Contributor contributor;
    private FridgeNotifications type;

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
