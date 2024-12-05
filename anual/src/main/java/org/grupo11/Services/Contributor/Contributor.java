package org.grupo11.Services.Contributor;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Logger;
import org.grupo11.Services.ActivityRegistry.ContributorRegistry;
import org.grupo11.Services.Contact.Contact;
import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.ContributionType;
import org.grupo11.Services.Contributor.LegalEntity.LegalEntity;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeNotifications;
import org.grupo11.Services.Fridge.Subscription;
import org.grupo11.Services.Fridge.Incident.Incident;
import org.grupo11.Services.Rewards.Reward;
import org.grupo11.Utils.Crypto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Contributor {
    @Id
    protected Long id;
    private String address = null;
    private double points;
    @OneToMany
    private List<Contact> contacts = new ArrayList<>();
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<ContributionType> possibleContributions = new ArrayList<>();
    @OneToMany
    private List<Contribution> contributions = new ArrayList<>();
    @OneToMany
    private List<Reward> rewards = new ArrayList<>();
    @OneToOne
    private ContributorRegistry contributorRegistry;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Subscription> fridgeSubscriptions = new ArrayList<>();

    public Contributor() {
        this.id = Crypto.genId();

    }

    public Contributor(Double points) {
        this.points = points;
        this.id = Crypto.genId();
    }

    public Contributor(String name, String address, List<ContributionType> possibleContributions) {
        this.address = address;
        this.possibleContributions = new ArrayList<ContributionType>(possibleContributions);
        this.contributions = new ArrayList<>();
        this.contacts = new ArrayList<>();
        this.fridgeSubscriptions = new ArrayList<>();
        this.id = Crypto.genId();
    }

    public void addContribution(Contribution contribution) {
        this.contributions.add(contribution);
    }

    public boolean canContributeIn(ContributionType contributionType) {
        // return possibleContributions.contains(contribution); tratando de conservar
        // las possibleContributions me salia esto :p
        // "org.hibernate.LazyInitializationException: failed to lazily initialize a
        // collection of role:
        // org.grupo11.Services.Contributor.Contributor.possibleContributions: could not
        // initialize proxy - no Session"
        // asiq ahora se hace de manera villera
        if (this instanceof Individual) {
            return contributionType == ContributionType.MEAL_DONATION
                    || contributionType == ContributionType.MEAL_DISTRIBUTION
                    || contributionType == ContributionType.MONEY_DONATION
                    || contributionType == ContributionType.PERSON_REGISTRATION
                    /*
                     * TODO: REMOVE THE FOLLOWING ContributionTypes AFTER IMPLEMENTING LEGAL ENTITY
                     * FRONTEND
                     */
                    || contributionType == ContributionType.FRIDGE_ADMINISTRATION
                    || contributionType == ContributionType.REWARD;
        }
        if (this instanceof LegalEntity) {
            return contributionType == ContributionType.FRIDGE_ADMINISTRATION
                    || contributionType == ContributionType.REWARD
                    || contributionType == ContributionType.MONEY_DONATION;
        }
        return false;
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

    public void subscribeToFridge(Fridge fridge, FridgeNotifications type, int threshold) {
        fridge.addNotificationSubscription(new Subscription(this, type, threshold));
    }

    public void unsubscribeFromFridge(Fridge fridge, Subscription subscription) {
        fridge.removeNotificationSubscription(subscription);
        this.fridgeSubscriptions.remove(subscription);
    }

    // getters and setters
    public Long getId() {
        return this.id;
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

    public ContributorRegistry getContributorRegistry() {
        return this.contributorRegistry;
    }

    public void setContributorRegistry(ContributorRegistry contributorRegistry) {
        this.contributorRegistry = contributorRegistry;
    }

    public boolean isIndividual() {
        return this instanceof Individual;
    }

    public boolean isLegalEntity() {
        return this instanceof LegalEntity;
    }

}
