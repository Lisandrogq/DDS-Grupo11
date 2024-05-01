package org.grupo11.Services;

class MoneyDonationArgs {
    int amount;
}

public class MoneyDonation extends Contribution {
    int amount;

    public MoneyDonation(MoneyDonationArgs args) {
        this.amount = args.amount;
    }

    @Override
    protected MoneyDonation contribute(Contributor c) {
        return this;
    }

    @Override
    protected boolean validate(Contributor contributor) {
        return true;
    }
}
