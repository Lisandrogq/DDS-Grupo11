package org.grupo11.Services.Fridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.grupo11.Enums.Provinces;
import org.grupo11.Services.Meal;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Incident.Incident;
import org.grupo11.Services.Fridge.Sensor.MovementSensorManager;
import org.grupo11.Services.Fridge.Sensor.SensorManager;
import org.grupo11.Services.Fridge.Sensor.TemperatureSensorManager;
import org.grupo11.Utils.Crypto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Fridge {
    @Id
    @GeneratedValue
    private int id;

    private boolean isActive;
    private double lon;
    private double lat;
    @Enumerated(EnumType.STRING)
    private Provinces area;
    private String address;
    private String name;
    private int capacity;
    private int commissioningDate;
    @OneToMany
    private List<Meal> meals = new ArrayList<Meal>();

    private Integer addedMeals = 0;
    private Integer removedMeals = 0;
    @OneToMany(cascade = CascadeType.ALL)
    private List<SensorManager> sensorManagers = new ArrayList<SensorManager>(); // [0] = temp , [1] = mov
    /*
     * @OneToOne(cascade = CascadeType.ALL) /// los saque pq se volvio imposible
     * manejar las dos bidireccionalidades al mismo tiempo
     * private TemperatureSensorManager tempManager;
     * 
     * @OneToOne(cascade = CascadeType.ALL)
     * private MovementSensorManager movManager;
     */
    @OneToMany
    private List<FridgeSolicitude> openSolicitudes;
    @OneToMany
    private List<FridgeOpenLogEntry> openedHistory;
    @OneToMany
    private List<Incident> incidents;
    @OneToMany(cascade = CascadeType.ALL)
    protected List<Subscription> notificationSubscriptions;
    @OneToMany
    private List<FridgeNotification> notificationsSent;

    public Fridge() {
        this.openSolicitudes = new ArrayList<>();
        this.openedHistory = new ArrayList<>();
        this.incidents = new ArrayList<>();
        this.notificationSubscriptions = new ArrayList<>();
        this.notificationsSent = new ArrayList<>();
    }

    public Fridge(double lon, double lat, String address, String name, int capacity, int commissioningDate,
            List<Meal> meals,
            TemperatureSensorManager tempManager, MovementSensorManager movManager) {
        this.lon = lon;
        this.lat = lat;
        this.address = address;
        this.name = name;
        this.capacity = capacity;
        this.commissioningDate = commissioningDate;
        this.meals = meals;
        this.sensorManagers.add(0, tempManager);
        this.sensorManagers.add(1, movManager);
        this.openSolicitudes = new ArrayList<>();
        this.openedHistory = new ArrayList<>();
        this.incidents = new ArrayList<>();
        this.notificationSubscriptions = new ArrayList<>();
        this.notificationsSent = new ArrayList<>();
    }

    // Method to convert to a Map
    public Map<String, Object> toMap() {
        Map<String, Object> fridgeMap = new HashMap<>();
        fridgeMap.put("name", getName());
        fridgeMap.put("id", getId());
        fridgeMap.put("temp", getTempManager().getLastTemp());
        fridgeMap.put("reserved", 0);// q pija es esto??
        fridgeMap.put("state", getIsActive() ? "Active" : "Inactive");
        fridgeMap.put("meals", getMeals().size());
        fridgeMap.put("food_status_desc", "located at " + getAddress());
        int cantIncidentes = getActiveIncidents().size();
        fridgeMap.put("meal_urgency",
                cantIncidentes + " Active Incident" + (cantIncidentes == 1 ? "" : "s"));
        return fridgeMap;
    }

    public void setTempManager(TemperatureSensorManager tempManager) {

        this.sensorManagers.add(0, tempManager);

    }

    public void setMovManager(MovementSensorManager movManager) {

        this.sensorManagers.add(1, movManager);
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

    public void cleanHistory() {
        this.addedMeals = 0;
        this.removedMeals = 0;
    }

    public Integer getAddedMeals() {
        return this.addedMeals;
    }

    public Integer getRemovedMeals() {
        return this.removedMeals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public void addMeal(Meal meal) {
        this.meals.add(meal);
        this.addedMeals++;
        this.evaluateSendNotification(
                new FridgeNotification(FridgeNotifications.NearFullInventory, meals.size(), "Fridge almost full"));
    }

    public void removeMeal(Meal meal) {
        this.meals.remove(meal);
        this.removedMeals++;
        this.evaluateSendNotification(
                new FridgeNotification(FridgeNotifications.LowInventory, meals.size(), "Fridge almost full"));

    }

    public Meal getMealByType(String type) {
        for (Meal meal : meals) {
            if (meal.getType().equals(type)) {
                return meal;
            }
        }
        return null;
    }

    public TemperatureSensorManager getTempManager() {
        return (TemperatureSensorManager) this.sensorManagers.get(0);
    }

    public MovementSensorManager getMovManager() {
        return (MovementSensorManager) this.sensorManagers.get(1);

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

    public boolean hasPermission(int registryId) {
        for (FridgeSolicitude solicitude : openSolicitudes) {
            if (solicitude.getIssuedBy().getId() == registryId && !solicitude.hasBeenUsed()) {
                if (solicitude.isValid()) {
                    solicitude.markAsUsed();
                    return true;
                }
                ;
            }
        }
        return false;
    }
    public Incident getIncidentById(int incident_id) {
        for (Incident incident : incidents) {
            if (incident.getId() == incident_id) {
                return incident; 
            }
        }
        return null; 
    }

    public List<Incident> getIncidents() {
        return this.incidents;
    }

    public List<Incident> getActiveIncidents() {
        return incidents.stream()
                .filter(incident -> !incident.hasBeenFixed())
                .collect(Collectors.toList());
    }

    public void setIncidents(List<Incident> incidents) {
        this.incidents = incidents;
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

    public boolean isSubscribed(Contributor contributor) {
        return this.notificationSubscriptions.stream().anyMatch(subscription -> subscription.getContributor().getId().equals(contributor.getId()));
    }

    public void removeSubscriber(Contributor contributor) {
        this.notificationSubscriptions.removeIf(subscription -> subscription.getContributor().getId().equals(contributor.getId()));
    }

    public List<Subscription> getSubscriptions(Contributor contributor) {
        return this.notificationSubscriptions.stream().filter(subscription -> subscription.getContributor().getId().equals(contributor.getId())).collect(Collectors.toList());
    }

    public void evaluateSendNotification(FridgeNotification fridgeNotification) {
        for (Subscription subscription : this.notificationSubscriptions) {
            boolean low_condition = fridgeNotification.getType() == FridgeNotifications.LowInventory
                    && subscription.getThreshold() >= fridgeNotification.getAmmount();
            boolean full_condition = fridgeNotification.getType() == FridgeNotifications.NearFullInventory
                    && subscription.getThreshold() <= fridgeNotification.getAmmount();

            if (low_condition || full_condition || fridgeNotification.getType() == FridgeNotifications.Malfunction) {
                if (subscription.getType() == fridgeNotification.getType()) {
                    subscription.getContributor().getContacts().forEach(value -> {
                        this.notificationsSent.add(fridgeNotification);
                        value.SendNotification("Subscription alert", fridgeNotification.getMessage());
                    });
                }
            }
        }
    }
}
