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
    // private Stop destiny; el destino ya esta en Shipping

    public Track(ShippingState state, List<Tramo> tramos) {
        this.state = state;
        this.tramos = tramos;
    }

    // Getters
    public ShippingState getState() {
        return state;
    }

    public List<Tramo> getTramos() {
        return tramos;
    }
    public Tramo getLastTramo() {
        return tramos.get(tramos.size()-1);
    }

    public void addTramos(Tramo stop) {
        tramos.add(stop);
    }

    public Tramo nextTramo() {
       Tramo nextTramo = tramos.stream().filter((tramo->tramo.getDeparturedAt()==0)).findFirst().orElse(null);
        return nextTramo;
    }
    public Tramo actualTramo() {
        Tramo actualTramo = tramos.stream().filter((tramo->tramo.getDeparturedAt()!=0)).findFirst().orElse(null);
        return actualTramo;
    }
    public void departureNextTramo() {
        Tramo nextTramo = nextTramo();
        nextTramo.departure();
        this.state = ShippingState.IN_TRANSIT;
    }

    public void arriveTramo() {
        Tramo actualTramo= this.actualTramo(); //esto podría ser un atributo directamente
        actualTramo.arrive();
        if (this.nextTramo() == null) {
            this.state = ShippingState.DELIVERED;
        }
    }
}