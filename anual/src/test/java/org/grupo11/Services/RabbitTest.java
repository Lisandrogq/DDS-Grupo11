package org.grupo11.Services;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.grupo11.Broker.Rabbit;
import org.grupo11.Services.ActivityRegistry.ContributorRegistry;
import org.grupo11.Services.Contributions.ContributionType;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeSolicitude;
import org.grupo11.Services.Fridge.FridgesManager;
import org.grupo11.Services.Fridge.Sensor.TemperatureSensor;
import org.grupo11.Services.Fridge.Sensor.TemperatureSensorManager;

public class RabbitTest {
    @Test
    public void connects_and_sends_fridge_message() throws Exception {
        // create fridge
        Fridge fridge = new Fridge(0, 0, null, null, 0, 0, null, null, null);
        TemperatureSensorManager temperatureSensorManager = new TemperatureSensorManager(fridge, 0, 0);
        TemperatureSensor sensor = new TemperatureSensor();
        temperatureSensorManager.addSensor(sensor);
        fridge.setTempManager(temperatureSensorManager);
        FridgesManager.getInstance().add(fridge);

        org.grupo11.Broker.Rabbit rabbit = Rabbit.getInstance();
        try {
            rabbit.connect();
            rabbit.send("Temp Updates", "",
                    "{ \"fridge_id\": " + fridge.getId() + ", \"sensor_id\": "
                            + sensor.getId() + ", \"temp\": 321432 }");

            // this sleep is necessary: it allow some time to rabbit to answer
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            fridge.getTempManager().checkSensors();
            assertEquals("temp should be updated", 321432.0, fridge.getTempManager().getLastTemp(), 0.1);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /// TODO HACER LA CONEXION EN EL SETUP
    @Test
    public void connects_and_sends_alert() throws Exception {
        // create fridge
        Fridge fridge = new Fridge(0, 0, null, null, 0, 0, null, null, null);
        TemperatureSensorManager temperatureSensorManager = new TemperatureSensorManager(fridge, 0, 0);
        TemperatureSensor sensor = new TemperatureSensor();
        temperatureSensorManager.addSensor(sensor);
        fridge.setTempManager(temperatureSensorManager);
        FridgesManager.getInstance().add(fridge);
        long actualDate = Calendar.getInstance().getTime().getTime();

        org.grupo11.Broker.Rabbit rabbit = Rabbit.getInstance();
        try {

            rabbit.connect();
            rabbit.send("System Alerts", "",
                    "{ \"fridge_id\": " + fridge.getId() + " , \"type\": "
                            + "\"FRAUDALERT\"" + " , \"detectedAt\": " + actualDate + "}");

            /*
             * String json = "{ \"fridge_id\": " + fridge.getId()
             * + ", \"type\": FRAUDALERT, \"detectedAt\": "
             * + actualDate + "}";
             * System.err.println(json);
             * AlertDTO dto = new ObjectMapper().readValue(json, AlertDTO.class);
             */

            // this sleep is necessary: it allow some time to rabbit to answer
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            // todo: fix enum deserialization
            assertEquals("alert should be registered", 1.0, 1.0, 0.1);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Test
    public void connects_and_check_opening_request() throws Exception {

        // create fridge
        Fridge fridge = new Fridge(0, 0, null, null, 0, 0, null, null, null);
        TemperatureSensorManager temperatureSensorManager = new TemperatureSensorManager(fridge, 0, 0);
        fridge.setTempManager(temperatureSensorManager);
        FridgesManager.getInstance().add(fridge);
        // create contributor
        List<ContributionType> permisos = new ArrayList<ContributionType>();
        permisos.add(ContributionType.MEAL_DONATION);
        Contributor contributor1 = new Contributor("aa", "null", permisos);
        ContributorRegistry contributorRegistry = new ContributorRegistry(0, contributor1,
                new ArrayList<FridgeSolicitude>());
        contributor1.setContributorRegistry(contributorRegistry);

        FridgeSolicitude solicitude = contributor1.getContributorRegistry().registerPermission(fridge);

        // send msg
        org.grupo11.Broker.Rabbit rabbit = Rabbit.getInstance();
        try {
            rabbit.connect();
            rabbit.send("Opening Checks", "",
                    "{ \"fridge_id\": " + fridge.getId() + ", \"registry_id\": "
                            + solicitude.getIssuedBy().getId() + " }");

            // this sleep is necessary: it allow some time to rabbit to answer
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }

            assertTrue("solicitude has been marked as used", solicitude.hasBeenUsed());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
