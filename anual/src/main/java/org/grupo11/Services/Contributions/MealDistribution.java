package org.grupo11.Services.Contributions;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeOpenLogEntry;
import org.grupo11.Utils.DateUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class MealDistribution extends Contribution {
    @ManyToOne
    private Fridge originFridge;
    @ManyToOne
    private Fridge destinyFridge;
    private int quantity;
    private String reason;

    public MealDistribution() {
    }

    public MealDistribution(Fridge originFridge, Fridge destinyFridge, int quantity,
            String reason,
            long distributionDate) {
        super(distributionDate);
        this.originFridge = originFridge;
        this.destinyFridge = destinyFridge;
        this.quantity = quantity;
        this.reason = reason;
    }

    @Override
    public boolean validate(Contributor contributor) {
        return (super.validate(contributor)
                && (this.originFridge.hasPermission(contributor.getContributorRegistry().getId())
                        && this.destinyFridge.hasPermission(contributor.getContributorRegistry().getId())));
    }

    @Override
    public List<FridgeOpenLogEntry> afterContribution() {
        FridgeOpenLogEntry originEntry = new FridgeOpenLogEntry(this.originFridge, DateUtils.getCurrentTimeInMs(),
                this.contributor.getContributorRegistry());
        FridgeOpenLogEntry destinyEntry = new FridgeOpenLogEntry(this.destinyFridge, DateUtils.getCurrentTimeInMs(),
                this.contributor.getContributorRegistry());
        this.originFridge.addOpenEntry(originEntry);
        this.destinyFridge.addOpenEntry(destinyEntry);
        List<FridgeOpenLogEntry> entries = new ArrayList<FridgeOpenLogEntry>();
        entries.add(originEntry);
        entries.add(destinyEntry);
        return entries;
    }

    public ContributionType getContributionType() {
        return ContributionType.MEAL_DISTRIBUTION;
    }

    public Fridge getOriginFridge() {
        return this.originFridge;
    }

    public void setOriginFridge(Fridge originFridge) {
        this.originFridge = originFridge;
    }

    public Fridge getDestinyFridge() {
        return this.destinyFridge;
    }

    public void setDestinyFridge(Fridge destinyFridge) {
        this.destinyFridge = destinyFridge;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public double getRewardPoints() {
        return 1 * quantity;
    }
}
