package org.grupo11.Services.Fridge;

import org.grupo11.Domain.Sensor.MovementSensorManager;
import org.grupo11.Domain.Sensor.TemperatureSensorManager;
import org.grupo11.Services.Meal;
import org.grupo11.Utils.Crypto;

public class Fridge {
    private int id;
    private boolean isActive;
    private double lon;
    private double lat;
    private String address;
    private String name;
    private int capacity;
    private int commissioningDate;
    private Meal[] meals;
    private TemperatureSensorManager tempManager;
    private MovementSensorManager movManager;

    public Fridge(double lon, double lat, String address, String name, int capacity, int commissioningDate,
            Meal[] meals,
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

    public Meal[] getMeals() {
        return this.meals;
    }

    public void setMeals(Meal[] meals) {
        this.meals = meals;
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
}
