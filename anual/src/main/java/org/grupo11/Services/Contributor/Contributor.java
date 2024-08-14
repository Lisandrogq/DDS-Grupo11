package org.grupo11.Services.Contributor;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.ActivityRegistry.ContributorRegistry;
import org.grupo11.Services.Contact.Contact;
import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.ContributionType;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeNotifications;
import org.grupo11.Services.Fridge.Subscription;
import org.grupo11.Services.Fridge.Incident.Incident;
import org.grupo11.Services.Rewards.Reward;

public class Contributor {
    private String name;
    private List<Contact> contacts = new ArrayList<Contact>();;
    private List<ContributionType> possibleContributions;
    private List<Contribution> contributions;
    private List<Reward> rewards;
    private String address = null;
    private double points;
    private ContributorRegistry contributorRegistry = null;
    private List<Subscription> fridgeSubscriptions;

    public Contributor(String name, String address, List<ContributionType> possibleContributions) {
        this.name = name;
        this.address = address;
        this.possibleContributions = new ArrayList<ContributionType>(possibleContributions);
        this.contributions = new ArrayList<>();
        this.fridgeSubscriptions = new ArrayList<>();
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

    public void reportIncident(Fridge fridge, Incident incident) {
        fridge.addIncident(incident);
    }

    public void subscribeToFridge(Fridge fridge, FridgeNotifications type) {
        fridge.addNotificationSubscription(new Subscription(this, type));
    }

    public void unsubscribeFromFridge(Fridge fridge, Subscription subscription) {
        fridge.removeNotificationSubscription(subscription);
        this.fridgeSubscriptions.remove(subscription);
    }

    // getters and setters
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

    public ContributorRegistry getCard() {
        return this.contributorRegistry;
    }

    public void setCard(ContributorRegistry contributorRegistry) {
        this.contributorRegistry = contributorRegistry;
    }
}
