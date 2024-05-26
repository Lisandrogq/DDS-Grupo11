package org.grupo11.Services.Fridge;

import org.grupo11.Services.Contributor.Contributor;

public class TemperatureMonitor {
    public int minTemp;
    public int maxTemp;
    private Contributor lastTemp;

    public TemperatureMonitor(int minTemp, int maxTemp, Contributor lastTemp) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.lastTemp = lastTemp;
    }

    public void SetMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public void SetMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Contributor GetLastTemp() {
        return lastTemp;
    }

    public void SetLastTemp(Contributor lastTemp) {
        this.lastTemp = lastTemp;
    }

    public void OnDangerousTemp() {
        // Method implementation for handling dangerous temperature
    }

}
