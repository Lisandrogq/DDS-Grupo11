package org.grupo11.Services.Technician;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.grupo11.DB;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Utils.GetNearestTech;
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

    public Technician sendHelpMsgByDistance(Fridge fridge) {
        HashMap<String, Object> map = GetNearestTech.getNearestTechnician(fridge.getLat(), fridge.getLon());
        Technician technician = (Technician) map.get("technician");
        int distance = ((Double) map.get("distance")).intValue();

        if (technician == null) {
            return null;
        } else {
            String message = fridge.getName() + " fridge is malfunctioning, its " + distance + "mts away";
            technician.getContact().SendNotification("WE NEED YOU", message);
            return technician;
        }
    }
}
