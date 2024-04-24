package org.grupo11.services.Shipping;

import org.grupo11.services.Employee.Postman;

public class Tramo {
    private Stop origin;
    private Stop destination;
    private Postman postman;
    private int departuredAt=0;
    private int arrivedAt=0;
    
    public Tramo(Stop origin, Stop destination, Postman postman) {
        this.origin = origin;
        this.destination = destination;
        this.postman = postman;
    }
    public void departure(){
        //poner fecha actual
    }
    public void arrive(){
        //poner fecha actual
    }
    public int getDeparturedAt(){
        return departuredAt;
    }
    public int getarrivedAt(){
        return arrivedAt;
    }

}
