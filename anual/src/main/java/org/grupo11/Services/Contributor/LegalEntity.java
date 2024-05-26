package org.grupo11.Services.Contributor;

import java.util.List;

import org.grupo11.Services.Contributions.ContributionType;

public class LegalEntity extends Contributor {
    // todo replace these with enums
    String businessName;
    String type;
    String category;

    public LegalEntity(List<ContributionType> possibleContributions) {
        super(possibleContributions);

    }

    public LegalEntity(String address, List<ContributionType> possibleContributions) {
        super(address, possibleContributions);
    }
}
