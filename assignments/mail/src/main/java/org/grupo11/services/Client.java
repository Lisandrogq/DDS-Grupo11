package org.grupo11.services;

import org.grupo11.services.Shipping.Stop;

public class Client implements Stop {
    private String name;
    private String address;
    private String locality;
    private int postalCode;

    public Client(String name, String address, String locality, int postalCode) {
        this.name = name;
        this.address = address;
        this.locality = locality;
        this.postalCode = postalCode;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLocality() {
        return locality;
    }

    public int getPostalCode() {
        return postalCode;
    }
}
