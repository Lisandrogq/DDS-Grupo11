package org.grupo11.Services;

import org.grupo11.Services.ActivityRegistry.ContributorRegistry;
import org.grupo11.Services.Contributions.ContributionType;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Contributor.ContributorsManager;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeSolicitude;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


import org.junit.Before;

public class ContributionsTest {

    Fridge fridge = null;
    Contributor contributor1 = null;
    ContributorRegistry contributorRegistry = null;
    Contributor contributor2 = null;
    Meal meal = null;

    @Before
    public void setUp() {
        List<ContributionType> permisos = new ArrayList<ContributionType>();
        permisos.add(ContributionType.MEAL_DONATION);
        contributor1 = new Contributor("aa", "null", permisos);
        contributor2 = new Contributor("nb", "null", new ArrayList<ContributionType>());
        fridge = new Fridge(-74.006, 40.7128, "Caballito", "Fridge A", 100, 2020, null, null, null);
        meal = new Meal("fidios", 0, 0, fridge, "nuevo", 123, 33);
        contributorRegistry = new ContributorRegistry(0, contributor1, new ArrayList<FridgeSolicitude>());
        contributor1.setContributorRegistry(contributorRegistry);

    }

    @Test
    public void MealsCanBeAddedByContributors() throws InterruptedException {
        contributor1.getContributorRegistry().registerPermission(fridge);
        MealDonation contribution = new MealDonation(meal, 0);
        Boolean result = ContributorsManager.getInstance().addContributionToContributor(contributor1, contribution);
        assertTrue("Contributor should be able to contribute if it has permission and register de opening", result);
    }

    @Test
    public void MealsOnlyCantBeAddedByContributorsWithOutRegister() throws InterruptedException {
       // contributor1.getContributorRegistry().registerPermission(fridge);

        MealDonation contribution = new MealDonation(meal, 0);
        Boolean result = ContributorsManager.getInstance().addContributionToContributor(contributor1, contribution);
        assertFalse("Contribution should not be allowed without previous register", result);
    }

    @Test
    public void MealsOnlyCanBeAddedByContributorsWithPermission() throws InterruptedException {
        contributor1.setPossibleContributions(new ArrayList<ContributionType>()); //removing all permissions
        contributor1.getContributorRegistry().registerPermission(fridge);
        MealDonation contribution = new MealDonation(meal, 0);
        Boolean result = ContributorsManager.getInstance().addContributionToContributor(contributor1, contribution);
        assertFalse("Contribution should not be allowed without permission", result);
    }



}