package org.grupo11.Services.Technician;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Enums.Provinces;
import org.grupo11.Services.Contact.Contact;
import org.grupo11.Services.Fridge.Incident.Alert;

public class Technician {
    private String name;
    private String surname;
    private TechnicianType type;
    private int DNI;
    private String cuil;
    private Provinces areasOfWork;
    private List<TechnicianVisit> visits;
    private Contact contact;

    public Technician(String name, String surname, TechnicianType type, int DNI, String cuil,
            Provinces areasOfWork,
            Contact contact) {
        this.name = name;
        this.surname = surname;
        this.type = type;
        this.DNI = DNI;
        this.cuil = cuil;
        this.areasOfWork = areasOfWork;
        this.contact = contact;
        this.visits = new ArrayList<>();
    }

    public String getName() {
        return this.name;
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

    public Provinces getAreasOfWork() {
        return this.areasOfWork;
    }

    public void setAreasOfWork(Provinces areasOfWork) {
        this.areasOfWork = areasOfWork;
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