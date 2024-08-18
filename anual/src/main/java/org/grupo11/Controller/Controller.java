package org.grupo11.Controller;

import java.io.IOException;

import org.grupo11.Controller.DTOS.AlertDTO;
import org.grupo11.Controller.DTOS.FridgeTempDTO;
import org.grupo11.Domain.Sensor.Sensor;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgesManager;
import org.grupo11.Services.Fridge.Incident.Alert;
import org.grupo11.Services.Fridge.Incident.AlertType;
import org.grupo11.Services.Fridge.Incident.Incident;

import com.rabbitmq.client.Delivery;

public class Controller {

    public Controller() {

    }

    public static void handleTempUpdate(String consumerTag, Delivery message) throws IOException {
        System.out.println("pija de goma");
        FridgeTempDTO dto= new FridgeTempDTO(0, 0, 0);//esto se extraería del body
        Fridge fridge =FridgesManager.getInstance().getById(dto.getFridgeId());
        Sensor<Double> sensor = fridge.getTempManager().getSensorById(dto.getSensor_id());
        sensor.setData(dto.getUpdatedTemp());
    }
    
    public static void handleAlert(String consumerTag, Delivery message) throws IOException {
        System.out.println("pija de goma");
        AlertDTO dto= new AlertDTO(0,AlertType.FraudAlert,0);
        Fridge fridge =FridgesManager.getInstance().getById(dto.fridge_id);
        Incident alert = new Alert(dto.type,dto.detectedAt );
        fridge.addIncident(alert);
    }
    public static void fridges(String consumerTag, Delivery message) throws IOException {
        System.out.println("pija de goma");
    }
}