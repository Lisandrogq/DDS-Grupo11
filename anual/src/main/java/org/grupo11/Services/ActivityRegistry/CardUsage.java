package org.grupo11.Services.ActivityRegistry;

import org.grupo11.Services.Fridge.Fridge;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class CardUsage {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Fridge fridge;
    private long usedAt;
    @ManyToOne
    private PINRegistry pinRegistry;

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
