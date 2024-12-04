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
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class SensorManager {
/*     @OneToMany
    protected List<Sensor> sensors; */
    @OneToOne
    protected Fridge fridge;
    @Transient
    protected ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    @Id
    @GeneratedValue
    private int id;
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


    public Fridge getFridge() {
        return this.fridge;
    }

    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }


    public abstract void checkSensors();

    public abstract void fireAlert();
}
