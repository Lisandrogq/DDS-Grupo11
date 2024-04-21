package org.grupo11.services.Shipping;

import org.grupo11.services.Client;

enum EncomiendasTypes {
    PERSON_MADE,
    PACKETS
}

public class Encomienda extends Shipping {
    private EncomiendasTypes type;

    public Encomienda(Client sender, Client receiver, float price, Track track, EncomiendasTypes type) {
        super(sender, receiver, price, track);
        this.type = type;
    }

    // Getter for type
    public EncomiendasTypes getType() {
        return type;
    }
}