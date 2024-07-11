package org.grupo11.Services.Contributor;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.ContributionsManager;
import org.grupo11.Services.Rewards.RewardSystem;

public class ContributorsManager {
    private static ContributorsManager instance = null;
    private List<Contributor> contributors;
    ContributionsManager contributionsManager;


    private ContributorsManager() {
        this.contributors = new ArrayList<>();
    }

    public static synchronized ContributorsManager getInstance() {
        if (instance == null)
            instance = new ContributorsManager();

        return instance;
    }

    public void add(Contributor contributor) {
        contributors.add(contributor);
    }

    public void remove(Contributor contributor) {
        contributors.remove(contributor);
    }

    public Contributor getByName(String name) {
        for (Contributor contributor : contributors) {
            if (contributor.getName() == name) {
                return contributor;
            }
        }
        return null;
    }

    public Individual getIndividualByDocument(int document) {
        for (Contributor contributor : contributors) {
            if (contributor instanceof Individual && ((Individual) contributor).getDocument() == document)
                return (Individual) contributor;
        }
        return null;
    }

    public void addContributionToContributor(Contributor contributor,Contribution contribution){
        if (contribution.validate(contributor)) {
            contribution.setContributor(contributor);
            contributor.addContribution(contribution);
            RewardSystem.assignPoints(contributor, contribution);
            contributionsManager.add(contribution);
        }
    }
    public List<Contributor> getContributors() {
        return this.contributors;
    }

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }

}