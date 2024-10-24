package org.grupo11.Services.Contributions;

import org.grupo11.Services.Meal;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.FridgeOpenLogEntry;
import org.grupo11.Utils.DateUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class MealDonation extends Contribution {
    @OneToOne
    private Meal meal;

    public MealDonation() {
    }

    public MealDonation(Meal meal, long date) {
        super(date);
        this.meal = meal;
    }

    @Override
    public boolean validate(Contributor contributor) {
        return (super.validate(contributor)
                && this.meal.getFridge().hasPermission(contributor.getContributorRegistry().getId()));
    }

    @Override
    public void afterContribution() {
        this.meal.getFridge()
                .addOpenEntry(new FridgeOpenLogEntry(DateUtils.getCurrentTimeInMs(),
                        this.contributor.getContributorRegistry()));
    }

    public ContributionType getContributionType() {
        return ContributionType.MEAL_DONATION;
    }

    @Override
    public void setContributor(Contributor contributor) {
        super.setContributor(contributor);
        meal.setContributor(contributor);
    }

    public Meal getMeal() {
        return this.meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    @Override
    public double getRewardPoints() {
        return 1.0;
    }

}
