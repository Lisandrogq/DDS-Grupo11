package org.grupo11.Services.Fridge;

import org.grupo11.Services.Contributor.Contributor;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Subscription {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Contributor contributor;
    @Enumerated(EnumType.STRING)
    private FridgeNotifications type;
    private int threshold=0;
    @ManyToOne
    private Fridge fridge;

    public Subscription(int threshold) {
    }

    public Subscription(Contributor contributor, FridgeNotifications fridgeNotificationsPreferences,int threshold) {
        this.contributor = contributor;
        this.type = fridgeNotificationsPreferences;
        this.threshold = threshold;
    }

    public Contributor getContributor() {
        return this.contributor;
    }

    public FridgeNotifications getType() {
        return this.type;
    }
    public int getThreshold(){
        return this.threshold;
    }
    public void sendFridgeNotification(String subject, String message) {
        this.contributor.getContacts().get(0).SendNotification(subject, message);
    }
}
