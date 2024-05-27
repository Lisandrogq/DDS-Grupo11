package org.grupo11.app;

import org.grupo11.Utils.PasswordValidator;
import org.grupo11.Services.Card.CardManager;
import org.grupo11.Services.Contributions.ContributionsManager;
import org.grupo11.Services.Contributor.ContributorsManager;
import org.grupo11.Services.Fridge.FridgesManager;
import org.grupo11.Services.PersonInNeed.PersonInNeedManager;
import org.grupo11.Services.Rewards.RewardSystem;
import org.grupo11.Services.Technician.TechnicianManager;

public class App {

    private ContributorsManager contributorManager;
    private ContributionsManager contributionsManager;
    private FridgesManager fridgesManager;
    private PersonInNeedManager personInNeedManager;
    private TechnicianManager technicianManager;
    private CardManager cardManager;
    private RewardSystem rewardSystem;

    public App() {
        contributorManager = new ContributorsManager();
        contributionsManager = new ContributionsManager();
        fridgesManager = new FridgesManager();
        personInNeedManager = new PersonInNeedManager();
        technicianManager = new TechnicianManager();
        cardManager = new CardManager();
        rewardSystem = new RewardSystem();
    }

    public ContributorsManager getContributorManager() {
        return contributorManager;
    }

    public ContributionsManager getContributionsManager() {
        return contributionsManager;
    }

    public FridgesManager getFridgesManager() {
        return fridgesManager;
    }

    public PersonInNeedManager getPersonInNeedManager() {
        return personInNeedManager;
    }

    public TechnicianManager getTechnicianManager() {
        return technicianManager;
    }

    public CardManager getCardManager() {
        return cardManager;
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
}
