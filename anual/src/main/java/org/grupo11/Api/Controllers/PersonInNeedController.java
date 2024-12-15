package org.grupo11.Api.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.ApiResponse;
import org.grupo11.Services.Meal;
import org.grupo11.Services.ActivityRegistry.PINRegistry;
import org.grupo11.Services.ActivityRegistry.RegistryManager;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeNotification;
import org.grupo11.Services.Fridge.FridgeOpenLogEntry;
import org.grupo11.Utils.DateUtils;
import org.hibernate.Session;

import io.javalin.http.Context;

class WithdrawMealDTO {
    public static class CardDTO {
        public String number;
        public String security_code;
    }

    public static class MealDTO {
        public Long id;
    }

    public int fridge_id;
    public CardDTO card;
    public List<MealDTO> meals;
}

public class PersonInNeedController {
    public static void handleWithdrawMeal(Context ctx) {
        try {
            WithdrawMealDTO body = ctx.bodyAsClass(WithdrawMealDTO.class);
            PINRegistry registry = RegistryManager.getInstance().queryPinRegistryByNumberAndSecurityCode(
                    body.card.number,
                    body.card.security_code);
            if (registry == null) {
                ctx.status(400).json(new ApiResponse(400, "The given card does not exist"));
                return;
            }
            Session session = DB.getSessionFactory().openSession();
            Fridge fridge = session.find(Fridge.class, body.fridge_id);
            if (fridge == null) {
                ctx.status(400).json(new ApiResponse(400, "The given fridge does not exist"));
                return;
            }

            List<Meal> meals = new ArrayList<>();
            for (WithdrawMealDTO.MealDTO mealDto : body.meals) {
                Meal meal = fridge.getMealByID(mealDto.id);
                meals.add(meal);
                if (meal == null) {
                    ctx.status(400).json(new ApiResponse(400, "Meal with id " + mealDto.id + " does not exist"));
                    return;
                }
            }
            // make sure the person has enough usages for all the meals
            if (!registry.canUseCardByQuantity(meals.size())) {
                ctx.status(400).json(new ApiResponse(400,
                        "You don't have enough usages, needed: " + meals.size()
                                + ", you have: " + registry.getUsagesLeftForToday()));
                return;
            }

            // if the validation goes ok, now we retrieve the meals from the fridge and
            // record the card usage
            for (Meal meal : meals) {
                FridgeNotification notification = fridge.removeMeal(meal);
                DB.create(notification);
                registry.useCard(fridge);
            }
            FridgeOpenLogEntry log = new FridgeOpenLogEntry(fridge, DateUtils.now(), registry);
            DB.create(log);
            fridge.addOpenEntry(log);
            DB.update(fridge);
            DB.update(registry);
            session.close();
            ctx.status(200).json(new ApiResponse(200));
        } catch (Exception e) {
            Logger.error("error ", e);
            ctx.status(500).json(new ApiResponse(500));
        }

    }

    public static void handleGetUsages(Context ctx) {
        WithdrawMealDTO body = ctx.bodyAsClass(WithdrawMealDTO.class);
        try {
            PINRegistry registry = RegistryManager.getInstance().queryPinRegistryByNumberAndSecurityCode(
                    body.card.number,
                    body.card.security_code);
            if (registry == null) {
                ctx.status(400).json(new ApiResponse(400, "The given card does not exist"));
                return;
            }
            Map<String, Integer> response = new HashMap<>();
            response.put("usages", registry.getUsages().size());
            response.put("usages_today", registry.getTodayUsages().size());
            response.put("usage_left_today", registry.getUsagesLeftForToday());
            ctx.status(200).json(new ApiResponse(200, response));
        } catch (Exception e) {
            ctx.status(500).json(new ApiResponse(500));
        }
    }
}
