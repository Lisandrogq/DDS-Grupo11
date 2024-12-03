package org.grupo11.Services.Fridge.Sensor;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Sensor {
    @Id
    @GeneratedValue
    private int id;

    public int getId() {
        return id;
    }
}
