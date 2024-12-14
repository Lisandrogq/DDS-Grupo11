package org.grupo11.Services.Fridge.Sensor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class MovementSensor {
    @Id
    @GeneratedValue
    private int id;

    private boolean data;

    public int getId() {
        return id;
    }

    public MovementSensor() {
    }

    public boolean getData() {
        return this.data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

}
