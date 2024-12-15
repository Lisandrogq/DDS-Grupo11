package org.grupo11.Api.JsonData.ExchangeRewards;

import java.util.List;

public class RedeemRequest {
    private int userPoints;
    private List<RewardData> rewards;

    // Getters y setters
    public int getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(int userPoints) {
        this.userPoints = userPoints;
    }

    public List<RewardData> getRewardsData() {
        return rewards;
    }

    public void setRewards(List<RewardData> rewardsData) {
        this.rewards = rewardsData;
    }

    public static class RewardData {
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
