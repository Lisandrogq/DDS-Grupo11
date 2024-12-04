package org.grupo11.Services.Fridge.Sensor;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
@Entity
public class TemperatureSensor extends Sensor{
    @Id
    @GeneratedValue
    private int id;

    private double data;
    public int getId() {
        return id;
    }
    public TemperatureSensor() {
        //sensor_id = Crypto.getRandomId(6);
    }

    public double getData() {
        return this.data;
    }

    public void setData(double data) {
        this.data = data;
    }
}
