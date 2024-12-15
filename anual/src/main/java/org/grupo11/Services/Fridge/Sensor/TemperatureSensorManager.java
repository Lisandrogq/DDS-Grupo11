package org.grupo11.Services.Fridge.Sensor;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeNotification;
import org.grupo11.Services.Fridge.FridgeNotifications;
import org.grupo11.Services.Fridge.Incident.Alert;
import org.grupo11.Services.Fridge.Incident.AlertType;
import org.grupo11.Services.Technician.TechnicianManager;
import org.grupo11.Utils.DateUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class TemperatureSensorManager extends SensorManager {
    @OneToMany
    protected List<TemperatureSensor> sensors = new ArrayList<>();
    private double minTemp;
    private double maxTemp;
    private double lastTemp;

    public TemperatureSensorManager(Fridge fridge, double minTemp, double maxTemp) {
        super(fridge, 60);
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    public TemperatureSensorManager() {
        super();
    }

    public double getMinTemp() {
        return this.minTemp;
    }

    public int getMinTempAsInt() {
        return (int) this.minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return this.maxTemp;
    }

    public int getMaxTempAsInt() {
        return (int) this.maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getLastTemp() {
        return this.lastTemp;
    }

    public int getLastTempAsInt() {
        return (int) this.lastTemp;
    }

    public void setLastTemp(double lastTemp) {
        this.lastTemp = lastTemp;
    }

    private double getSensorsAvgTemp(List<TemperatureSensor> sensors) {
        return sensors.stream()
                .mapToDouble(sensor -> sensor.getData())
                .average()
                .orElse(0);
    }

    private boolean checkTempBoundaries(double avgTemp) {
        Boolean isHigherThanMaxTemp = avgTemp > maxTemp;
        Boolean isLowerThanMinTemp = avgTemp < minTemp;

        return isHigherThanMaxTemp || isLowerThanMinTemp;
    }

    @Override
    public void fireAlert() {
        fridge.addIncidentAndStoreOnDB(new Alert(AlertType.TEMPERATUREALERT, DateUtils.getCurrentTimeInMs(), fridge));
    }

    public TemperatureSensor getSensorById(int id) {
        for (TemperatureSensor sensor : sensors) {
            if (sensor.getId() == id) {
                return sensor;
            }
        }
        return null;
    }

    public List<TemperatureSensor> getSensors() {
        return this.sensors;
    }

    public void setSensors(List<TemperatureSensor> sensors) {
        this.sensors = sensors;
    }

    public void addSensor(TemperatureSensor sensor) {
        sensors.add(sensor);
    }

    public void removeSensor(TemperatureSensor sensor) {
        sensors.remove(sensor);
    }

    @Override
    public void checkSensors() {
        double avgTemp = this.getSensorsAvgTemp(sensors);
        this.lastTemp = avgTemp;
        if (this.checkTempBoundaries(avgTemp)) {
            this.fireAlert();
        }
    }
}
