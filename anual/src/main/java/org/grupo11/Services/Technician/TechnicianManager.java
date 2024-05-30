package org.grupo11.Services.Technician;

import java.util.ArrayList;
import java.util.List;

public class TechnicianManager {
    private List<Technician> technicians;

    public TechnicianManager() {
        this.technicians = new ArrayList<>();
    }

    public void add(Technician technician) {
        technicians.add(technician);
    }

    public void remove(Technician technician) {
        technicians.remove(technician);
    }

    public List<Technician> getTechnicians() {
        return this.technicians;
    }

    public void setTechnicians(List<Technician> technicians) {
        this.technicians = technicians;
    }

    public Technician getByDNI(int id) {
        for (Technician technician : technicians) {
            if (technician.getDNI() == id) {
                return technician;
            }
        }
        return null;
    }
}
