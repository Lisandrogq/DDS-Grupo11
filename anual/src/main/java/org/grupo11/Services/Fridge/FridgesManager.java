package org.grupo11.Services.Fridge;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Services.Contributions.FridgeAdmin;
import org.grupo11.Services.Contributor.LegalEntity.LegalEntity;
import org.grupo11.Services.Fridge.Sensor.MovementSensorManager;
import org.grupo11.Services.Fridge.Sensor.TemperatureSensorManager;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class FridgesManager {
    private List<Fridge> fridges;
    private FridgeAllocator fridgeAllocator;
    private static FridgesManager instance = null;

    private FridgesManager() {
        this.fridges = new ArrayList<>();
        this.fridgeAllocator = new FridgeAllocator();
    }

    public static synchronized FridgesManager getInstance() {
        if (instance == null)
            instance = new FridgesManager();

        return instance;
    }

    public void add(Fridge fridge) {
        fridges.add(fridge);
    }

    public void remove(Fridge fridge) {
        fridges.remove(fridge);
    }

    public void setFridges(List<Fridge> fridges) {
        this.fridges = fridges;
    }

    public Fridge getById(int id) {
        for (Fridge fridge : fridges) {
            if (fridge.getId() == id) {
                return fridge;
            }
        }
        return null;
    }

    public Fridge queryById(int id) {
        try {
            Session session = DB.getSessionFactory().openSession();
            String hql = "SELECT f " +
                    "FROM Fridge f WHERE f.id = :id";
            Query<Fridge> query = session.createQuery(hql, Fridge.class);
            query.setParameter("id", id);
            Fridge fridge = query.getSingleResult();
            return fridge;
        } catch (Exception e) {
            return null;
        }
    }

    public TemperatureSensorManager queryTemperatureManagerFromFridgeId(int fridgeId) {
        try {
            Session session = DB.getSessionFactory().openSession();
            String hql = "SELECT f " +
                    "FROM Fridge f WHERE f.id = :id";
            Query<Fridge> query = session.createQuery(hql, Fridge.class);
            query.setParameter("id", fridgeId);
            Fridge fridge = query.getSingleResult();
            TemperatureSensorManager sensorManager = fridge.getTempManager();
            sensorManager.getSensors(); // load sensors to memory too
            return sensorManager;
        } catch (Exception e) {
            return null;
        }
    }

    public MovementSensorManager queryMovementManagerFromFridgeId(int fridgeId) {
        try {
            Session session = DB.getSessionFactory().openSession();
            String hql = "SELECT f " +
                    "FROM Fridge f WHERE f.id = :id";
            Query<Fridge> query = session.createQuery(hql, Fridge.class);
            query.setParameter("id", fridgeId);
            Fridge fridge = query.getSingleResult();
            MovementSensorManager sensorManager = fridge.getMovManager();
            sensorManager.getSensors(); // load sensors to memory too
            return sensorManager;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isFridgeOwnerByFridgeId(LegalEntity legalEntity, int fridgeId) {
        try {
            Session session = DB.getSessionFactory().openSession();
            String hql = "SELECT f " +
                    "FROM FridgeAdmin f WHERE f.business = :business AND f.fridge.id = :fridgeId";
            Query<FridgeAdmin> query = session.createQuery(hql, FridgeAdmin.class);
            query.setParameter("business", legalEntity);
            query.setParameter("fridgeId", fridgeId);
            if (query.getResultCount() > 0)
                return true;
            return false;
        } catch (Exception e) {
            Logger.error("Exception ", e);
            return false;
        }
    }

    public String getFridgesMap() {
        return FridgeMapper.getFridgesMapLocations(fridges);
    }

    public FridgeAllocator getFridgeAllocator() {
        return this.fridgeAllocator;
    }
}
