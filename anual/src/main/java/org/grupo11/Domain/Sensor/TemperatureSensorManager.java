package org.grupo11.Domain.Sensor;

import java.util.List;

import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.Incident.Alert;
import org.grupo11.Services.Fridge.Incident.AlertType;
import org.grupo11.Services.Technician.Technician;
import org.grupo11.Services.Technician.TechnicianManager;
import org.grupo11.Services.Technician.TechnicianType;
import org.grupo11.Utils.DateUtils;

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
        fridge.addIncident(new Alert(AlertType.TemperatureAlert, DateUtils.getCurrentTimeInMs()));
        // send a message to all the technicians so one responds
        for (Technician technician : TechnicianManager.getInstance().getTechnicians()) {
            if (technician.getAreasOfWork() == fridge.getArea() && technician.getType() == TechnicianType.ELECTRICIAN) {
                technician.getContact().SendNotification("WE NEED YOU",
                        "We need you to fix a fridge");
            }
        }
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
