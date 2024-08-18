package org.grupo11.Controller.DTOS;

import java.util.List;

public class FridgeTempDTO {
    private int fridge_id;
    private int sensor_id;
    private double temp;

    public FridgeTempDTO(int fridge_id, int sensor_id, double temp) {
        this.fridge_id = fridge_id;
        this.sensor_id = sensor_id;
        this.temp = temp;
    }
    public int getFridgeId(){
        return fridge_id;
    }

    public int getSensor_id(){
        return sensor_id;
    }
    public Double getUpdatedTemp(){
        return temp;
    }
}
