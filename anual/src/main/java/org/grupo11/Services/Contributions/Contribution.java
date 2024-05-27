package org.grupo11.Services.Contributions;

import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Rewards.RewardSystem;
import org.grupo11.Utils.Crypto;

public abstract class Contribution {
    private int id;

    public Contribution() {
        id = Crypto.getRandomId(6);
    }

    /**
     * @return false in case validation fails
     */
    public boolean contribute(Contributor contributor) {
        if (this.validate(contributor)) {
            contributor.addContribution(this);
            RewardSystem.assignPoints(contributor, this);
            return true;
        }
        return false;
    }

    protected boolean validate(Contributor contributor) {
        contributor.canContributeIn(this.getContributionType());
        return true;
    };

    public abstract ContributionType getContributionType();

    public int getId() {
        return id;
    }
}
