package org.grupo11.Services.Card;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.PersonInNeed.PersonInNeed;
import org.grupo11.Utils.Crypto;
import org.grupo11.Utils.Date;

public class Card {
    int id;
    PersonInNeed owner;
    List<CardUsage> usages;
    int maxDailyUse;
    Contributor givenBy;

    public Card(PersonInNeed owner, Contributor givenBy) {
        id = Crypto.getRandomId(11);
        this.usages = new ArrayList<>();
        this.givenBy = givenBy;
    }

    public boolean canUseCard() {
        long todaysUsages = usages.stream().filter(usage -> Date.isSameDay(usage.usedAt, Date.now())).count();
        if (todaysUsages >= getMaxDailyUse())
            return false;
        return true;
    }

    public int getMaxDailyUse() {
        return owner.childCount * 2 + 4;
    }

    public void useCard(Fridge fridge) {
        if (this.canUseCard())
            usages.add(new CardUsage(fridge, Date.now()));
    }
}
