package org.grupo11.Services.Fridge;

import org.grupo11.Services.ActivityRegistry.ActivityRegistry;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class FridgeOpenLogEntry {
    @Id
    @GeneratedValue
    private Long id;
    public long openedAt;

    public FridgeOpenLogEntry() {
    }

    public FridgeOpenLogEntry(long openedAt, ActivityRegistry activityRegistry) {
        this.openedAt = openedAt;
    }
}
