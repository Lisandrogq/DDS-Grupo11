package org.grupo11.Services.Fridge.Sensor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class TemperatureSensor {
    @Id
    private int id;
    private double data;
    @ManyToOne
    private TemperatureSensorManager manager;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setManager(TemperatureSensorManager manager) {
        this.manager = manager;
    }

    public TemperatureSensor() {
    }

    public double getData() {
        return this.data;
    }

    public void setData(double data) {
        this.data = data;
    }
}
