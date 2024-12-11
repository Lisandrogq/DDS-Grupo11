package org.grupo11.Services.Technician;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.DB;
import org.grupo11.Services.Fridge.Fridge;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class TechnicianManager {
    private List<Technician> technicians;
    private static TechnicianManager instance = null;

    private TechnicianManager() {
        this.technicians = new ArrayList<>();
    }

    public static synchronized TechnicianManager getInstance() {
        if (instance == null)
            instance = new TechnicianManager();
        // inicializacion/update de lista
        Session session = DB.getSessionFactory().openSession();
        String hql = "SELECT t " +
                "FROM Technician t ";
        Query<Technician> query = session.createQuery(hql, Technician.class);
        instance.setTechnicians(query.getResultList());
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

    public Technician selectTechnician(Fridge fridge) {

        Technician technician = technicians.get(0);// TODO: calculate distance between addresses
        int distance = 0;
        String message = fridge.getName() + " fridge is malfunctioning, its " + distance + "mts away";
        technician.addNotification(message);
        technician.getContact().SendNotification("WE NEED YOU", message);
        return technician;
        
    }
}
