package org.grupo11.services.Shipping;

import org.grupo11.services.Employee.Postman;

public class Tramo {
    private Stop origin;
    private Stop destination;
    private Postman postman;
    
    public Tramo(Stop origin, Stop destination, Postman postman) {
        this.origin = origin;
        this.destination = destination;
        this.postman = postman;
    }

}
