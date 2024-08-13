package org.grupo11.Services.Contributions;

import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.PersonInNeed.PersonInNeed;

public class PersonRegistration extends Contribution {
    private PersonInNeed person;
    private Contributor givenBy;
    public PersonRegistration(PersonInNeed person, long date, Contributor givenBy) {
        super(date);
        this.person = person;
        this.givenBy = givenBy;
    }

    @Override
    public boolean validate(Contributor contributor) {
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

    @Override
    public double getRewardPoints() {
        return 1.2;
    }

}
