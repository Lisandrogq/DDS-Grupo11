package org.grupo11.services.Shipping;

import java.util.List;

public class Track {
    public static enum ShippingState {
        IN_TRANSIT,
        PENDING,
        CANCELED,
        DELIVERED,
    }

    private ShippingState state;
    // don't quite understand the stops type
    // should they be a branch themselves or an arbitrary place??
    private List<Tramo> tramos;
    //private Stop destiny; el destino ya esta en Shipping


    public Track(ShippingState state,List<Tramo> tramos) {
        this.state = state;
        this.tramos= tramos;
    }

    // Getters
    public ShippingState getState() {
        return state;
    }

    public List<Tramo> getTramos() {
        return tramos;
    }

    public void addTramos(Tramo stop) {
        tramos.add(stop);
    }

}