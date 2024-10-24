package org.grupo11.Services.ActivityRegistry;

import org.grupo11.Services.Fridge.Fridge;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class CardUsage {
    @Id
    @GeneratedValue
    private Long id;

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
