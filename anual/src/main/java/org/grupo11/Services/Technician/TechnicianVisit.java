package org.grupo11.Services.Technician;

import java.util.List;

public class TechnicianVisit {
    private Technician technician;
    private List<String> pictureUrls;
    private String description;

    public TechnicianVisit(Technician technician, List<String> pictureUrls, String description) {
        this.technician = technician;
        this.pictureUrls = pictureUrls;
        this.description = description;
    }

    public Technician getTechnician() {
        return this.technician;
    }

    public List<String> getPictureUrls() {
        return this.pictureUrls;
    }

    public String getDescription() {
        return this.description;
    }

}
