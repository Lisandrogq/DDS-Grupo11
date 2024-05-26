package org.grupo11.Services.Contributor;

import java.util.List;

import org.grupo11.Services.Contributions.ContributionType;

public class Individual extends Contributor {
    // todo replace these with enums
    String name;
    String surname;
    String birth;

    public Individual(List<ContributionType> possibleContributions) {
        super(possibleContributions);

    }

    public Individual(String address, List<ContributionType> possibleContributions) {
        super(address, possibleContributions);
    }
}
