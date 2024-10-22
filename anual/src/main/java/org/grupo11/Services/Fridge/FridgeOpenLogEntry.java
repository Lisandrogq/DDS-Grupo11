package org.grupo11.Services.Fridge;

import org.grupo11.Services.ActivityRegistry.ActivityRegistry;

import jakarta.persistence.Entity;

@Entity
public class FridgeOpenLogEntry {
    public long openedAt;

    public FridgeOpenLogEntry(long openedAt, ActivityRegistry activityRegistry) {
        this.openedAt = openedAt;
    }
}
