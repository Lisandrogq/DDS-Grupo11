package org.grupo11.Services.Rewards;

import java.util.HashMap;
import java.util.Map;

import org.grupo11.Services.Contributor.Contributor;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Reward {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String description;
    private String imageUrl;
    private float neededPoints;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private RewardCategory category;
    @ManyToOne
    private Contributor contributor;

    public Reward() {
    }

    public Reward(String name, float neededPoints, String imageUrl, RewardCategory category) {
        this.name = name;
        this.category = category;
        this.neededPoints = neededPoints;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return this.id;
    }

    public String getIdAsString() {
        return Integer.toString(this.id);
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

    public Contributor getContributor() {
        return this.contributor;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
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

    public String getNeededPointsAsString() {
        int intPoints = (int) this.neededPoints;
        return Integer.toString(intPoints);
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

    public int getQuantity() {
        return this.quantity;
    }

    public String getQuantityAsString() {
        return Integer.toString(this.quantity);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> reward_map = new HashMap<>();
        String emoji = getCategory() == RewardCategory.TECH ? "💻"
                : (getCategory() == RewardCategory.COOKING ? "🍴" : "🏡");
        reward_map.put("id", getIdAsString());
        reward_map.put("name", getName());
        reward_map.put("emoji", emoji);
        reward_map.put("category", getCategory().toString());
        reward_map.put("description", getDescription());
        reward_map.put("neededPoints", getNeededPointsAsString());
        reward_map.put("quantity", getQuantityAsString());
        if (getImageUrl() != null)
            reward_map.put("imageUrl", getImageUrl());
        else
            reward_map.put("imageUrl", "https://images.emojiterra.com/google/android-12l/512px/1f381.png");
        return reward_map;
    }
}
