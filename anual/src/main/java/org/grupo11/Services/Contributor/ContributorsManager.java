package org.grupo11.Services.Contributor;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.ContributionsManager;
import org.grupo11.Services.Fridge.FridgeOpenLogEntry;
import org.grupo11.Services.Rewards.RewardSystem;
import org.hibernate.Session;

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

    public List<FridgeOpenLogEntry> addContributionToContributor(Contributor contributor, Contribution contribution) {

        if (contribution.validate(contributor)) {
            contribution.setContributor(contributor);
            // contributor.addContribution(contribution); //deja de ser necesario pq las
            // contribuciones de un contributor se sacan por FK
            List<FridgeOpenLogEntry> entries = contribution.afterContribution();
            RewardSystem.assignPoints(contributor, contribution);
            contributionsManager.add(contribution);
            return entries;
        }
        return null;
    }

    public List<Contributor> getContributors() {
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT c FROM Contributor c " +
                    "JOIN c.contributions contr";
            org.hibernate.query.Query<Contributor> query = session.createQuery(hql, Contributor.class);
            List<Contributor> contributors = query.getResultList();
            return contributors;
        } catch (Exception e) {
            Logger.error("Could not create contributor", e);
            return new ArrayList<>();
        }
    }

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }

}
