package org.grupo11.Services.Fridge.Sensor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class MovementSensor {
    @Id
    private int id;
    @ManyToOne
    private MovementSensorManager manager;

    public MovementSensor() {
    }

    private boolean data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setManager(MovementSensorManager manager) {
        this.manager = manager;
    }

    public boolean getData() {
        return this.data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
}
