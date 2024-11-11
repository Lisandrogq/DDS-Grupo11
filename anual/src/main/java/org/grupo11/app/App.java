package org.grupo11.app;

import org.grupo11.Utils.PasswordValidator;
import org.grupo11.DB;
import org.grupo11.DataImporter;
import org.grupo11.Logger;
import org.grupo11.Metrics;
import org.grupo11.Api.Api;
import org.grupo11.Broker.Rabbit;
import org.grupo11.Services.ActivityRegistry.RegistryManager;
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
    private RegistryManager cardsManager;
    private RewardSystem rewardSystem;

    public static void main(String[] args) {
        // contributorsManager = ContributorsManager.getInstance();
        // contributionsManager = ContributionsManager.getInstance();
        // fridgesManager = FridgesManager.getInstance();
        // personsInNeedManager = PersonInNeedManager.getInstance();
        // technicianManager = TechnicianManager.getInstance();
        // cardsManager = RegistryManager.getInstance();
        // rewardSystem = RewardSystem.getInstance();
        try {
            DB.getSessionFactory();
            Rabbit.getInstance().connect();
            Api api = new Api(8000);
            api.start();
        } catch (Exception e) {
            Logger.error("Could not start app {}", e);
        }

        try {
            Metrics.getInstance().startMetricsServer();
        } catch (Exception e) {
            Logger.error("Could not start metrics server", e);
        }
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

    public RegistryManager getCardsManager() {
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
