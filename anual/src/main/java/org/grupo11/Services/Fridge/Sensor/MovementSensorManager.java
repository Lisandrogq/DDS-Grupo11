package org.grupo11.Services.Fridge.Sensor;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.Incident.Alert;
import org.grupo11.Services.Fridge.Incident.AlertType;
import org.grupo11.Utils.DateUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class MovementSensorManager extends SensorManager {
    private Boolean isMoving;
    @OneToMany
    protected List<MovementSensor> sensors = new ArrayList<>();

    public MovementSensorManager(Fridge fridge) {
        super(fridge, 7);
        this.isMoving = false;
    }

    public MovementSensorManager() {
        super();
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
        fridge.addIncidentAndStoreOnDB(new Alert(AlertType.TEMPERATUREALERT, DateUtils.getCurrentTimeInMs(), fridge));
    }

    public MovementSensor getSensorById(int id) {
        for (MovementSensor sensor : sensors) {
            if (sensor.getId() == id) {
                return sensor;
            }
        }
        return null;
    }

    public List<MovementSensor> getSensors() {
        return this.sensors;
    }

    public void setSensors(List<MovementSensor> sensors) {
        this.sensors = sensors;
    }

    public void addSensor(MovementSensor sensor) {
        sensors.add(sensor);
    }

    public void removeSensor(MovementSensor sensor) {
        sensors.remove(sensor);
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
