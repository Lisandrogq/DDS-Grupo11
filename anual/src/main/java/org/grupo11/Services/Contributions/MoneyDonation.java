package org.grupo11.Services.Contributions;

public class MoneyDonation extends Contribution {
    int amount;

    public MoneyDonation(int amount) {
        this.amount = amount;
    }

    public ContributionType getContributionType() {
        return ContributionType.MONEY_DONATION;
    }
}
