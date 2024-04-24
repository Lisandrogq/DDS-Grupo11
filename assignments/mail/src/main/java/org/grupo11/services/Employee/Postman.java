package org.grupo11.services.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.grupo11.services.Shipping.Shipping;

public class Postman extends Employee {
    private List<Shipping> shippings;
    private Shipping currentShip;

    public Postman(String name, String address, String phoneNumber) {
        super(name, address, phoneNumber);
        this.shippings = new ArrayList<Shipping>();
    };

   
    public void updateCurrentShip(){
        //todo: el orden de los envíos debería estar determinado de antemano y simplemente agarraría el siguiente en la lista
        currentShip = this.shippings.get(0);//demo
    }
    public void startShip() {
        currentShip.departure();
    }
    public void finishCurrentShip() {
        currentShip.arrive();
        currentShip = null;
    }

    public void addShipping(Shipping shipping) {
        shippings.add(shipping);
    }

    // Getters
    public List<Shipping> getShippings() {
        return shippings;
    }

    public Shipping getCurrentShip() {
        return currentShip;
    }
}