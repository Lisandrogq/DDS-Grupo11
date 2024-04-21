package org.grupo11.services.Shipping;

import org.grupo11.services.Client;

enum TelegramType {
    CARTA_DOCUMENTO,
    EVENTO,
    INVITACION
}

public class Telegram extends Shipping {
    private String text;
    private TelegramType type;

    public Telegram(Client sender, Client receiver, float price, Track track, String text, TelegramType type) {
        super(sender, receiver, price, track);
        this.text = text;
        this.type = type;
    }

    // Getters
    public String getText() {
        return text;
    }

    public TelegramType getType() {
        return type;
    }
}
