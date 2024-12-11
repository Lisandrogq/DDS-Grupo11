package org.grupo11.Services.Technician;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Enums.Provinces;
import org.grupo11.Services.Credentials;
import org.grupo11.Services.Contact.Contact;
import org.grupo11.Utils.Crypto;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Technician {
    @Id
    private Long id;
    private String name;
    private String surname;
    @Enumerated(EnumType.STRING)
    private TechnicianType type;
    private int DNI;
    private String cuil;
    private String address;
    @OneToMany
    private List<TechnicianVisit> visits;
    @OneToOne
    private Contact contact;
    @OneToOne
    private Credentials credentials;
    private List<String> alerts= new ArrayList<>();

    public Technician() {
        alerts = new ArrayList<>();
    }

    public Technician(String name, String surname, TechnicianType type, int DNI, String cuil,
            String address,
            Contact contact) {
        this.id = Crypto.genId();
        this.name = name;
        this.surname = surname;
        this.type = type;
        this.DNI = DNI;
        this.cuil = cuil;
        this.address = address;
        this.contact = contact;
        this.visits = new ArrayList<>();
    }

    public List<String> getNotifications() {
            return alerts;
    }

    public void setNotifications(List<String> alerts) {
        this.alerts = alerts;
    }

    public void addNotification(String notification) {
        alerts.add(notification);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public TechnicianType getType() {
        return this.type;
    }

    public void setType(TechnicianType type) {
        this.type = type;
    }

    public int getDNI() {
        return this.DNI;
    }

    public void setDNI(int dni) {
        this.DNI = dni;
    }

    public String getCuil() {
        return this.cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<TechnicianVisit> getVisits() {
        return this.visits;
    }

    public void addVisit(TechnicianVisit visit) {
        this.visits.add(visit);
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
