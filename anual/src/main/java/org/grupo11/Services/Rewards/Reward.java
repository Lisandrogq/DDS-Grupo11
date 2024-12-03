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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> reward_map = new HashMap<>();
        String emoji = getCategory() == RewardCategory.TECH ? "💻"
                : (getCategory() == RewardCategory.COOKING ? "🍴" : "🏡");
        reward_map.put("emoji", emoji);
        reward_map.put("category", getCategory().toString());
        reward_map.put("description", "You can exchange your points for (todo text depending on category)");
        return reward_map;
    }
}
