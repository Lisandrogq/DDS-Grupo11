package org.grupo11.Services;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Config.Env;
import org.grupo11.Domain.Sensor.MovementSensorManager;
import org.grupo11.Domain.Sensor.Sensor;
import org.grupo11.Domain.Sensor.TemperatureSensorManager;
import org.grupo11.Enums.Provinces;
import org.grupo11.Services.Contact.Contact;
import org.grupo11.Services.Contact.EmailContact;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeMapper;
import org.grupo11.Services.Technician.Technician;
import org.grupo11.Services.Technician.TechnicianManager;
import org.grupo11.Services.Technician.TechnicianType;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class IncidentTest {

    private List<Fridge> fridges;
    Sensor<Double> sensor1 = new Sensor<Double>();
    Fridge fridge = null;
    TemperatureSensorManager tempManager = null;
    MovementSensorManager movManager = null;
    Technician technician1 = null;
    Technician technician2 = null;

    @Before
    public void setUp() {
        Contact contact1 = new EmailContact("tech.shemale");
        Contact contact2 = new EmailContact("tech.shemale");
        technician1 = new Technician("pepe", "gomez", TechnicianType.ELECTRICIAN, 123123, "null",
                Provinces.CABA, contact1);
        technician2 = new Technician("pepe", "gomez", TechnicianType.ELECTRICIAN, 123123, "null",
                Provinces.ZonaSur, contact2);
        TechnicianManager.getInstance().add(technician1);
        List<Meal> meals = new ArrayList<Meal>();
        fridge = new Fridge(-74.006, 40.7128, "Caballito", "Fridge A", 100, 2020, meals, null, null);
        tempManager = new TemperatureSensorManager(fridge, 2, 24);
        movManager = new MovementSensorManager(fridge);
        fridge.setArea(Provinces.CABA);
        fridge.setTempManager(tempManager);
        fridge.setMovManager(movManager);
        tempManager.addSensor(sensor1);
    }

    @Test
    public void testTempIsUpdated() throws InterruptedException {
        sensor1.setData(35.0);
        tempManager.checkSensors();
        assertEquals("temp should be updated", tempManager.getLastTemp(), 35.0, 0.1);
    }

    @Test
    public void testTempIsUpdatedWithMultipleSensors() throws InterruptedException {
        Sensor<Double> sensor2 = new Sensor<Double>();
        Sensor<Double> sensor3 = new Sensor<Double>();
        Sensor<Double> sensor4 = new Sensor<Double>();
        sensor1.setData(35.0);
        sensor2.setData(36.0);
        sensor3.setData(37.0);
        sensor4.setData(38.0);

        tempManager.addSensor(sensor2);
        tempManager.addSensor(sensor3);
        tempManager.addSensor(sensor4);

        tempManager.checkSensors();
        assertEquals("temp should be the average of the 4 sensors", 36.5, tempManager.getLastTemp(), 0.1);
    }

    @Test
    public void testTechnicianIsAlerted() {
        fridge.getTempManager().setMaxTemp(34);
        sensor1.setData(35.0);
        tempManager.checkSensors();
        EmailContact contact1= (EmailContact) technician1.getContact();
        assertEquals("technician1 should be alerted", contact1.getNotifications().size(), 1.0, 0.1);
    }

    @Test
    public void testOnlyNearTechnicianIsAlerted() {
        fridge.getTempManager().setMaxTemp(34);
        sensor1.setData(35.0);
        tempManager.checkSensors();
        EmailContact contact1= (EmailContact) technician1.getContact();
        EmailContact contact2= (EmailContact) technician2.getContact();
        assertEquals("technician1 should be alerted",  1.0,contact1.getNotifications().size(), 0.1);
        assertEquals("technician2 should not be alerted", 0.0,contact2.getNotifications().size(), 0.1);
    }

}
