package org.grupo11.Services.Contributions;

import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Rewards.RewardSystem;
import org.grupo11.Utils.Crypto;

public abstract class Contribution {
    private int id;
    protected long date;
    protected Contributor contributor;

    public Contribution(long date) {
        id = Crypto.getRandomId(6);
        this.date = date;
    }

   


    public boolean validate(Contributor contributor) {
        contributor.canContributeIn(this.getContributionType());
        return true;
    };

    public abstract ContributionType getContributionType();
    public abstract double getRewardPoints();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Contributor getContributor() {
        return this.contributor;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
    }

}
