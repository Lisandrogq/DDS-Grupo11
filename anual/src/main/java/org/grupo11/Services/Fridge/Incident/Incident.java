package org.grupo11.Services.Fridge.Incident;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Technician.TechnicianVisit;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Incident {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany
    private List<TechnicianVisit> visits;
    private boolean hasBeenFixed;
    private long detectedAt;
    @ManyToOne
    protected Fridge fridge;

    public Incident() {
    }

    public Incident(long detectedAt) {
        this.visits = new ArrayList<>();
        this.hasBeenFixed = false;
        this.detectedAt = detectedAt;
    }

    public List<TechnicianVisit> getVisits() {
        return this.visits;
    }

    public void addVisits(TechnicianVisit visit) {
        this.visits.add(visit);
    }

    public boolean hasBeenFixed() {
        return this.hasBeenFixed;
    }

    public void markAsFixed() {
        this.hasBeenFixed = true;
    }

    public long getDetectedAt() {
        return this.detectedAt;
    }
    public Fridge getFridge() {
        return fridge;
    }
    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }
}
