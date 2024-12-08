package org.grupo11.Services.Contributions;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class MoneyDonation extends Contribution {
    private int amount;
    private double rewardCoef = 1.0;
    private String message;

    public MoneyDonation() {
    }

    public MoneyDonation(int amount, long date, String message) {
        super(date);
        this.amount = amount;
        this.message = message;
    }

    public ContributionType getContributionType() {
        return ContributionType.MONEY_DONATION;
    }

    public String getMessage() {
        return this.message;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public double getRewardPoints() {
        return amount * 0.5;
    }

}
