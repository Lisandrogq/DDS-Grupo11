package org.grupo11.Services.Contributor;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Contact.Contact;
import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.ContributionType;
import org.grupo11.Services.Rewards.Reward;

public class Contributor  {
    private String name;
    private List<Contact> contacts;
    private List<ContributionType> possibleContributions;
    private List<Contribution> contributions;
    private List<Reward> rewards;
    private String address = null;
    private double points;

    public Contributor(String name, String address, List<ContributionType> possibleContributions) {
        this.name = name;
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

    public void addReward(Reward reward) {
        rewards.add(reward);
    }

    // getters and settters
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Contact> getContacts() {
        return this.contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
    }

    public List<ContributionType> getPossibleContributions() {
        return this.possibleContributions;
    }

    public void setPossibleContributions(List<ContributionType> possibleContributions) {
        this.possibleContributions = possibleContributions;
    }

    public List<Contribution> getContributions() {
        return this.contributions;
    }

    public void setContributions(List<Contribution> contributions) {
        this.contributions = contributions;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Reward> getRewards() {
        return this.rewards;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public double getPoints() {
        return this.points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

}
