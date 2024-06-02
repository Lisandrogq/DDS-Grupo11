package org.grupo11.Domain.Sensor;

import org.grupo11.Services.Fridge.Fridge;

public abstract class MovementSensorManager extends SensorManager<Boolean> {
    private Boolean isMoving;

    public MovementSensorManager(Fridge fridge) {
        super(fridge, 60);
        this.isMoving = false;

    }

    public Boolean isIsMoving() {
        return this.isMoving;
    }

    public Boolean getIsMoving() {
        return this.isMoving;
    }

    public void setIsMoving(Boolean isMoving) {
        this.isMoving = isMoving;
    }

    @Override
    public void fireAlert() {
        // maybe send an email here...
        System.out.println("fridge is moving!");
    }

    @Override
    public void checkSensors() {
        if (this.sensors.stream().anyMatch(a -> a.getData())) {
            this.isMoving = true;
            this.fireAlert();
        } else
            this.isMoving = false;
    }
}