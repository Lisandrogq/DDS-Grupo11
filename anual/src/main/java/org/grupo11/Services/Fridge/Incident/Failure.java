package org.grupo11.Services.Fridge.Incident;

import java.util.List;

import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Fridge;

public class Failure extends Incident {
    private Contributor reportedBy;
    private String description;
    private List<String> pictureUrls;

    public Failure(Fridge fridge, Contributor reportedBy, String description, List<String> pictureUrls,
            long detectedAt) {
        super(detectedAt);

        this.reportedBy = reportedBy;
        this.description = description;
        this.pictureUrls = pictureUrls;
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
}
