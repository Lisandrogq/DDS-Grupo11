package org.grupo11.Domain.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.grupo11.Services.Fridge.Fridge;

public abstract class SensorManager<T> {
    protected List<Sensor<T>> sensors;
    protected Fridge fridge;
    protected ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * @param checkSensorsEvery should be in seconds
     * @param task              callback to run on every schedule
     */
    public SensorManager(Fridge fridge, Integer checkSensorsEvery) {
        this.fridge = fridge;
        sensors = new ArrayList<>();
        Runnable task = () -> this.checkSensors();
        scheduler.scheduleAtFixedRate(task, checkSensorsEvery, checkSensorsEvery, TimeUnit.SECONDS);
    }

    public List<Sensor<T>> getSensors() {
        return this.sensors;
    }

    public void setSensors(List<Sensor<T>> sensors) {
        this.sensors = sensors;
    }

    public Fridge getFridge() {
        return this.fridge;
    }

    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }

    public void addSensor(Sensor<T> sensor) {
        sensors.add(sensor);
    }

    public void removeSensor(Sensor<T> sensor) {
        sensors.add(sensor);
    }

    public abstract void checkSensors();

    public abstract void fireAlert();
}
