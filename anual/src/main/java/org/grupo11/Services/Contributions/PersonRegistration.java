package org.grupo11.Services.Contributions;

import org.grupo11.Services.PersonInNeed;
import org.grupo11.Services.Contributor.Contributor;

public class PersonRegistration extends Contribution {
    PersonInNeed person;

    public PersonRegistration(PersonInNeed person) {
        this.person = person;
    }

    @Override
    protected boolean validate(Contributor contributor) {
        return (super.validate(contributor) && contributor.address != null);
    }

    public ContributionType getContributionType() {
        return ContributionType.PERSON_REGISTRATION;
    }
}
