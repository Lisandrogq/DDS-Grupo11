package org.grupo11.Services.PersonInNeed;

import org.grupo11.Services.Card.Card;

public class PersonInNeed {
    private int birth;
    private int createdAt;
    private String address = null;
    private int DNI;
    private int childCount;
    private Card card;

    public PersonInNeed(int birth, int createdAt, String address, int DNI, int childCount, Card card) {
        this.birth = birth;
        this.createdAt = createdAt;
        this.address = address;
        this.DNI = DNI;
        this.childCount = childCount;
        this.card = card;
    }

    public int getBirth() {
        return this.birth;
    }

    public void setBirth(int birth) {
        this.birth = birth;
    }

    public int getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDNI() {
        return this.DNI;
    }

    public void setDNI(int identification) {
        this.DNI = identification;
    }

    public int getChildCount() {
        return this.childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public Card getCard() {
        return this.card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

}
