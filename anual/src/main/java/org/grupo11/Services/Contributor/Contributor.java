package org.grupo11.Services.Contributor;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Contact.Contact;
import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.ContributionType;

public class Contributor {
    String name;
    public List<Contact> contacts;
    public List<ContributionType> possibleContributions;
    public List<Contribution> contributions;
    public String address = null;

    public Contributor(List<ContributionType> possibleContributions) {
        this.contacts = new ArrayList<>();
        this.possibleContributions = new ArrayList<ContributionType>(possibleContributions);
        this.contributions = new ArrayList<>();
    }

    public Contributor(String address, List<ContributionType> possibleContributions) {
        this.address = address;
        this.contacts = new ArrayList<>();
        this.possibleContributions = new ArrayList<ContributionType>(possibleContributions);
        this.contributions = new ArrayList<>();
    }

    public void contribute(Contribution contribution) {
        contribution.contribute(this);
    }

    public void addContribution(Contribution contribution) {
        contributions.add(contribution);
    }

    public boolean canContributeIn(ContributionType contribution) {
        return possibleContributions.contains(contribution);
    }

    public void addPossibleContribution(ContributionType type) {
        possibleContributions.add(type);
    }
}
