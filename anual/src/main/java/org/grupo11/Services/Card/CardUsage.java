package org.grupo11.Services.Card;

import org.grupo11.Services.Fridge.Fridge;

public class CardUsage {
    Fridge fridge;
    long usedAt;

    public CardUsage(Fridge fridge, long usedAt) {
        this.fridge = fridge;
        this.usedAt = usedAt;
    }
}
