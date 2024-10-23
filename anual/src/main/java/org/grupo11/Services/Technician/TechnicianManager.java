package org.grupo11.Services.Technician;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Fridge.Fridge;

public class TechnicianManager {
    private List<Technician> technicians;
    private static TechnicianManager instance = null;

    private TechnicianManager() {
        this.technicians = new ArrayList<>();
    }

    public static synchronized TechnicianManager getInstance() {
        if (instance == null)
            instance = new TechnicianManager();

        return instance;
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
    public Technician selectTechnician (Fridge fridge){

        for (Technician technician : TechnicianManager.getInstance().getTechnicians()) {
            //todo: aplicar array de strategy para los criterios q van en el if)
            if (technician.getAreasOfWork() == fridge.getArea() && technician.getType() == TechnicianType.ELECTRICIAN) {
                technician.getContact().SendNotification("WE NEED YOU",
                        "We need you to fix a fridge");
                return technician;
            }
        }
        return null;
    }
}
