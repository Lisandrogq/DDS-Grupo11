package org.grupo11.Services.Fridge.Sensor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.grupo11.Services.Fridge.Fridge;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class SensorManager {
    @Id
    @GeneratedValue
    private int id;
    @OneToOne
    protected Fridge fridge;
    @Transient
    protected ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public int getId() {
        return id;
    }

    /**
     * @param checkSensorsEvery should be in seconds
     * @param task              callback to run on every schedule
     */
    public SensorManager(Fridge fridge, Integer checkSensorsEvery) {
        this.fridge = fridge;
        Runnable task = () -> this.checkSensors();
        scheduler.scheduleAtFixedRate(task, checkSensorsEvery, checkSensorsEvery, TimeUnit.SECONDS);
    }

    public SensorManager() {
        Runnable task = () -> this.checkSensors();
        scheduler.scheduleAtFixedRate(task, 60, 60, TimeUnit.SECONDS);
    }

    public Fridge getFridge() {
        return this.fridge;
    }

    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }

    public abstract void checkSensors();

    public abstract void fireAlert();
}
