package org.grupo11.Domain.Sensor;

import java.util.List;

import org.grupo11.Services.Fridge.Fridge;

public class TemperatureSensorManager extends SensorManager<Double> {
    private double minTemp;
    private double maxTemp;
    private double lastTemp;

    public TemperatureSensorManager(Fridge fridge, double minTemp, double maxTemp) {
        super(fridge, 60);
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    public double getMinTemp() {
        return this.minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return this.maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getLastTemp() {
        return this.lastTemp;
    }

    public void setLastTemp(double lastTemp) {
        this.lastTemp = lastTemp;
    }

    private double getSensorsAvgTemp(List<Sensor<Double>> sensors) {
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
        // maybe send an email here...
        System.out.println("fridge temperature is out of bounds!");
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
