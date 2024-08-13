package org.grupo11.app;

import org.grupo11.Utils.PasswordValidator;
import org.grupo11.Services.DataImporter;
import org.grupo11.Services.ActivityRegistry.CardManager;
import org.grupo11.Services.Contributions.ContributionsManager;
import org.grupo11.Services.Contributor.ContributorsManager;
import org.grupo11.Services.Fridge.FridgesManager;
import org.grupo11.Services.PersonInNeed.PersonInNeedManager;
import org.grupo11.Services.Rewards.RewardSystem;
import org.grupo11.Services.Technician.TechnicianManager;

public class App {
    private ContributorsManager contributorsManager;
    private ContributionsManager contributionsManager;
    private FridgesManager fridgesManager;
    private PersonInNeedManager personsInNeedManager;
    private TechnicianManager technicianManager;
    private CardManager cardsManager;
    private RewardSystem rewardSystem;

    public App() {
        contributorsManager = ContributorsManager.getInstance();
        contributionsManager = ContributionsManager.getInstance();
        fridgesManager = FridgesManager.getInstance();
        personsInNeedManager = PersonInNeedManager.getInstance();
        technicianManager = TechnicianManager.getInstance();
        cardsManager = CardManager.getInstance();
        rewardSystem = RewardSystem.getInstance();
    }

    public ContributorsManager getContributorsManager() {
        return contributorsManager;
    }

    public ContributionsManager getContributionsManager() {
        return contributionsManager;
    }

    public FridgesManager getFridgesManager() {
        return fridgesManager;
    }

    public PersonInNeedManager getPersonsInNeedManager() {
        return personsInNeedManager;
    }

    public TechnicianManager getTechnicianManager() {
        return technicianManager;
    }

    public CardManager getCardsManager() {
        return cardsManager;
    }

    public RewardSystem getRewardSystem() {
        return rewardSystem;
    }

    public void ValidatePassword(String pw) {
        PasswordValidator.Result result = PasswordValidator.ValidatePassword(pw);
        if (!result.valid) {
            System.out.println("Your password is invalid, reason: " + result.reasons.toString());
            return;
        }
        System.out.println("Password is valid");
    }

    public void bulkContributionsImport(String cvsFileName) {
        new DataImporter(contributionsManager, contributorsManager).loadContributors(cvsFileName);
    }
}
