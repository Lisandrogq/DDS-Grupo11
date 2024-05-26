package org.grupo11.Services.Card;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.PersonInNeed;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Utils.Crypto;

public class Card {
    int id;
    PersonInNeed owner;
    List<CardUsage> usages;
    int maxUses;
    Contributor givenBy;

    public Card(PersonInNeed owner, Contributor givenBy) {
        id = Crypto.getRandomId(11);
        this.usages = new ArrayList<>();
        this.givenBy = givenBy;
    }

    public void canUseCard() {
    }
}
