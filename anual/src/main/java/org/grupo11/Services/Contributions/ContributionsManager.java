package org.grupo11.Services.Contributions;

import java.util.ArrayList;
import java.util.List;

public class ContributionsManager {
    private List<Contribution> contributions;
    private static ContributionsManager instance = null;

    private ContributionsManager() {
        this.contributions = new ArrayList<>();
    }

    public static synchronized ContributionsManager getInstance() {
        if (instance == null)
            instance = new ContributionsManager();

        return instance;
    }

    public void add(Contribution contribution) {
        contributions.add(contribution);
    }

    public void remove(Contribution contribution) {
        contributions.remove(contribution);
    }

    public List<Contribution> getContributions() {
        return this.contributions;
    }

    public void setContributions(List<Contribution> contributions) {
        this.contributions = contributions;
    }

    public Contribution getById(int id) {
        for (Contribution contribution : contributions) {
            if (contribution.getId() == id) {
                return contribution;
            }
        }
        return null;
    }

    public List<Contribution> getAllByType(ContributionType type) {
        List<Contribution> contributionsByType = new ArrayList<>();
        for (Contribution contribution : contributions) {
            if (contribution.getContributionType() == type) {
                contributionsByType.add(contribution);
            }
        }
        return contributionsByType;
    }
}
