package org.grupo11.Services.Fridge;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> notifications;
    public Subscription() {
        this.notifications = new ArrayList<>();
    }

    public Subscription(Contributor contributor, FridgeNotifications fridgeNotificationsPreferences,int threshold) {
        this.contributor = contributor;
        this.type = fridgeNotificationsPreferences;
        this.threshold = threshold;
    }
    public List<String> getNotifications() {
        return notifications;
    }
    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }
    public void addNotification(String notification){
        notifications.add(notification);
    }
    public Contributor getContributor() {
        return this.contributor;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
    }

    public FridgeNotifications getType() {
        return this.type;
    }

    public void setType(FridgeNotifications type) {
        this.type = type;
    }

    public int getThreshold(){
        return this.threshold;
    }

    public void setThreshold(int threshold){
        this.threshold = threshold;
    }

    public void sendFridgeNotification(String subject, String message) {
        this.contributor.getContacts().get(0).SendNotification(subject, message);
    }
}
