package org.grupo11.Services.Contributor;

import java.util.ArrayList;
import java.util.List;

public class ContributorsManager {
    private List<Contributor> contributors;

    public ContributorsManager() {
        this.contributors = new ArrayList<>();
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
            if (contributor instanceof Individual) {
                return ((Individual) contributor).getDocument() == document ? (Individual) contributor : null;
            }
        }
        return null;
    }

    public List<Contributor> getContributors() {
        return this.contributors;
    }

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }

}