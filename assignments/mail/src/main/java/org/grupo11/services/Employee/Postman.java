package org.grupo11.services.Employee;

import java.util.List;
import java.util.Optional;

import org.grupo11.services.Shipping.Shipping;

public class Postman extends Employee {
    private List<Shipping> shippings;
    private Optional<Shipping> currentShip;

    public Postman(String name, String address, String phoneNumber) {
        super(name, address, phoneNumber);
    };

    public void finishCurrentShip() {
        currentShip.ifPresent(this::addShipping);
        this.updateCurrentShip(null);
    }

    public void updateCurrentShip(Optional<Shipping> shipping) {
        currentShip = shipping;
    }

    public void addShipping(Shipping shipping) {
        shippings.add(shipping);
    }

    // Getters
    public List<Shipping> getShippings() {
        return shippings;
    }

    public Optional<Shipping> getCurrentShip() {
        return currentShip;
    }
}