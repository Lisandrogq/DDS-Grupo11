package org.grupo11.Services.Fridge;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Utils.JSON;
import org.hibernate.Session;

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

    public List<Fridge> getFridges() {
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT f FROM Fridge f JOIN FETCH f.incidents i";
            org.hibernate.query.Query<Fridge> query = session.createQuery(hql, Fridge.class);
            List<Fridge> fridges = query.getResultList();
            for (Fridge fridge : fridges) {
                fridge.getIncidents();
            }
            session.close();
            return fridges;
        } catch (Exception e) {
            Logger.error("Could not create report", e);
            return new ArrayList<>();
        }
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

    public String getFridgesMap() {
        return FridgeMapper.getFridgesMapLocations(fridges);
    }

    public FridgeAllocator getFridgeAllocator() {
        return this.fridgeAllocator;
    }
}
