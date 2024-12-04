package org.grupo11.Api.Controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.ApiResponse;
import org.grupo11.Services.Meal;
import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.FridgeAdmin;
import org.grupo11.Services.Contributions.MealDistribution;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributions.MoneyDonation;
import org.grupo11.Services.Contributions.PersonRegistration;
import org.grupo11.Services.Contributions.RewardContribution;
import org.grupo11.Services.Contributor.Individual;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Rewards.Reward;
import org.grupo11.Services.Rewards.RewardCategory;
import org.grupo11.Services.Rewards.ExchangeRewards.RedeemRequest;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;

import io.javalin.http.Context;
import jakarta.persistence.Tuple;

public class RewardsController {
    
    public static void handleUpdateRewards(Context ctx) {

        try {
            RedeemRequest redeemRequest = ctx.bodyAsClass(RedeemRequest.class);
    
            // Conectar a la base de datos
    
            ctx.status(200).json(new ApiResponse(200, "Rewards redeemed successfully."));
        } catch (Exception e) {
            Logger.error("An error occurred while redeeming rewards.", e);
            ctx.status(500).json(new ApiResponse(500, "An error occurred while redeeming rewards."));
        }

    }
}

