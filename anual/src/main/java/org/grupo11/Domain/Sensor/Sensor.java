package org.grupo11.Domain.Sensor;

public class Sensor<T> {
    private T data;
    private T prevData;

    public T getData() {
        return this.data;
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
