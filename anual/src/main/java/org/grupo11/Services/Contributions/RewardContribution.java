package org.grupo11.Services.Contributions;

import org.grupo11.Services.Rewards.Reward;

public class RewardContribution extends Contribution {
    public Reward reward;

    public RewardContribution(Reward reward) {
        this.reward = reward;
    }

    public ContributionType getContributionType() {
        return ContributionType.REWARD;
    }
}
