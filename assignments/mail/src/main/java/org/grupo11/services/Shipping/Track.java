package org.grupo11.services.Shipping;

import java.util.List;

import org.grupo11.services.Employee.Postman;

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
    private List<Stop> stops;
    private Stop destiny;
    private Postman postman;

    public Track(ShippingState state, Stop destiny, Postman postman) {
        this.state = state;
        this.destiny = destiny;
        this.postman = postman;
    }

    // Getters
    public ShippingState getState() {
        return state;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public Stop getDestiny() {
        return destiny;
    }

    public Postman getPostman() {
        return postman;
    }

    public void addStop(Stop stop) {
        stops.add(stop);
    }

    public void setState(ShippingState state) {
        // makes sense to update the postman ship from here
        // coz the shipping class is responsible for knowing the state of the deliver
        if (state == ShippingState.DELIVERED) {
            postman.finishCurrentShip();
        }
        this.state = state;
    }

}