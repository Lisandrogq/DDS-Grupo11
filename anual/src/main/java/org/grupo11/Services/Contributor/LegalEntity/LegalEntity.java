package org.grupo11.Services.Contributor.LegalEntity;

import java.util.List;

import org.grupo11.Services.Contributions.ContributionType;
import org.grupo11.Services.Contributor.Contributor;

public class LegalEntity extends Contributor {
    private LegalEntityType type;
    private LegalEntityCategory category;

    public LegalEntity(String businessName, String address, LegalEntityType type, LegalEntityCategory category,
            List<ContributionType> possibleContributions) {
        super(businessName, address, possibleContributions);
        this.type = type;
        this.category = category;
    }

    public LegalEntityType getType() {
        return this.type;
    }

    public void setType(LegalEntityType type) {
        this.type = type;
    }

    public LegalEntityCategory getCategory() {
        return this.category;
    }

    public void setCategory(LegalEntityCategory category) {
        this.category = category;
    }

}
