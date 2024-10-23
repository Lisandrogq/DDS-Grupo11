package org.grupo11.Services.Fridge.Sensor;

import org.grupo11.Utils.Crypto;

public class Sensor<T> {
    private T data;
    private T prevData;// cual era el sentido de esto??
    private int sensor_id;

    public Sensor() {
        sensor_id = Crypto.getRandomId(6);
    }

    public T getData() {
        return this.data;
    }

    public int getId() {
        return this.sensor_id;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getPrevData() {
        return this.prevData;
    }

    public void setPrevData(T prev_data) {
        this.prevData = prev_data;
    }

}
