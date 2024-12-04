package org.grupo11.Services.Fridge.Sensor;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class MovementSensor extends Sensor{
    @Id
    @GeneratedValue
    private int id;

    private boolean data;
    public int getId() {
        return id;
    }
    public MovementSensor() {
        //sensor_id = Crypto.getRandomId(6);
    }

    public boolean getData() {
        return this.data;
    }

    public void setData(boolean data) {
        this.data = data;
    }


}
