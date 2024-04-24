package org.grupo11.services.Employee;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.services.Shipping.Shipping;

public class Postman extends Employee {
    private List<Shipping> shippings;
    private Shipping currentShip;
    private int nextShippingIndex=0;
    public Postman(String name, String address, String phoneNumber) {
        super(name, address, phoneNumber);
        this.shippings = new ArrayList<Shipping>();
    };

   
    public void updateCurrentShip(){
        currentShip = this.shippings.get(nextShippingIndex);
        nextShippingIndex+=1;
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