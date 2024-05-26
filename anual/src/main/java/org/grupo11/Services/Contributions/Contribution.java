package org.grupo11.Services.Contributions;

import org.grupo11.Services.Contributor.Contributor;

public abstract class Contribution {
    /**
     * @return false in case validation fails
     */
    public boolean contribute(Contributor contributor) {
        if (this.validate(contributor)) {
            contributor.addContribution(this);
            return true;
        }
        return false;
    }

    protected boolean validate(Contributor contributor) {
        contributor.canContributeIn(this.getContributionType());
        return true;
    };

    public abstract ContributionType getContributionType();
}
