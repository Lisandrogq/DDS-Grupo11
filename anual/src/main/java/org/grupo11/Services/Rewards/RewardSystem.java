package org.grupo11.Services.Rewards;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributor.Contributor;

public class RewardSystem {
    private List<Reward> rewards;
    private static RewardSystem instance;

    private RewardSystem() {
        this.rewards = new ArrayList<>();
    }

    public static synchronized RewardSystem getInstance() {
        if (instance == null)
            instance = new RewardSystem();

        return instance;
    }

    public List<Reward> getRewards() {
        return this.rewards;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public static void assignPoints(Contributor contributor, Contribution contribution) {
        contributor.setPoints(contributor.getPoints() + RewardPointsCalculator.getContributionPoints(contribution));
    }

    /**
     * 
     * @return true if bought was successful
     */
    public boolean buyReward(Contributor contributor, int rewardId) {
        Reward reward = getRewardById(rewardId);
        int quantity = reward.getQuantity();
        if (quantity <= 0)
            return false;
        if (reward != null && contributor.getPoints() >= reward.getNeededPoints()) {
            // Deduct points from contributor
            double remainingPoints = contributor.getPoints() - reward.getNeededPoints();
            contributor.setPoints(remainingPoints);
            contributor.addReward(reward);
            reward.setQuantity(quantity - 1);
            return true;
        }

        return false;
    }

    private Reward getRewardById(int id) {
        for (Reward reward : rewards) {
            if (reward.getId() == id) {
                return reward;
            }
        }
        return null;
    }

    public void listRewards() {
        System.out.println("Available Rewards:");
        for (Reward reward : rewards) {
            System.out.println("ID: " + reward.getId() + ", Name: " + reward.getName() + ", Points Needed: "
                    + reward.getNeededPoints());
        }
    }
}