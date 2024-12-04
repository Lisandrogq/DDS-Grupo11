package org.grupo11.Api.Controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import io.javalin.http.Context;

public class RenderController {
    public static void renderRegisterPages(Context ctx) {
        try {
            String filename = ctx.pathParam("filename");
            Path filePath = Paths.get("src/main/resources/templates/register/", filename + ".html");
            String error = ctx.queryParam("error");
            Map<String, Object> model = new HashMap<>();
            model.put("error", error);

            if (Files.exists(filePath)) {
                ctx.render("templates/register/" + filename + ".html", model);
            } else {
                ctx.status(404);
            }
        } catch (Exception e) {
            ctx.status(500);
        }
    }

    // TODO(marcos): here we should check if the user is authenticated
    // otherwise redirect to login
    public static void renderDashboardPage(Context ctx) {
        try {
            // user
            Map<String, Object> user = new HashMap<>();
            user.put("name", "John");
            user.put("points", 1213);

            // temperature
            List<Map<String, Object>> temperatures = new ArrayList<>();
            Map<String, Object> fridge1Temp = new HashMap<>();
            fridge1Temp.put("fridge", "Medrano");
            fridge1Temp.put("temp", 10);

            Map<String, Object> fridge2Temp = new HashMap<>();
            fridge2Temp.put("fridge", "Lugano");
            fridge2Temp.put("temp", 15);

            temperatures.add(fridge1Temp);
            temperatures.add(fridge2Temp);

            // donations
            List<Map<String, Object>> donations = new ArrayList<>();
            Map<String, Object> donation1 = new HashMap<>();
            Map<String, Object> donatedFridge = new HashMap<>();
            donatedFridge.put("name", "Medrano");
            donatedFridge.put("temp", 3);
            donatedFridge.put("reserved", 40);
            donatedFridge.put("state", "Active");
            donatedFridge.put("meals", 120);

            donation1.put("emoji", "🥘");
            donation1.put("type", "Food");
            donation1.put("desc", "You have donated “musaka”");
            donation1.put("fridge", donatedFridge);

            Map<String, Object> donation2 = new HashMap<>();
            donation2.put("emoji", "👨");
            donation2.put("type", "Registered person");
            donation2.put("desc", "You have registered “valentina” as part of our community");
            donation2.put("fridge", donatedFridge);

            donations.add(donation1);
            donations.add(donation2);

            // fridges
            List<Map<String, Object>> fridges = new ArrayList<>();
            Map<String, Object> fridge1 = new HashMap<>();
            fridge1.put("name", "Medrano");
            fridge1.put("temp", 3);
            fridge1.put("reserved", 40);
            fridge1.put("state", "Active");
            fridge1.put("meals", 120);
            fridge1.put("food_status_desc", "Medrano fridge needs food");
            fridge1.put("meal_urgency", "High");

            Map<String, Object> fridge2 = new HashMap<>();
            fridge2.put("name", "Lugano");
            fridge2.put("temp", 3);
            fridge2.put("reserved", 40);
            fridge2.put("state", "Active");
            fridge2.put("meals", 120);
            fridge2.put("food_status_desc", "Lugano fridge needs food");
            fridge2.put("meal_urgency", "Medium");

            Map<String, Object> fridge3 = new HashMap<>();
            fridge3.put("name", "Mataderos");
            fridge3.put("temp", 3);
            fridge3.put("reserved", 40);
            fridge3.put("state", "Active");
            fridge3.put("meals", 120);
            fridge3.put("food_status_desc", "Lugano fridge needs food");
            fridge3.put("meal_urgency", "Low");

            fridges.add(fridge1);
            fridges.add(fridge2);
            fridges.add(fridge3);

            // rewards
            List<Map<String, Object>> rewards = new ArrayList<>();
            Map<String, Object> reward1 = new HashMap<>();

            reward1.put("emoji", "🍴");
            reward1.put("category", "Cooking");
            reward1.put("description", "You can exchange your points for useful cooking supplies");

            Map<String, Object> reward2 = new HashMap<>();
            reward2.put("emoji", "🛋️");
            reward2.put("category", "Home");
            reward2.put("description", "You can exchange your points for nice home supplies");

            Map<String, Object> reward3 = new HashMap<>();
            reward3.put("emoji", "🎮");
            reward3.put("category", "Technology");
            reward3.put("description", "You can exchange your points for trendy technological devices");

            rewards.add(reward1);
            rewards.add(reward2);
            rewards.add(reward3);

            // final model
            Map<String, Object> model = new HashMap<>();
            model.put("user", user);
            model.put("temperatures", temperatures);
            model.put("donations", donations);
            model.put("fridges", fridges);
            model.put("rewards", rewards);

            ctx.render("templates/dash/home.html", model);
        } catch (

        Exception e) {
            ctx.status(500);
        }
    }
}
