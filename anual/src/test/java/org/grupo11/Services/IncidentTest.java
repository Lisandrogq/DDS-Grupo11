package org.grupo11.Services;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Enums.Provinces;
import org.grupo11.Services.Contact.Contact;
import org.grupo11.Services.Contact.EmailContact;
import org.grupo11.Services.Contributions.ContributionType;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeNotifications;
import org.grupo11.Services.Fridge.Sensor.MovementSensorManager;
import org.grupo11.Services.Fridge.Sensor.TemperatureSensor;
import org.grupo11.Services.Fridge.Sensor.TemperatureSensorManager;
import org.grupo11.Services.Technician.Technician;
import org.grupo11.Services.Technician.TechnicianType;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class IncidentTest {

    TemperatureSensor sensor1 = new TemperatureSensor();
    Fridge fridge = null;
    TemperatureSensorManager tempManager = null;
    MovementSensorManager movManager = null;
    Technician technician1 = null;
    Technician technician2 = null;
    Contributor contributor1 = null;
    Contributor contributor2 = null;

    @Before
    public void setUp() {
        contributor1 = new Contributor("aa", "null", new ArrayList<ContributionType>());
        contributor2 = new Contributor("nb", "null", new ArrayList<ContributionType>());
        Contact cont_contact1 = new EmailContact("cont.shemale");
        Contact cont_contact2 = new EmailContact("cont.shemale");
        contributor1.addContact(cont_contact1);
        contributor2.addContact(cont_contact2);

        Contact tech_contact1 = new EmailContact("tech.shemale");
        Contact tech_contact2 = new EmailContact("tech.shemale");
        technician1 = new Technician("pepe", "gomez", TechnicianType.ELECTRICIAN, 123123, "null",
                "", tech_contact1);
        technician2 = new Technician("pepe", "gomez", TechnicianType.ELECTRICIAN, 123123, "null",
                "", tech_contact2);
        // TechnicianManager.getInstance().add(technician1);
        List<Meal> meals = new ArrayList<Meal>();
        fridge = new Fridge(-74.006, 40.7128, "Caballito", "Fridge A", 100, 2020, meals, null, null);
        tempManager = new TemperatureSensorManager(fridge, 2, 24);
        movManager = new MovementSensorManager(fridge);
        fridge.setArea(Provinces.CABA);
        fridge.setTempManager(tempManager);
        fridge.setMovManager(movManager);
        tempManager.addSensor(sensor1);

        contributor1.subscribeToFridge(fridge, FridgeNotifications.Malfunction, 0);
        contributor2.subscribeToFridge(fridge, FridgeNotifications.LowInventory, 10);
    }

    @Test
    public void TempIsUpdated() throws InterruptedException {
        sensor1.setData(35.0);
        tempManager.checkSensors();
        assertEquals("temp should be updated", tempManager.getLastTemp(), 35.0, 0.1);
    }

    @Test
    public void TempIsUpdatedWithMultipleSensors() throws InterruptedException {
        TemperatureSensor sensor2 = new TemperatureSensor();
        TemperatureSensor sensor3 = new TemperatureSensor();
        TemperatureSensor sensor4 = new TemperatureSensor();

        fridge.getTempManager().setMaxTemp(34);
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
    public void AlertIsGenerated() {
        fridge.getTempManager().setMaxTemp(34);
        sensor1.setData(35.0);
        tempManager.checkSensors();
        assertEquals("alert should be generated", fridge.getIncidents().size(), 1.0, 0.1);
    }

    @Test
    public void TechnicianIsAlerted() {
        fridge.getTempManager().setMaxTemp(34);
        sensor1.setData(35.0);
        tempManager.checkSensors();
        EmailContact contact1 = (EmailContact) technician1.getContact();
        assertEquals("technician1 should be alerted", contact1.getNotifications().size(), 1.0, 0.1);
    }

    @Test
    public void OnlyNearTechnicianIsAlerted() {
        fridge.getTempManager().setMaxTemp(34);
        sensor1.setData(35.0);
        tempManager.checkSensors();
        EmailContact contact1 = (EmailContact) technician1.getContact();
        EmailContact contact2 = (EmailContact) technician2.getContact();
        assertEquals("technician1 should be alerted", 1.0, contact1.getNotifications().size(), 0.1);
        assertEquals("technician2 should not be alerted", 0.0, contact2.getNotifications().size(), 0.1);
    }

    @Test
    public void IncidentGeneratesFridgeNotification() {
        fridge.getTempManager().setMaxTemp(34);
        sensor1.setData(35.0);
        tempManager.checkSensors();
        assertEquals("fridge should have registered an incident", 1.0, fridge.getIncidents().size(), 0.1);
    }

    @Test
    public void testOnlyMalFunctionSubscribersGetAlerted() {
        fridge.getTempManager().setMaxTemp(34);
        sensor1.setData(35.0);
        tempManager.checkSensors();
        EmailContact contact1 = (EmailContact) contributor1.getContacts().get(0);
        EmailContact contact2 = (EmailContact) contributor2.getContacts().get(0);
        assertEquals("contributor1 should be alerted", 1.0, contact1.getNotifications().size(), 0.1);
        assertEquals("contributor2 should not be alerted", 0.0, contact2.getNotifications().size(), 0.1);
    }

}
