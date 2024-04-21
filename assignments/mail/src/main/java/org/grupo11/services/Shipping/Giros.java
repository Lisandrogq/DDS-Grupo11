package org.grupo11.services.Shipping;

import org.grupo11.services.Client;

// no idea whats the english for Giros :)
public class Giros extends Shipping {
    private float amount;

    public Giros(Client sender, Client receiver, float price, Track track, float amount) {
        super(sender, receiver, price, track);
        this.amount = amount;
    }

    // Getter for amount
    public float getAmount() {
        return amount;
    }
}
