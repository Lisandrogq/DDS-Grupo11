package org.grupo11.Services.Contributions;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Meal;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeOpenLogEntry;
import org.grupo11.Utils.DateUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class MealDonation extends Contribution {
    @OneToOne
    private Meal meal;
    @ManyToOne
    private Fridge fridge = null;

    public MealDonation() {
    }

    public MealDonation(Meal meal, long date) {
        super(date);
        this.meal = meal;
    }

    public MealDonation(Meal meal, long date, Fridge fridge) {
        super(date);
        this.meal = meal;
        this.fridge = fridge;
    }

    public Fridge getFridge() {
        return this.fridge;
    }

    @Override
    public boolean validate(Contributor contributor) {
        return (super.validate(contributor)
                && (true || this.meal.getFridge().hasPermission(contributor.getContributorRegistry().getId())));
        // TODO: implement open solicitudes with db and front
    }

    @Override
    public List<FridgeOpenLogEntry> afterContribution() {
        FridgeOpenLogEntry entry = new FridgeOpenLogEntry(DateUtils.getCurrentTimeInMs(),
                this.contributor.getContributorRegistry());

        this.meal.getFridge()
                .addOpenEntry(entry);
        List<FridgeOpenLogEntry> entries = new ArrayList<FridgeOpenLogEntry>();
        entries.add(entry);
        return entries;
    }

    public ContributionType getContributionType() {
        return ContributionType.MEAL_DONATION;
    }

    @Override
    public void setContributor(Contributor contributor) {
        super.setContributor(contributor);
        // meal.setContributor(contributor); las bidireccionalidades son re de trolo
    }

    public Meal getMeal() {
        return this.meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    @Override
    public double getRewardPoints() {
        return 1.5;
    }

}
