package org.grupo11.Services.Rewards.ExchangeRewards;

import java.util.List;

public class RedeemRequest {
    private int userPoints;
    private List<Reward> rewards;

    // Getters y setters
    public int getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(int userPoints) {
        this.userPoints = userPoints;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

    // Clase interna para representar una recompensa
    public static class Reward {
        private String rewardId;
        private int quantity;

        public String getRewardId() {
            return rewardId;
        }

        public void setRewardId(String rewardId) {
            this.rewardId = rewardId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}