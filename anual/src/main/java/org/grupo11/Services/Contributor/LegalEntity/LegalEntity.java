package org.grupo11.Services.Contributor.LegalEntity;

import java.util.List;

import org.grupo11.Services.Contributions.ContributionType;
import org.grupo11.Services.Contributor.Contributor;

public class LegalEntity extends Contributor {
    private String businessName;
    private LegalEntityType type;
    private LegalEntityCategory category;

    public LegalEntity(List<ContributionType> possibleContributions) {
        super(possibleContributions);

    }

    public LegalEntity(String address, List<ContributionType> possibleContributions) {
        super(address, possibleContributions);
    }

    public String getBusinessName() {
        return this.businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
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
