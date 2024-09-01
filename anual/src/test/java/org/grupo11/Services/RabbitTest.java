package org.grupo11.Services;

import org.junit.Test;

import org.grupo11.Rabbit;
import org.grupo11.Domain.Sensor.Sensor;
import org.grupo11.Domain.Sensor.TemperatureSensorManager;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgesManager;

public class RabbitTest {
    @Test
    public void connects_and_sends_fridge_message() {
        // create fridge
        Fridge fridge = new Fridge(0, 0, null, null, 0, 0, null, null, null);
        TemperatureSensorManager temperatureSensorManager = new TemperatureSensorManager(fridge, 0, 0);
        Sensor<Double> sensor = new Sensor<Double>();
        temperatureSensorManager.addSensor(sensor);
        fridge.setTempManager(temperatureSensorManager);
        FridgesManager.getInstance().add(fridge);
        // assert should be 0 at the beginning
        assert (fridge.getTempManager().getLastTemp() == 0);

        org.grupo11.Rabbit rabbit = Rabbit.getInstance();
        rabbit.connect();
        rabbit.send("System alerts", "",
                "{ \"fridge_id\": " + fridge.getId() + ", \"sensor_id\": "
                        + sensor.getId() + ", \"temp\": 321432 }");

        // this sleep is necessary: it allow some time to rabbit to answer
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        fridge.getTempManager().checkSensors();

        assert (fridge.getTempManager().getLastTemp() == 321432.0);
    }
}
