package org.grupo11.Broker;

import java.io.IOException;

import org.grupo11.DTOS.AlertDTO;
import org.grupo11.DTOS.FridgeTempDTO;
import org.grupo11.DTOS.OpeningDTO;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgesManager;
import org.grupo11.Services.Fridge.Incident.Alert;
import org.grupo11.Services.Fridge.Sensor.Sensor;
import org.grupo11.Utils.JSON;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Delivery;

public class Controller {

    public Controller() {

    }

    public static void handle_temp_update(String consumerTag, Delivery message) throws IOException {
        try {
            System.out.println("handle_temp_update");
            String json = new String(message.getBody(), "UTF-8");
            System.out.println(json);

            FridgeTempDTO dto = JSON.parse(json, new TypeReference<FridgeTempDTO>() {
            });
            Fridge fridge = FridgesManager.getInstance().getById(dto.fridge_id);
            Sensor<Double> sensor = fridge.getTempManager().getSensorById(dto.sensor_id);
            sensor.setData(dto.temp);
        } catch (Exception e) {
            System.err.println("Err while handling a temp update: " + e);
        }
    }

    public static void handle_alert(String consumerTag, Delivery message) throws IOException {
        try {
            System.out.println("handle_alert");
            String json = new String(message.getBody(), "UTF-8");
            System.out.println(json);

            AlertDTO dto = new ObjectMapper().readValue(json, AlertDTO.class);

            Fridge fridge = FridgesManager.getInstance().getById(dto.fridge_id);
            Alert alert = new Alert(dto.type, dto.detectedAt);
            fridge.addIncident(alert);

        } catch (Exception e) {
            System.err.println("Err while handling an alert: " + e);
        }
    }

    public static void handle_opening_request(String consumerTag, Delivery message) throws IOException {
        try {
            System.out.println("handle_opening_request");
            String json = new String(message.getBody(), "UTF-8");
            System.out.println(json);

            OpeningDTO dto = JSON.parse(json, new TypeReference<OpeningDTO>() {
            });
            Fridge fridge = FridgesManager.getInstance().getById(dto.fridge_id);
            boolean response = fridge.hasPermission(dto.registry_id);
            // todo: return response to fridge

        } catch (Exception e) {
            System.err.println("Err while handling an alert: " + e);
        }

    }

    public static void fridges(String consumerTag, Delivery message) throws IOException {
        System.out.println("pija de goma");
    }
}