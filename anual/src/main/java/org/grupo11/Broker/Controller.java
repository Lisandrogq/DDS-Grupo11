package org.grupo11.Broker;

import java.io.IOException;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.DTOS.FridgeMovementDTO;
import org.grupo11.DTOS.FridgeTempDTO;
import org.grupo11.Services.Fridge.FridgesManager;
import org.grupo11.Services.Fridge.Sensor.MovementSensor;
import org.grupo11.Services.Fridge.Sensor.MovementSensorManager;
import org.grupo11.Services.Fridge.Sensor.TemperatureSensor;
import org.grupo11.Services.Fridge.Sensor.TemperatureSensorManager;
import org.grupo11.Utils.JSON;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.Delivery;

public class Controller {
    public Controller() {
    }

    public static void handleTempUpdate(String consumerTag, Delivery message) throws IOException {
        try {
            Logger.info("Handling temperature update msg");
            String json = new String(message.getBody(), "UTF-8");
            FridgeTempDTO dto = JSON.parse(json, new TypeReference<FridgeTempDTO>() {
            });
            TemperatureSensorManager sensorManager = FridgesManager.getInstance()
                    .queryTemperatureManagerFromFridgeId(dto.fridge_id);
            TemperatureSensor sensor = sensorManager.getSensorById(dto.sensor_id);
            // if sensor does not exist, create it
            if (sensor == null) {
                sensor = new TemperatureSensor();
                sensor.setId(dto.sensor_id);
                sensorManager.addSensor(sensor);
                DB.create(sensor);
            }
            sensor.setData(dto.temp);
            sensorManager.checkSensors();
            DB.update(sensorManager);
        } catch (Exception e) {
            Logger.error("Err while handling a temp update ", e);
        }
    }

    public static void handleMovementDetected(String consumerTag, Delivery message) throws IOException {
        try {
            Logger.info("Handling movement update msg");
            String json = new String(message.getBody(), "UTF-8");
            FridgeMovementDTO dto = JSON.parse(json, new TypeReference<FridgeMovementDTO>() {
            });
            MovementSensorManager sensorManager = FridgesManager.getInstance()
                    .queryMovementManagerFromFridgeId(dto.fridge_id);
            MovementSensor sensor = sensorManager.getSensorById(dto.sensor_id);
            // if sensor does not exist, create it
            if (sensor == null) {
                sensor = new MovementSensor();
                sensor.setId(dto.sensor_id);
                sensorManager.addSensor(sensor);
                DB.create(sensor);
            }
            sensor.setData(dto.is_moving);
            sensorManager.checkSensors();
            DB.update(sensorManager);
        } catch (Exception e) {
            Logger.error("Err while handling a movement update ", e);
        }
    }
}
