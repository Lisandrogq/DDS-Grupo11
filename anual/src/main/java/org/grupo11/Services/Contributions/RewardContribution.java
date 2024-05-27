package org.grupo11.Services.Contributions;

import org.grupo11.Services.Rewards.Reward;

public class RewardContribution extends Contribution {
    private Reward reward;

    public RewardContribution(Reward reward) {
        this.reward = reward;
    }

    public ContributionType getContributionType() {
        return ContributionType.REWARD;
    }

    public Reward getReward() {
        return this.reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

}
