package org.grupo11.Services.Fridge;

import org.grupo11.Services.Meal;
import org.grupo11.Utils.Crypto;

public class Fridge {
    private int id;
    private boolean isActive;
    private float lon;
    private float lat;
    private String address;
    private String name;
    private int capacity;
    private int commissioningDate;
    private Meal[] meals;
    private TemperatureMonitor temp;
    private MovementMonitor mov;

    public Fridge(float lon, float lat, String address, String name, int capacity, int commissioningDate, Meal[] meals,
            TemperatureMonitor temp, MovementMonitor mov) {
        this.id = Crypto.getRandomId(6);
        this.lon = lon;
        this.lat = lat;
        this.address = address;
        this.name = name;
        this.capacity = capacity;
        this.commissioningDate = commissioningDate;
        this.meals = meals;
        this.temp = temp;
        this.mov = mov;
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

    public float getLon() {
        return this.lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return this.lat;
    }

    public void setLat(float lat) {
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

    public TemperatureMonitor getTemp() {
        return this.temp;
    }

    public void setTemp(TemperatureMonitor temp) {
        this.temp = temp;
    }

    public MovementMonitor getMov() {
        return this.mov;
    }

    public void setMov(MovementMonitor mov) {
        this.mov = mov;
    }
}
