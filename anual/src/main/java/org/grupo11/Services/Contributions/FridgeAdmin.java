package org.grupo11.Services.Contributions;

import org.grupo11.Services.Contributor.LegalEntity;
import org.grupo11.Services.Fridge.Fridge;

public class FridgeAdmin extends Contribution {
    LegalEntity business;
    Fridge fridge;

    public FridgeAdmin(LegalEntity business, Fridge fridge) {
        this.business = business;
        this.fridge = fridge;
    }

    public ContributionType getContributionType() {
        return ContributionType.FRIDGE_ADMINISTRATION;
    }
}
