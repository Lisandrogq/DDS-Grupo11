package org.grupo11.Services.ActivityRegistry;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.DB;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class RegistryManager {
    private List<ActivityRegistry> registers;
    private static RegistryManager instance = null;

    private RegistryManager() {
        this.registers = new ArrayList<>();
    }

    public static synchronized RegistryManager getInstance() {
        if (instance == null)
            instance = new RegistryManager();

        return instance;
    }

    public void add(ActivityRegistry registry) {
        registers.add(registry);
    }

    public void remove(ActivityRegistry registry) {
        registers.remove(registry);
    }

    public List<ActivityRegistry> getRegisters() {
        return this.registers;
    }

    public void setRegisters(List<ActivityRegistry> cards) {
        this.registers = cards;
    }

    public ActivityRegistry getById(int id) {
        for (ActivityRegistry registry : registers) {
            if (registry.getId() == id) {
                return registry;
            }
        }
        return null;
    }

    public PINRegistry queryPinRegistryByNumberAndSecurityCode(String number, String securityCode) {
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT p from PINRegistry p WHERE p.number = :number AND p.securityCode = :securityCode";
            Query<PINRegistry> query = session.createQuery(hql, PINRegistry.class);
            query.setParameter("number", number);
            query.setParameter("securityCode", securityCode);
            PINRegistry registry = query.getSingleResult();
            Hibernate.initialize(registry.getUsages());
            return registry;
        } catch (Exception e) {
            return null;
        }
    }
}
