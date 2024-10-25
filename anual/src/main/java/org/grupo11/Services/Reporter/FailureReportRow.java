package org.grupo11.Services.Reporter;

import java.util.List;

import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.Incident.Incident;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class FailureReportRow {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Fridge fridge;
    @OneToMany
    private List<Incident> failures;

    public FailureReportRow() {
    }

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
