package org.grupo11.Services.Contributions;

import org.grupo11.Services.Rewards.Reward;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class RewardContribution extends Contribution {
    @Id
    @GeneratedValue
    private Long id;
    private Reward reward;

    public RewardContribution(Reward reward, long date) {
        super(date);
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

    @Override
    public double getRewardPoints() {
        return 0;
    }

}
