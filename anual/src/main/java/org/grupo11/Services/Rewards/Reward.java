package org.grupo11.Services.Rewards;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

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
}
