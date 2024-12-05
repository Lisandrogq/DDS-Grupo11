package org.grupo11.Services.Contributor;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.ContributionsManager;
import org.grupo11.Services.Rewards.RewardSystem;

public class ContributorsManager {
    private List<Contributor> contributors;
    private static ContributorsManager instance = null;
    ContributionsManager contributionsManager = ContributionsManager.getInstance();

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

    public Individual getIndividualByDocument(int document) {
        for (Contributor contributor : contributors) {
            if (contributor instanceof Individual && ((Individual) contributor).getDocument() == document)
                return (Individual) contributor;
        }
        return null;
    }

    public Boolean addContributionToContributor(Contributor contributor, Contribution contribution) {
        if (contribution.validate(contributor)) {
            contribution.setContributor(contributor);
            // contributor.addContribution(contribution); //deja de ser necesario pq las
            // contribuciones de un contributor se sacan por FK 
            contribution.afterContribution();
            RewardSystem.assignPoints(contributor, contribution);
            contributionsManager.add(contribution);
            return true;
        }
        return false;
    }

    public List<Contributor> getContributors() {
        return this.contributors;
    }

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }

}
