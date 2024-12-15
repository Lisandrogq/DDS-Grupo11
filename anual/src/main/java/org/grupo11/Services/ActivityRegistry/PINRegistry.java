package org.grupo11.Services.ActivityRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.PersonInNeed.PersonInNeed;
import org.grupo11.Utils.Crypto;
import org.grupo11.Utils.DateUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class PINRegistry extends ActivityRegistry {
    @OneToOne
    private PersonInNeed owner;
    @OneToMany(cascade = CascadeType.ALL)
    private List<CardUsage> usages;
    @ManyToOne
    private Contributor givenBy;
    private String number;
    private String securityCode;

    public PINRegistry() {
    }

    public PINRegistry(PersonInNeed owner, Contributor givenBy) {
        this.owner = owner;
        this.usages = new ArrayList<>();
        this.givenBy = givenBy;
        this.number = Crypto.getRandomIdAsString(10);
        this.securityCode = Crypto.getRandomIdAsString(5);
    }

    public PINRegistry(PersonInNeed owner, Contributor givenBy, List<CardUsage> usages, String number,
            String securityCode) {
        this.usages = usages;
        this.givenBy = givenBy;
        this.number = number;
        this.securityCode = securityCode;
    }

    public boolean canUseCard() {
        int todaysUsages = getTodayUsages().size();
        if (todaysUsages > getMaxDailyUse())
            return false;
        return true;
    }

    public boolean canUseCardByQuantity(int quantity) {
        int todaysUsages = getTodayUsages().size();
        if (todaysUsages + quantity > getMaxDailyUse())
            return false;
        return true;
    }

    public int getUsagesLeftForToday() {
        return this.getMaxDailyUse() - this.getTodayUsages().size();
    }

    public int getMaxDailyUse() {
        return owner.getChildCount() * 2 + 4;
    }

    public void useCard(Fridge fridge) {
        if (this.canUseCard())
            usages.add(new CardUsage(this, fridge, DateUtils.now()));
    }

    public List<CardUsage> getTodayUsages() {
        return usages.stream().filter(usage -> DateUtils.isSameDay(usage.getUsedAt(), DateUtils.now()))
                .collect(Collectors.toList());
    }

    // getters and setters
    public PersonInNeed getOwner() {
        return this.owner;
    }

    public void setOwner(PersonInNeed owner) {
        this.owner = owner;
    }

    public String getNumber() {
        return this.number;
    }

    public String getSecurityCode() {
        return this.securityCode;
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

    public PINRegistry clone() {
        List<CardUsage> clonedUsages = new ArrayList<>(this.usages);
        return new PINRegistry(this.owner, this.givenBy, clonedUsages, this.number, this.securityCode);
    }
}
