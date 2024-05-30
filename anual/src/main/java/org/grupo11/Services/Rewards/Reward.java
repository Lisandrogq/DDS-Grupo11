package org.grupo11.Services.Rewards;

import org.grupo11.Utils.Crypto;

public class Reward {
    private int id;
    private String name;
    private String imageUrl;
    private float neededPoints;
    private RewardCategory category;

    public Reward(String name, float neededPoints, String imageUrl, RewardCategory category) {
        this.id = Crypto.getRandomId(10);
        this.name = name;
        this.category = category;
        this.neededPoints = neededPoints;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getNeededPoints() {
        return this.neededPoints;
    }

    public void setNeededPoints(float neededPoints) {
        this.neededPoints = neededPoints;
    }

    public RewardCategory getCategory() {
        return this.category;
    }

    public void setCategory(RewardCategory category) {
        this.category = category;
    }

}
