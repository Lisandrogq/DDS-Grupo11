package org.grupo11.Services.Fridge.Sensor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class TemperatureSensor {
    @Id
    @GeneratedValue
    private int id;
    private double data;
    @ManyToOne
    private TemperatureSensorManager manager;

    public int getId() {
        return id;
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
