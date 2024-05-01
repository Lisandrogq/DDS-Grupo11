package org.grupo11.Services;

import java.util.List;

public class Contributor {
    List<Contribution> contributions;

    public void Contribute(MoneyDonationArgs args) {
        new MoneyDonation(args).tryContribute(this);
    }

    public void addContribution(Contribution contribution) {
        contributions.add(contribution);
    }
}
