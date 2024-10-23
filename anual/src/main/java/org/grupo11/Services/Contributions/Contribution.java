package org.grupo11.Services.Contributions;

import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Utils.Crypto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Contribution {
    @Id
    @GeneratedValue
    private int id;
    protected long date;
    @ManyToOne
    protected Contributor contributor;

    public Contribution() {
    }

    public Contribution(long date) {
        id = Crypto.getRandomId(6);
        this.date = date;
    }

    public boolean validate(Contributor contributor) {
        return contributor.canContributeIn(this.getContributionType());
    };

    public void afterContribution() {
    }

    public abstract ContributionType getContributionType();

    public abstract double getRewardPoints();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Contributor getContributor() {
        return this.contributor;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
    }

}
