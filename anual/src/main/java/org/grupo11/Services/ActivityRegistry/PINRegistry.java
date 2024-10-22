package org.grupo11.Services.ActivityRegistry;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.PersonInNeed.PersonInNeed;
import org.grupo11.Utils.Crypto;
import org.grupo11.Utils.DateUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class PINRegistry extends ActivityRegistry {
    private int id;
    @OneToOne
    private PersonInNeed owner;
    @OneToMany
    private List<CardUsage> usages;
    @ManyToOne
    private Contributor givenBy;

    public PINRegistry(PersonInNeed owner, Contributor givenBy) {
        id = Crypto.getRandomId(11);
        this.usages = new ArrayList<>();
        this.givenBy = givenBy;
    }

    public boolean canUseCard() {
        long todaysUsages = usages.stream().filter(usage -> DateUtils.isSameDay(usage.getUsedAt(), DateUtils.now()))
                .count();
        if (todaysUsages >= getMaxDailyUse())
            return false;
        return true;
    }

    public int getMaxDailyUse() {
        return owner.getChildCount() * 2 + 4;
    }

    public void useCard(Fridge fridge) {
        if (this.canUseCard())
            usages.add(new CardUsage(fridge, DateUtils.now()));
    }

    // getters and setters
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PersonInNeed getOwner() {
        return this.owner;
    }

    public void setOwner(PersonInNeed owner) {
        this.owner = owner;
    }

    public List<CardUsage> getUsages() {
        return this.usages;
    }

    public void setUsages(List<CardUsage> usages) {
        this.usages = usages;
    }

    public Contributor getGivenBy() {
        return this.givenBy;
    }

    public void setGivenBy(Contributor givenBy) {
        this.givenBy = givenBy;
    }

}
