package org.grupo11.services.Shipping;

import org.grupo11.services.Client;

enum Sellado {
    RED,
    BLACK
}

enum LetterType {
    SIMPLE,
    CERTIFIED,
    EXPRESS
}

public class Letter extends Shipping {
    private Sellado sellado;
    private LetterType type;

    public Letter(Client sender, Client receiver, float price, Track track, Sellado sellado, LetterType type) {
        super(sender, receiver, price, track);
        this.sellado = sellado;
        this.type = type;
    }

    // Getters
    public Sellado getSellado() {
        return sellado;
    }

    public LetterType getType() {
        return type;
    }
}
