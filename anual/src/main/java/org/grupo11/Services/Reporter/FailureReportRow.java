package org.grupo11.Services.Reporter;

import java.util.List;

import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.Incident.Failure;
import org.grupo11.Services.Fridge.Incident.Incident;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class FailureReportRow {
    private Fridge fridge;
    private List<Incident> failures;

    public FailureReportRow(Fridge fridge, List<Incident> failures) {
        this.fridge = fridge;
        this.failures = failures;
    }


    public Fridge getFridge() {
        return this.fridge;
    }
    public List<Incident> getFailures() {
        return this.failures;
    }


}
