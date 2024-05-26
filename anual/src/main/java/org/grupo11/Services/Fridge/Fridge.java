package org.grupo11.Services.Fridge;

import org.grupo11.Services.Meal;

public class Fridge {
    public boolean isActive;
    public float lon;
    public float lat;
    public String address;
    public String name;
    public int capacity;
    public int commissioningDate;
    public Meal[] meals;
    public TemperatureMonitor temp;
    public MovementMonitor mov;

    public Fridge(float lon, float lat, String address, String name, int capacity, int commissioningDate, Meal[] meals,
            TemperatureMonitor temp, MovementMonitor mov) {
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
}
