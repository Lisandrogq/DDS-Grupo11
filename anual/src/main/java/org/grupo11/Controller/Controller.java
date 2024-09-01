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
import org.grupo11.Utils.JSON;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.Delivery;

public class Controller {

    public Controller() {

    }

    public static void handleTempUpdate(String consumerTag, Delivery message) throws IOException {
        try {
            String json = new String(message.getBody(), "UTF-8");
            FridgeTempDTO dto = JSON.parse(json, new TypeReference<FridgeTempDTO>() {
            });
            Fridge fridge = FridgesManager.getInstance().getById(dto.fridge_id);
            Sensor<Double> sensor = fridge.getTempManager().getSensorById(dto.sensor_id);
            sensor.setData(dto.temp);
        } catch (Exception e) {
            System.err.println("Err while handling temp update: " + e);
        }
    }

    public static void handleAlert(String consumerTag, Delivery message) throws IOException {
        AlertDTO dto = new AlertDTO(0, AlertType.FraudAlert, 0);
        Fridge fridge = FridgesManager.getInstance().getById(dto.fridge_id);
        Incident alert = new Alert(dto.type, dto.detectedAt);
        fridge.addIncident(alert);
    }

    public static void fridges(String consumerTag, Delivery message) throws IOException {
        System.out.println("pija de goma");
    }
}