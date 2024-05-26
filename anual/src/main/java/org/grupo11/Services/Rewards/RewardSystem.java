package org.grupo11.Services.Rewards;

import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.FridgeAdmin;
import org.grupo11.Services.Contributions.MealDistribution;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributions.MoneyDonation;
import org.grupo11.Services.Contributions.PersonRegistration;
import org.grupo11.Services.Contributor.Contributor;

public class RewardSystem {
    static double mealDistributionCoef = 1.5;
    static double fridgeAdminCoef = 2.0;
    static double mealDonationCoef = 1.0;
    static double personRegistrationCoef = 1.2;
    static double moneyDonationCoef = 1.0;

    public static double getContributorPoints(Contributor contributor) {
        double totalPoints = 0.0;

        for (Contribution contribution : contributor.contributions) {
            double contributionPoints = 0.0;

            if (contribution instanceof MealDistribution) {
                contributionPoints = mealDistributionCoef;
            }
            if (contribution instanceof FridgeAdmin) {
                contributionPoints = 0;
            }
            if (contribution instanceof MealDonation) {
                contributionPoints = 1 * mealDonationCoef;
            }
            if (contribution instanceof PersonRegistration) {
                contributionPoints = personRegistrationCoef;
            }
            if (contribution instanceof MoneyDonation) {
                MoneyDonation moneyDonation = (MoneyDonation) contribution;
                contributionPoints = moneyDonation.amount * moneyDonationCoef;
            }
            totalPoints += contributionPoints;
        }

        return totalPoints;
    }
}
