package org.grupo11.Services.Fridge;

import org.grupo11.Services.Meal;

public class Fridge {
    public boolean isActive;
    private float lon;
    private float lat;
    private String address;
    private String name;
    private int capacity;
    private int commissioningDate;
    private Meal[] meals;
    private TemperatureMonitor temp;

    public Fridge(float lon, float lat, String address, String name, int capacity, int commissioningDate, Meal[] meals,
            TemperatureMonitor temp) {
        this.lon = lon;
        this.lat = lat;
        this.address = address;
        this.name = name;
        this.capacity = capacity;
        this.commissioningDate = commissioningDate;
        this.meals = meals;
        this.temp = temp;
    }
}
