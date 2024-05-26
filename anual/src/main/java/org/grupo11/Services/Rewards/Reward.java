package org.grupo11.Services.Rewards;

public class Reward {
    public String name;
    public String imageUrl;
    public float neededPoints;
    public RewardCategory category;

    public Reward(String name, float neededPoints, String imageUrl, RewardCategory category) {
        this.name = name;
        this.category = category;
        this.neededPoints = neededPoints;
        this.imageUrl = imageUrl;
    }
}
