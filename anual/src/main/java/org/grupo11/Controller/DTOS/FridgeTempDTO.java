package org.grupo11.Controller.DTOS;

public class FridgeTempDTO {
    public int fridge_id;
    public int sensor_id;
    public double temp;

    public FridgeTempDTO(int fridge_id, int sensor_id, double temp) {
        this.fridge_id = fridge_id;
        this.sensor_id = sensor_id;
        this.temp = temp;
    }
}
