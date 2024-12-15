package org.grupo11.Api.Controllers;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.ApiResponse;
import org.grupo11.Api.Middlewares;
import org.grupo11.Api.JsonData.ExchangeRewards.RedeemRequest;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Utils.FieldValidator;
import org.hibernate.Session;

import io.javalin.http.Context;

public class RewardsController {
    public static void handleUpdateRewards(Context ctx) {
        Contributor contributor = Middlewares.contributorIsAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }

        RedeemRequest redeemRequest = ctx.bodyAsClass(RedeemRequest.class);
        if (redeemRequest == null) {
            ctx.status(400).json(new ApiResponse(400, "Invalid request data."));
            return;
        }

        if (redeemRequest.getUserPoints() < 0) {
            ctx.status(400).json(new ApiResponse(400, "Invalid user points."));
            return;
        }
        for (RedeemRequest.RewardData reward : redeemRequest.getRewardsData()) {
            if (!FieldValidator.isString(reward.getRewardId())) {
                ctx.status(400).json(new ApiResponse(400, "Invalid reward ID."));
                return;
            }
            if (reward.getQuantity() < 0) {
                ctx.status(400).json(new ApiResponse(400, "Invalid reward quantity."));
                return;
            }
        }

        org.hibernate.Transaction transaction = null;
        try (Session session = DB.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Update user points
            String userHQL = "UPDATE Contributor c SET c.points = :points WHERE c.id = :id";
            org.hibernate.query.MutationQuery userQuery = session.createMutationQuery(userHQL);
            userQuery.setParameter("points", redeemRequest.getUserPoints());
            userQuery.setParameter("id", contributor.getId());
            int userUpdateResult = userQuery.executeUpdate();
            if (userUpdateResult == 0) {
                ctx.status(400).json(new ApiResponse(400, "User not found."));
                return;
            }

            // Update reward amount
            for (RedeemRequest.RewardData reward : redeemRequest.getRewardsData()) {
                String rewardHQL = "UPDATE Reward r SET r.quantity = :quantity WHERE r.id = :id";
                org.hibernate.query.MutationQuery rewardQuery = session.createMutationQuery(rewardHQL);
                rewardQuery.setParameter("quantity", reward.getQuantity());
                rewardQuery.setParameter("id", reward.getRewardId());
                int rewardUpdateResult = rewardQuery.executeUpdate();
                if (rewardUpdateResult == 0) {
                    ctx.status(400).json(new ApiResponse(400, "Reward not found."));
                    return;
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
