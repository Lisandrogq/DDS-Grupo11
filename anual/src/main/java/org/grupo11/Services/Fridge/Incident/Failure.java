package org.grupo11.Services.Fridge.Incident;

import java.util.List;

import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Fridge;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;

@Entity
public class Failure extends Incident {
    @ManyToOne
    private Contributor reportedBy;
    private String description;
    @ElementCollection
    private List<String> pictureUrls;
    @Enumerated(EnumType.STRING)
    Urgency urgency;

    public Failure() {
    }

    public Failure(Fridge fridge, Contributor reportedBy, String description, Urgency urgency,
            long detectedAt) {
        super(detectedAt);
        this.fridge = fridge;
        this.urgency = urgency;
        this.reportedBy = reportedBy;
        this.description = description;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public void setUrgency(Urgency urgency) {
        this.urgency = urgency;
    }

    public Contributor getReportedBy() {
        return this.reportedBy;
    }

    public String getDescription() {
        return this.description;
    }

    public List<String> getPictureUrls() {
        return this.pictureUrls;
    }

    public Long getReporterID() {
        return this.reportedBy.getId();
    }
}
