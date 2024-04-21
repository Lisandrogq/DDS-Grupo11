package org.grupo11.services.Shipping;

public class Stop {
    private String address;
    private String city;
    private int postalCode;
    private int arrivedAt; // timestamp in unix format

    public Stop(String address, String city, int postalCode, int arrivedAt) {
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.arrivedAt = arrivedAt;
    }

    // Getters
    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public int getArrivedAt() {
        return arrivedAt;
    }
}
