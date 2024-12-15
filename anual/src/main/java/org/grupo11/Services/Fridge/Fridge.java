package org.grupo11.Services.Fridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Enums.Provinces;
import org.grupo11.Services.Meal;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Incident.Incident;
import org.grupo11.Services.Fridge.Sensor.MovementSensorManager;
import org.grupo11.Services.Fridge.Sensor.TemperatureSensorManager;
import org.grupo11.Services.Technician.Technician;
import org.grupo11.Services.Technician.TechnicianManager;
import org.grupo11.Utils.LocationHandler;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

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
    @OneToOne
    private TemperatureSensorManager tempManager;
    @OneToOne
    private MovementSensorManager movManager;
    @OneToMany(cascade = CascadeType.ALL)
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
        this.tempManager = tempManager;
        this.movManager = movManager;
        this.openSolicitudes = new ArrayList<>();
        this.openedHistory = new ArrayList<>();
        this.incidents = new ArrayList<>();
        this.notificationSubscriptions = new ArrayList<>();
        this.notificationsSent = new ArrayList<>();
    }

    public Fridge(String address, String name, int capacity, int commissioningDate,
            List<Meal> meals,
            TemperatureSensorManager tempManager, MovementSensorManager movManager) {
        this.address = address;
        if (address != null && !address.isEmpty()) {
            setLatAndLon(address);
        }
        this.name = name;
        this.capacity = capacity;
        this.commissioningDate = commissioningDate;
        this.meals = meals;
        this.tempManager = tempManager;
        this.movManager = movManager;
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
        fridgeMap.put("id", getIdAsString());
        fridgeMap.put("lat", getLatAsString());
        fridgeMap.put("lon", getLonAsString());
        fridgeMap.put("temp", Double.toString(getTempManager().getLastTemp()));
        fridgeMap.put("reserved", 0);
        fridgeMap.put("state", getIsActive() ? "Active" : "Inactive");
        fridgeMap.put("meals", Integer.toString(getMeals().size()));
        fridgeMap.put("food_status_desc", "located at " + getAddress());
        fridgeMap.put("capacity", Integer.toString(getCapacity()));
        fridgeMap.put("address", getAddress());
        int cantIncidentes = getActiveIncidents().size();
        fridgeMap.put("meal_urgency",
                cantIncidentes + " Active Incident" + (cantIncidentes == 1 ? "" : "s"));
        return fridgeMap;
    }

    public void setLatAndLon(String address) {
        try {
            double[] coordinates = LocationHandler.getCoordinates(address);
            this.lat = coordinates[0];
            this.lon = coordinates[1];
        } catch (Exception e) {
            Logger.error("Error setting lat and lon for fridge: " + this.name);
        }
    }

    public void setTempManager(TemperatureSensorManager tempManager) {
        this.tempManager = tempManager;

    }

    public void setMovManager(MovementSensorManager movManager) {
        this.movManager = movManager;
    }

    public int getId() {
        return id;
    }

    public String getIdAsString() {
        return Integer.toString(this.id);
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

    public String getLonAsString() {
        return Double.toString(this.lon);
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return this.lat;
    }

    public String getLatAsString() {
        return Double.toString(this.lat);
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

    public FridgeNotification addMeal(Meal meal) {
        this.meals.add(meal);
        this.addedMeals++;
        FridgeNotification notification = new FridgeNotification(FridgeNotifications.NearFullInventory, meals.size(),
                name + "Fridge almost full");
        this.evaluateSendNotification(notification);
        return notification;
    }

    public FridgeNotification removeMeal(Meal meal) {
        this.meals.remove(meal);
        this.removedMeals++;
        FridgeNotification notification = new FridgeNotification(FridgeNotifications.LowInventory, meals.size(),
                name + " Fridge almost empty");
        this.evaluateSendNotification(notification);
        return notification;

    }

    public Meal getMealByID(Long id) {
        for (Meal meal : meals) {
            if (meal.getId().equals(id)) {
                return meal;
            }
        }
        return null;
    }

    public TemperatureSensorManager getTempManager() {
        return this.tempManager;
    }

    public MovementSensorManager getMovManager() {
        return this.movManager;

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
            }
        }
        return false;
    }

    public boolean hasSolicitude(int registryId) {
        for (FridgeSolicitude solicitude : openSolicitudes) {
            if (solicitude.getIssuedBy().getId() == registryId && !solicitude.hasBeenUsed()) {
                if (solicitude.isValid()) {
                    return true;
                }
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

    public Map<String, Object> addIncident(Incident incident) {
        FridgeNotification notification = new FridgeNotification(this, FridgeNotifications.Malfunction, 0,
                this.name + " fridge is malfunctioning");
        Technician selectedTechnician = TechnicianManager.getInstance().sendHelpMsgByDistance(this);
        this.incidents.add(incident);
        this.evaluateSendNotification(notification);
        Map<String, Object> return_map = new HashMap<>();
        return_map.put("fridge_notification", notification);
        return_map.put("selected_technician", selectedTechnician);
        return return_map;
    }

    public void addIncidentAndStoreOnDB(Incident incident) {
        this.incidents.add(incident);
        FridgeNotification notification = new FridgeNotification(this, FridgeNotifications.Malfunction, 0,
                this.name + " fridge is malfunctioning");
        this.evaluateSendNotification(notification);
        Technician technician = TechnicianManager.getInstance().sendHelpMsgByDistance(this);
        if (technician != null)
            DB.update(technician);
        DB.create(incident);
        DB.create(notification);
        DB.update(this);
    }

    public void addNotificationSent(FridgeNotification notification) {
        this.notificationsSent.add(notification);
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
        return this.notificationSubscriptions.stream()
                .anyMatch(subscription -> subscription.getContributor().getId().equals(contributor.getId()));
    }

    public void removeSubscriber(Contributor contributor) {
        this.notificationSubscriptions
                .removeIf(subscription -> subscription.getContributor().getId().equals(contributor.getId()));
    }

    public List<Subscription> getSubscriptions(Contributor contributor) {
        return this.notificationSubscriptions.stream()
                .filter(subscription -> subscription.getContributor().getId().equals(contributor.getId()))
                .collect(Collectors.toList());
    }

    public void evaluateSendNotification(FridgeNotification fridgeNotification) {
        for (Subscription subscription : this.notificationSubscriptions) {
            boolean low_condition = fridgeNotification.getType() == FridgeNotifications.LowInventory
                    && subscription.getThreshold() >= fridgeNotification.getAmmount();
            boolean full_condition = fridgeNotification.getType() == FridgeNotifications.NearFullInventory
                    && subscription.getThreshold() <= fridgeNotification.getAmmount();
            boolean malfunction_condition = fridgeNotification.getType() == FridgeNotifications.Malfunction;

            if (low_condition || full_condition || malfunction_condition) {
                if (subscription.getType() == fridgeNotification.getType()) {
                    subscription.getContributor().getContacts().forEach(value -> {
                        this.notificationsSent.add(fridgeNotification);
                        String message = fridgeNotification.getMessage();
                        value.SendNotification("Subscription alert", message);
                    });
                }
            }
        }
    }
}
