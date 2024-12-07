package org.grupo11.Services.Contributions;

import org.grupo11.Services.Contributor.LegalEntity.LegalEntity;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgesManager;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class FridgeAdmin extends Contribution {
    @OneToOne
    private LegalEntity business;
    @OneToOne
    public Fridge fridge;

    public FridgeAdmin() {
    }

    public FridgeAdmin(LegalEntity business, Fridge fridge, long date) {
        super(date);
        this.business = business;
        this.fridge = fridge;
        FridgesManager manager = FridgesManager.getInstance();
        manager.add(fridge);
    }

    public ContributionType getContributionType() {
        return ContributionType.FRIDGE_ADMINISTRATION;
    }

    public LegalEntity getBusiness() {
        return this.business;
    }

    public void setBusiness(LegalEntity business) {
        this.business = business;
    }

     public Fridge getFridge() {
     return this.fridge;
     }

    // public void setFridge(Fridge fridge) {
    // this.fridge = fridge;
    // }

    @Override
    public double getRewardPoints() {
        return 5;
    }

}
