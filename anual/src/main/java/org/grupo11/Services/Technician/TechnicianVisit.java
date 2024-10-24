package org.grupo11.Services.Technician;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class TechnicianVisit {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Technician technician;
    @ElementCollection
    private List<String> pictureUrls;
    private String description;

    public TechnicianVisit() {
    }

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
