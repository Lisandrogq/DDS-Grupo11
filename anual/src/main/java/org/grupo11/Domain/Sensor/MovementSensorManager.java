package org.grupo11.Domain.Sensor;

import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeNotification;
import org.grupo11.Services.Fridge.FridgeNotifications;
import org.grupo11.Services.Fridge.Incident.Alert;
import org.grupo11.Services.Fridge.Incident.AlertType;
import org.grupo11.Services.Technician.Technician;
import org.grupo11.Services.Technician.TechnicianManager;
import org.grupo11.Services.Technician.TechnicianType;
import org.grupo11.Utils.DateUtils;

public class MovementSensorManager extends SensorManager<Boolean> {
    private Boolean isMoving;

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
        fridge.addIncident(new Alert(AlertType.FraudAlert, DateUtils.getCurrentTimeInMs()));
        for (Technician technician : TechnicianManager.getInstance().getTechnicians()) {
            if (technician.getAreasOfWork() == fridge.getArea() && technician.getType() == TechnicianType.ELECTRICIAN) {
                technician.getContact().SendNotification("WE NEED YOU",
                        "We need you to fix a fridge");
            }
        }
        // send a message to the subscribers
        fridge.sendFridgeNotifications(
                new FridgeNotification(FridgeNotifications.Malfunction, "Fridge is malfunctioning",
                        "The fridge is moving, meals should be redistributed in brevity."));
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