package org.grupo11.Services.Rewards;

import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.FridgeAdmin;
import org.grupo11.Services.Contributions.MealDistribution;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributions.MoneyDonation;
import org.grupo11.Services.Contributions.PersonRegistration;
import org.grupo11.Services.Contributor.Contributor;

public class RewardPointsCalculator {
    private static double mealDistributionCoef = 1.5;
    private static double fridgeAdminCoef = 2.0;
    private static double mealDonationCoef = 1.0;
    private static double personRegistrationCoef = 1.2;
    private static double moneyDonationCoef = 1.0;

    public static double getMealDistributionCoef() {
        return mealDistributionCoef;
    }

    public static void setMealDistributionCoef(double mealDistributionCoef) {
        RewardPointsCalculator.mealDistributionCoef = mealDistributionCoef;
    }

    public static double getFridgeAdminCoef() {
        return fridgeAdminCoef;
    }

    public static void setFridgeAdminCoef(double fridgeAdminCoef) {
        RewardPointsCalculator.fridgeAdminCoef = fridgeAdminCoef;
    }

    public static double getMealDonationCoef() {
        return mealDonationCoef;
    }

    public static void setMealDonationCoef(double mealDonationCoef) {
        RewardPointsCalculator.mealDonationCoef = mealDonationCoef;
    }

    public static double getPersonRegistrationCoef() {
        return personRegistrationCoef;
    }

    public static void setPersonRegistrationCoef(double personRegistrationCoef) {
        RewardPointsCalculator.personRegistrationCoef = personRegistrationCoef;
    }

    public static double getMoneyDonationCoef() {
        return moneyDonationCoef;
    }

    public static void setMoneyDonationCoef(double moneyDonationCoef) {
        RewardPointsCalculator.moneyDonationCoef = moneyDonationCoef;
    }

    public static double getContributorPoints(Contributor contributor) {
        double totalPoints = 0.0;

        for (Contribution contribution : contributor.getContributions()) {
            double contributionPoints = 0.0;

            if (contribution instanceof MealDistribution) {
                contributionPoints = mealDistributionCoef;
            } else if (contribution instanceof FridgeAdmin) {
                contributionPoints = fridgeAdminCoef;
            } else if (contribution instanceof MealDonation) {
                contributionPoints = mealDonationCoef;
            } else if (contribution instanceof PersonRegistration) {
                contributionPoints = personRegistrationCoef;
            } else if (contribution instanceof MoneyDonation) {
                MoneyDonation moneyDonation = (MoneyDonation) contribution;
                contributionPoints = moneyDonation.getAmount() * moneyDonationCoef;
            }
            totalPoints += contributionPoints;
        }

        return totalPoints;
    }
}