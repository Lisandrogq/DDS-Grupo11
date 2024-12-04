package org.grupo11.Services.Fridge.Sensor;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeNotification;
import org.grupo11.Services.Fridge.FridgeNotifications;
import org.grupo11.Services.Fridge.Incident.Alert;
import org.grupo11.Services.Fridge.Incident.AlertType;
import org.grupo11.Services.Technician.Technician;
import org.grupo11.Services.Technician.TechnicianManager;
import org.grupo11.Utils.DateUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
        fridge.addIncident(new Alert(AlertType.FRAUDALERT, DateUtils.getCurrentTimeInMs()));
        Technician selected = TechnicianManager.getInstance().selectTechnician(fridge);
        // send a message to the subscribers
        fridge.sendFridgeNotifications(
                new FridgeNotification(FridgeNotifications.Malfunction, "Fridge is malfunctioning",
                        "The fridge is moving, meals should be redistributed in brevity."));
    }

    public Sensor getSensorById(int id) {
        for (Sensor sensor : sensors) {
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