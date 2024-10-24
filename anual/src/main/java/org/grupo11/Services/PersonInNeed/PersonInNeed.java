package org.grupo11.Services.PersonInNeed;

import org.grupo11.Services.ActivityRegistry.PINRegistry;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class PersonInNeed {
    @Id
    @GeneratedValue
    private Long id;
    private int birth;
    private int createdAt;
    private String address = null;
    private int DNI;
    private int childCount;
    @OneToOne
    private PINRegistry PINRegistry;

    public PersonInNeed() {
    }

    public PersonInNeed(int birth, int createdAt, String address, int DNI, int childCount, PINRegistry PINRegistry) {
        this.birth = birth;
        this.createdAt = createdAt;
        this.address = address;
        this.DNI = DNI;
        this.childCount = childCount;
        this.PINRegistry = PINRegistry;
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

    public PINRegistry getCard() {
        return this.PINRegistry;
    }

    public void setCard(PINRegistry card) {
        this.PINRegistry = card;
    }

}
