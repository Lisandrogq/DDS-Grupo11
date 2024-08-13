package org.grupo11.Services.Fridge;

import org.grupo11.Services.Contributor.ContributorCard;

public class FridgeOpenLogEntry {
    public long openedAt;
    public ContributorCard openedBy;//PRINCIPALMENTE SE DEBEN ACEPTAR TARJETAS DE PINs, capaz tambien de Contributors

    public FridgeOpenLogEntry(long openedAt, ContributorCard openedBy) {
        this.openedAt = openedAt;
        this.openedBy = openedBy;
    }
}
