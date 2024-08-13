package org.grupo11.Services.ActivityRegistry;

import org.grupo11.Services.Fridge.Fridge;

public class CardUsage {
    private Fridge fridge;
    private long usedAt;

    public CardUsage(Fridge fridge, long usedAt) {
        this.fridge = fridge;
        this.usedAt = usedAt;
    }

    public Fridge getFridge() {
        return this.fridge;
    }

    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }

    public long getUsedAt() {
        return this.usedAt;
    }

    public void setUsedAt(long usedAt) {
        this.usedAt = usedAt;
    }

}
