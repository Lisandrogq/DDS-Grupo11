package org.grupo11.Services;

public abstract class Contribution {

    public void tryContribute(Contributor contributor) {
        if (this.validate(contributor)) {
            contributor.addContribution(this.contribute(contributor));
        }
    }

    protected abstract Contribution contribute(Contributor c);

    protected abstract boolean validate(Contributor contributor);
}
