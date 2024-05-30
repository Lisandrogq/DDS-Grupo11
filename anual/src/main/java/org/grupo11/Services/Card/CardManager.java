package org.grupo11.Services.Card;

import java.util.ArrayList;
import java.util.List;

public class CardManager {
    private List<Card> cards;

    public CardManager() {
        this.cards = new ArrayList<>();
    }

    public void add(Card card) {
        cards.add(card);
    }

    public void remove(Card card) {
        cards.remove(card);
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Card getById(int id) {
        for (Card card : cards) {
            if (card.getId() == id) {
                return card;
            }
        }
        return null;
    }
}
