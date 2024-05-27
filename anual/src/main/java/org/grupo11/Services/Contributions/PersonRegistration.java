package org.grupo11.Services.Contributions;

import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.PersonInNeed.PersonInNeed;

public class PersonRegistration extends Contribution {
    private PersonInNeed person;

    public PersonRegistration(PersonInNeed person) {
        this.person = person;
    }

    @Override
    protected boolean validate(Contributor contributor) {
        return (super.validate(contributor) && contributor.getAddress() != null);
    }

    public ContributionType getContributionType() {
        return ContributionType.PERSON_REGISTRATION;
    }

    public PersonInNeed getPerson() {
        return this.person;
    }

    public void setPerson(PersonInNeed person) {
        this.person = person;
    }

}
