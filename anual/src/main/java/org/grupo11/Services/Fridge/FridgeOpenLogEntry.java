package org.grupo11.Services.Fridge;

import org.grupo11.Services.ActivityRegistry.ActivityRegistry;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class FridgeOpenLogEntry {
    @Id
    @GeneratedValue
    private Long id;
    public long openedAt;
    @ManyToOne
    private Fridge fridge;

    public FridgeOpenLogEntry() {
    }

    public FridgeOpenLogEntry(Fridge fridge, long openedAt, ActivityRegistry activityRegistry) {
        this.openedAt = openedAt;
        this.fridge = fridge;
    }
}
