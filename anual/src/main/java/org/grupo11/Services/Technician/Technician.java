package org.grupo11.Services.Technician;

public class Technician {
    private String name;
    private String surname;
    private TechnicianType type;
    private int DNI;
    private String cuil;
    private String areasOfWork;

    public Technician(String name, String surname, TechnicianType type, int DNI, String cuil, String areasOfWork) {
        this.name = name;
        this.surname = surname;
        this.type = type;
        this.DNI = DNI;
        this.cuil = cuil;
        this.areasOfWork = areasOfWork;
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

    public String getAreasOfWork() {
        return this.areasOfWork;
    }

    public void setAreasOfWork(String areasOfWork) {
        this.areasOfWork = areasOfWork;
    }

}