package org.grupo11.Services.Fridge;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class FridgeNotification {
    @Id
    @GeneratedValue
    private Long id;
    private int ammount;
    private String message;
    @Enumerated(EnumType.STRING)
    private FridgeNotifications type;
    @ManyToOne
    private Fridge fridge;

    public FridgeNotification() {
    }

    public FridgeNotification(FridgeNotifications type, int ammount, String message) {
        this.ammount = ammount;
        this.message = message;
        this.type = type;
    }

    public FridgeNotification(Fridge fridge, FridgeNotifications type, int ammount, String message) {
        this.fridge = fridge;
        this.ammount = ammount;
        this.message = message;
        this.type = type;
    }

    public int getAmmount() {
        return this.ammount;
    }

    public String getMessage() {
        return this.message;
    }

    public FridgeNotifications getType() {
        return this.type;
    }
}
