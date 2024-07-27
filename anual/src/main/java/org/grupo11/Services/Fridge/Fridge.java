package org.grupo11.Services.Fridge;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Domain.Sensor.MovementSensorManager;
import org.grupo11.Domain.Sensor.TemperatureSensorManager;
import org.grupo11.Enums.Provinces;
import org.grupo11.Services.Meal;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Incident.Incident;
import org.grupo11.Utils.Crypto;

public class Fridge {
    private int id;
    private boolean isActive;
    private double lon;
    private double lat;
    private Provinces area;
    private String address;
    private String name;
    private int capacity;
    private int commissioningDate;
    private List<Meal> meals;
    private TemperatureSensorManager tempManager;
    private MovementSensorManager movManager;
    private List<FridgeSolicitude> openSolicitudes;
    private List<FridgeOpenLogEntry> openedHistory;
    private List<Incident> incidents;
    protected List<Subscription> notificationSubscriptions;
    private List<FridgeNotification> notificationsSent;

    public Fridge(double lon, double lat, String address, String name, int capacity, int commissioningDate,
            List<Meal> meals,
            TemperatureSensorManager tempManager, MovementSensorManager movManager) {
        this.id = Crypto.getRandomId(6);
        this.lon = lon;
        this.lat = lat;
        this.address = address;
        this.name = name;
        this.capacity = capacity;
        this.commissioningDate = commissioningDate;
        this.meals = meals;
        this.tempManager = tempManager;
        this.movManager = movManager;
        this.openSolicitudes = new ArrayList<FridgeSolicitude>();
        this.openedHistory = new ArrayList<FridgeOpenLogEntry>();
    }

    public int getId() {
        return id;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public double getLon() {
        return this.lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Provinces getArea() {
        return this.area;
    }

    public void setArea(Provinces area) {
        this.area = area;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCommissioningDate() {
        return this.commissioningDate;
    }

    public void setCommissioningDate(int commissioningDate) {
        this.commissioningDate = commissioningDate;
    }

    public List<Meal> getMeals() {
        return this.meals;
    }

    public void addMeal(Meal meal) {
        this.meals.add(meal);
        // if the fridge is 90 percent full, send a notification
        if (meals.size() >= this.capacity * 0.9)
            this.sendFridgeNotifications(
                    new FridgeNotification(FridgeNotifications.NearFullInventory, "Fridge almost full",
                            "Fridge has low inventory with " + meals.size() + " meals"));
    }

    public void removeMeal(Meal meal) {
        this.meals.remove(meal);
        // if the fridge is 25 percent full, send a notification
        if (meals.size() >= this.capacity * 0.25)

            this.sendFridgeNotifications(
                    new FridgeNotification(FridgeNotifications.NearFullInventory, "Fridge almost full",
                            "Fridge has low inventory with " + meals.size() + " meals"));

    }

    public TemperatureSensorManager getTempManager() {
        return this.tempManager;
    }

    public void setTemp(TemperatureSensorManager temp) {
        this.tempManager = temp;
    }

    public MovementSensorManager getMovManager() {
        return this.movManager;
    }

    public void setMov(MovementSensorManager mov) {
        this.movManager = mov;
    }

    public String getMapLocation() {
        return FridgeMapper.getSingleFridgeMapLocation(this);
    }

    public List<FridgeSolicitude> getOpenSolicitudes() {
        return this.openSolicitudes;
    }

    public void addSolicitudes(FridgeSolicitude openSolicitude) {
        openSolicitudes.add(openSolicitude);
    }

    public List<FridgeOpenLogEntry> getOpenedHistory() {
        return this.openedHistory;
    }

    public void addOpenEntry(FridgeOpenLogEntry entry) {
        openedHistory.add(entry);
    }

    public boolean hasPermission(Contributor contributor) {
        for (FridgeSolicitude solicitude : openSolicitudes) {
            if (solicitude.getIssuedBy().getId() == contributor.getCard().getId() && !solicitude.hasBeenUsed()) {
                if (solicitude.isValid()) {
                    solicitude.markAsUsed();
                    return true;
                }
                ;
            }
        }
        return false;
    }

    public List<Incident> getIncidents() {
        return this.incidents;
    }

    public void addIncident(Incident incident) {
        this.incidents.add(incident);
    }

    public List<Subscription> getNotificationSubscription() {
        return this.notificationSubscriptions;
    }

    public void addNotificationSubscription(Subscription subscription) {
        this.notificationSubscriptions.add(subscription);
    }

    public void removeNotificationSubscription(Subscription subscription) {
        this.notificationSubscriptions.remove(subscription);
    }

    public void sendFridgeNotifications(FridgeNotification fridgeNotification) {
        for (Subscription subscription : this.notificationSubscriptions) {
            if (subscription.getType() == fridgeNotification.getType()) {

                subscription.getContributor().getContacts().forEach(value -> {
                    this.notificationsSent.add(fridgeNotification);
                    value.SendNotification(fridgeNotification.getSubject(), fridgeNotification.getMessage());
                });
            }
        }
    }
}
