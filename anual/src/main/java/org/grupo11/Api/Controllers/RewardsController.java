package org.grupo11.Api.Controllers;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.ApiResponse;
import org.grupo11.Api.Middlewares;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Rewards.Reward;
import org.grupo11.Services.Rewards.ExchangeRewards.RedeemRequest;
import org.grupo11.Utils.FieldValidator;
import org.hibernate.Session;

import io.javalin.http.Context;

public class RewardsController {

    public static void handleUpdateRewards(Context ctx) {
        // Obtengo el contributor
        Contributor contributor = Middlewares.contributorIsAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }

        // Obtengo los datos del request
        RedeemRequest redeemRequest = ctx.bodyAsClass(RedeemRequest.class);

        // Valido los datos
        if (redeemRequest.getUserPoints() < 0) {
            throw new IllegalArgumentException("Invalid user points.");
        }
        for (RedeemRequest.RewardData reward : redeemRequest.getRewardsData()) {
            if (!FieldValidator.isString(reward.getRewardId())) {
                throw new IllegalArgumentException("Invalid reward ID: " + reward.getRewardId());
            }
            if (reward.getQuantity() < 0) {
                throw new IllegalArgumentException("Invalid reward quantity for ID: " + reward.getRewardId());
            }
        }

        // Transacción
        org.hibernate.Transaction transaction = null;
        try (Session session = DB.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Actualizo puntos del usuario
            String userHQL = "UPDATE Contributor c SET c.points = :points WHERE c.id = :id";
            org.hibernate.query.Query<Contributor> userQuery = session.createQuery(userHQL);
            userQuery.setParameter("points", redeemRequest.getUserPoints());
            userQuery.setParameter("id", contributor.getId());
            int userUpdateResult = userQuery.executeUpdate();
            if (userUpdateResult == 0) {
                throw new IllegalArgumentException("User not found.");
            }

            // Actualizo cantidades de las recompensas
            for (RedeemRequest.RewardData reward : redeemRequest.getRewardsData()) {
                String rewardHQL = "UPDATE Reward r SET r.quantity = :quantity WHERE r.id = :id";
                org.hibernate.query.Query<Reward> rewardQuery = session.createQuery(rewardHQL);
                rewardQuery.setParameter("quantity", reward.getQuantity());
                rewardQuery.setParameter("id", reward.getRewardId());
                int rewardUpdateResult = rewardQuery.executeUpdate();
                if (rewardUpdateResult == 0) {
                    throw new IllegalArgumentException("Reward not found: " + reward.getRewardId());
                }
            }

            transaction.commit();

            ctx.status(200).json(new ApiResponse(200, "Rewards redeemed successfully."));
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            Logger.error("An error occurred while redeeming rewards.", e);
            ctx.status(500).json(new ApiResponse(500, "Error: " + e.getMessage()));
        }

    }
}
