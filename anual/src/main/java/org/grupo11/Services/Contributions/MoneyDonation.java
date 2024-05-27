package org.grupo11.Services.Contributions;

public class MoneyDonation extends Contribution {
    private int amount;

    public MoneyDonation(int amount) {
        this.amount = amount;
    }

    public ContributionType getContributionType() {
        return ContributionType.MONEY_DONATION;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
