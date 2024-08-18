package org.grupo11.Services.Fridge;

import org.grupo11.Services.ActivityRegistry.ActivityRegistry;

public class FridgeOpenLogEntry {
    public long openedAt;
    public ActivityRegistry openedBy;//PRINCIPALMENTE SE DEBEN ACEPTAR TARJETAS DE PINs, capaz tambien de Contributors

    public FridgeOpenLogEntry(long openedAt, ActivityRegistry openedBy) {
        this.openedAt = openedAt;
        this.openedBy = openedBy;
    }
}
