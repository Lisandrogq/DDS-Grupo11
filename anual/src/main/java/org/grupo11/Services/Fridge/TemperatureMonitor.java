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

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Contributor getLastTemp() {
        return lastTemp;
    }

    public void setLastTemp(Contributor lastTemp) {
        this.lastTemp = lastTemp;
    }

    public void onDangerousTemp() {

    }
}
