package org.grupo11.Services.Fridge.Incident;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Technician.TechnicianVisit;

public class Incident {
    private List<TechnicianVisit> visits;
    private boolean hasBeenFixed;
    private long detectedAt;

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
}
