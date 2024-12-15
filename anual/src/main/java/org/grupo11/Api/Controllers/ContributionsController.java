package org.grupo11.Api.Controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.io.File;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Api.ApiResponse;
import org.grupo11.Api.Middlewares;
import org.grupo11.Services.Meal;
import org.grupo11.Services.Contributions.FridgeAdmin;
import org.grupo11.Services.Contributions.MealDistribution;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributions.MoneyDonation;
import org.grupo11.Services.Contributions.PersonRegistration;
import org.grupo11.Services.Contributions.RewardContribution;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Contributor.ContributorsManager;
import org.grupo11.Services.Contributor.LegalEntity.LegalEntity;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeNotification;
import org.grupo11.Services.Fridge.FridgeOpenLogEntry;
import org.grupo11.Services.Fridge.FridgeSolicitude;
import org.grupo11.Services.Fridge.Sensor.MovementSensorManager;
import org.grupo11.Services.Fridge.Sensor.TemperatureSensorManager;
import org.grupo11.Services.PersonInNeed.PersonInNeed;
import org.grupo11.Services.Rewards.Reward;
import org.grupo11.Services.Rewards.RewardCategory;
import org.grupo11.Utils.DateUtils;
import org.grupo11.Utils.FieldValidator;
import org.hibernate.Session;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

public class ContributionsController {

    public static void handleMealContribution(Context ctx) {
        Contributor contributor = Middlewares.contributorIsAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }
        if (contributor instanceof LegalEntity) {
            ctx.redirect("/dash/home?error=You can't donate meals as a legal entity");
            return;
        }

        String type = ctx.formParam("type");
        String expirationDate = ctx.formParam("expirationDate");
        String fridge_id = ctx.formParam("fridge_id");
        String calories = ctx.formParam("calories");
        String weight = ctx.formParam("weight");

        Consumer<String> sendFormError = (msg) -> {
            ctx.status(400)
                    .json(new ApiResponse(400, msg, null));
            ctx.redirect("/dash/home?error=" + msg);
        };

        if (!FieldValidator.isString(type)) {
            sendFormError.accept("invalid type");
            return;
        }
        if (!FieldValidator.isInt(fridge_id)) {
            sendFormError.accept("invalid fridge_address");
            return;
        }
        if (!FieldValidator.isDate(expirationDate)) {
            sendFormError.accept("invalid expirationDate");
            return;
        }
        if (!FieldValidator.isInt(calories)) {
            sendFormError.accept("invalid calories");
            return;
        }
        if (!FieldValidator.isInt(weight)) {
            sendFormError.accept("invalid weight");
            return;
        }
        if (Integer.parseInt(weight) <= 0) {
            ctx.redirect("/dash/home?error=Enter a valid weight amount");
            return;
        }
        if (Integer.parseInt(calories) <= 0) {
            ctx.redirect("/dash/home?error=Enter a valid calories amount");
            return;
        }
        try {
            if (DateUtils.parseDateYMDString(expirationDate) < DateUtils.now()) {
                ctx.redirect("/dash/home?error=Yout can't donate expired meals");
                return;
            }
        } catch (ParseException e) {
            ctx.redirect("/dash/home?error=Invalid expiration date format");
            return;
        }

        System.err.println(ctx.body());

        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT f " +
                    "FROM Fridge f " +
                    "WHERE f.id = :fridge_id";
            org.hibernate.query.Query<Fridge> query = session.createQuery(hql, Fridge.class);
            query.setParameter("fridge_id", fridge_id);
            Fridge fridge = query.uniqueResult();

            if (fridge == null) {
                ctx.redirect("/dash/home?error=The fridge does not exist");
                return;
            }
            if (fridge.getIsActive() == false) {
                ctx.redirect("/dash/home?error=The fridge is not active");
                return;
            }
            if (fridge.getMeals().size() >= fridge.getCapacity()) {
                ctx.redirect("/dash/home?error=The fridge is full");
                return;
            }
            Meal meal = new Meal(type, DateUtils.parseDateYMDString(expirationDate), DateUtils.now(), fridge, "",
                    Integer.parseInt(calories), Integer.parseInt(weight));
            MealDonation mealDonation = new MealDonation(meal, DateUtils.now(), fridge);

            if (!fridge.hasSolicitude(contributor.getContributorRegistry().getId())) {
                FridgeSolicitude fridgeSolicitude = new FridgeSolicitude(contributor.getContributorRegistry(),
                        DateUtils.now(), fridge);
                contributor.getContributorRegistry().registerPermission(fridge);
                DB.create(fridgeSolicitude);
            }

            mealDonation.setContributor(contributor);
            List<FridgeOpenLogEntry> entries = ContributorsManager.getInstance().addContributionToContributor(
                    contributor,
                    mealDonation);
            if (entries == null) {
                ctx.redirect("/dash/home?error=Somenthing went wrong");
                return;
            }
            DB.create(meal);
            DB.create(mealDonation);
            DB.create(entries.get(0));
            DB.update(contributor);
            FridgeNotification notification = fridge.addMeal(meal);
            DB.create(notification);
            DB.update(fridge);
            ctx.redirect("/dash/home");

        } catch (Exception e) {
            Logger.error("Exception ", e);
            ctx.redirect("/dash/home?error=" + e.getMessage());
            return;
        }
    }

    public static void handleMealDistributionContribution(Context ctx) {
        Contributor contributor = Middlewares.contributorIsAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }

        String reason = ctx.formParam("reason");
        String origin_id = ctx.formParam("origin_id");
        String destiny_id = ctx.formParam("destiny_id");

        try (Session session = DB.getSessionFactory().openSession()) {

            if (!FieldValidator.isString(reason)) {
                ctx.redirect("/dash/home?error=invalid reason");
                return;
            }
            if (!FieldValidator.isInt(origin_id)) {
                ctx.redirect("/dash/home?error=invalid origin_id");
                return;
            }
            if (!FieldValidator.isInt(destiny_id)) {
                ctx.redirect("/dash/home?error=invalid destiny_id");
                return;
            }

            String origin_hql = "SELECT f " +
                    "FROM Fridge f " +
                    "WHERE f.id = :origin_id";
            org.hibernate.query.Query<Fridge> origin_query = session.createQuery(origin_hql, Fridge.class);
            origin_query.setParameter("origin_id", origin_id);
            String destiny_hql = "SELECT f " +
                    "FROM Fridge f " +
                    "WHERE f.id = :destiny_id";
            org.hibernate.query.Query<Fridge> destiny_query = session.createQuery(destiny_hql, Fridge.class);
            destiny_query.setParameter("destiny_id", destiny_id);

            Fridge origin_fridge = origin_query.getSingleResult();
            Fridge destiny_fridge = destiny_query.getSingleResult();
            if (origin_fridge == null) {
                ctx.redirect("/dash/home?error=The origin fridge does not exist");
                return;
            }
            if (destiny_fridge == null) {
                ctx.redirect("/dash/home?error=The destiny fridge does not exist");
                return;
            }
            if (origin_fridge.getIsActive() == false) {
                ctx.redirect("/dash/home?error=The origin fridge is not active");
                return;
            }
            if (destiny_fridge.getIsActive() == false) {
                ctx.redirect("/dash/home?error=The destiny fridge is not active");
                return;
            }

            if (!origin_fridge.hasSolicitude(contributor.getContributorRegistry().getId())) {
                FridgeSolicitude fridgeSolicitude = new FridgeSolicitude(contributor.getContributorRegistry(),
                        DateUtils.now(), origin_fridge);
                contributor.getContributorRegistry().registerPermission(origin_fridge);
                DB.create(fridgeSolicitude);
            }
            if (!destiny_fridge.hasSolicitude(contributor.getContributorRegistry().getId())) {
                FridgeSolicitude fridgeSolicitude = new FridgeSolicitude(contributor.getContributorRegistry(),
                        DateUtils.now(), destiny_fridge);
                contributor.getContributorRegistry().registerPermission(destiny_fridge);
                DB.create(fridgeSolicitude);
            }

            int i = 0;
            int max = 0;
            for (i = 0; i < 10; i++) {
                String meal_id = ctx.formParam("meal_" + i);
                if (meal_id != null) {
                    Meal meal = origin_fridge.getMealByID(Long.parseLong(meal_id));
                    max++;
                    if (meal == null) {
                        ;
                        ctx.redirect("/dash/home?error=The meal " + meal_id + " does not exist in the origin fridge");
                        return;
                    }
                }
            }
            if (max == 0) {
                ctx.redirect("/dash/home?error=Select at least one meal");
                return;
            }
            if (destiny_fridge.getMeals().size() + max > destiny_fridge.getCapacity()) {
                int spacesLeft = destiny_fridge.getCapacity() - destiny_fridge.getMeals().size();
                if (spacesLeft == 0) {
                    ctx.redirect("/dash/home?error=The destiny fridge is full");
                } else {
                    ctx.redirect("/dash/home?error=The destiny fridge is full. There are only " + spacesLeft
                            + " spaces left");
                }
                return;
            }
            MealDistribution mealDistribution = new MealDistribution(origin_fridge, destiny_fridge, max, reason,
                    DateUtils.now());
            List<FridgeOpenLogEntry> entries = ContributorsManager.getInstance().addContributionToContributor(
                    contributor,
                    mealDistribution);
            if (entries == null) {
                ctx.redirect("/dash/home?error=Somenthing went wrong");
                return;
            }
            DB.create(entries.get(0));
            DB.create(entries.get(1));
            for (i = 0; i < max; i++) {
                String meal_id = ctx.formParam("meal_" + i);
                if (meal_id != null) {
                    Meal meal = origin_fridge.getMealByID(Long.parseLong(meal_id));
                    FridgeNotification notification_ori = origin_fridge.removeMeal(meal);
                    FridgeNotification notification_des = destiny_fridge.addMeal(meal);
                    DB.create(notification_ori);
                    DB.create(notification_des);
                    meal.setFridge(destiny_fridge);
                    DB.update(meal);

                }
            }
            DB.update(origin_fridge);
            DB.update(destiny_fridge);
            mealDistribution.setContributor(contributor);
            DB.create(mealDistribution);
            DB.update(contributor);

            ctx.redirect("/dash/home");

        } catch (Exception e) {
            ctx.redirect("/dash/home?error=" + e.getMessage());
            return;
        }

    }

    public static void handlFridgeAdministrationContribution(Context ctx) {
        Contributor contributor = Middlewares.contributorIsAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }
        String name = ctx.formParam("fridgeName");
        String address = ctx.formParam("address");
        String capacity = ctx.formParam("capacity");
        String city = ctx.formParam("city");
        if (!FieldValidator.isString(name)) {
            ctx.redirect("/dash/home?error=Enter a valid name");
            return;
        }
        if (!FieldValidator.isValidAddress(address)) {
            ctx.redirect("/dash/home?error=Enter a valid address (it must be like 'Street 1234')");
            return;
        }
        if (!FieldValidator.isInt(capacity)) {
            ctx.redirect("/dash/home?error=Enter a valid capacity amount");
            return;
        }
        if (Integer.parseInt(capacity) <= 0) {
            ctx.redirect("/dash/home?error=Enter a valid capacity amount");
            return;
        }
        if (!FieldValidator.isString(city)) {
            ctx.redirect("/dash/home?error=Enter a valid city");
            return;
        }

        address = address + ", " + city + ", Argentina ";// this is done so the map always work with real addresses in
                                                         // argentina
        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT f " +
                    "FROM Fridge f " +
                    "WHERE f.address = :address";
            org.hibernate.query.Query<Fridge> query = session.createQuery(hql, Fridge.class);
            query.setParameter("address", address);
            Fridge fridge = query.uniqueResult();
            if (fridge != null) {
                ctx.redirect("/dash/home?error=There is already a fridge with that address");
                return;
            }

            String leHQL = "SELECT le " +
                    "FROM LegalEntity le " +
                    "WHERE le.id = :contributor_id";
            org.hibernate.query.Query<LegalEntity> leQuery = session.createQuery(leHQL, LegalEntity.class);
            leQuery.setParameter("contributor_id", contributor.getId());
            LegalEntity le = leQuery.uniqueResult();
            if (le == null) {
                ctx.redirect("/dash/home?error=You must be a legal entity to create a fridge");
                return;
            }

            fridge = new Fridge(address, name, Integer.parseInt(capacity), 0, new ArrayList<>(), null, null);
            DB.create(fridge);
            TemperatureSensorManager tManager = new TemperatureSensorManager(fridge, -1, 60);
            MovementSensorManager mManager = new MovementSensorManager(fridge);
            fridge.setIsActive(true);
            fridge.setTempManager(tManager);
            fridge.setMovManager(mManager);
            tManager.setFridge(fridge);
            mManager.setFridge(fridge);

            FridgeAdmin fridgeAdmin = new FridgeAdmin(le, fridge, DateUtils.now());
            fridgeAdmin.setContributor(contributor);

            List<FridgeOpenLogEntry> entries = ContributorsManager.getInstance().addContributionToContributor(
                    contributor,
                    fridgeAdmin);
            if (entries == null) {
                ctx.redirect("/dash/home?error=Somenthing went wrong");
                return;
            }
            DB.create(tManager);
            DB.create(mManager);
            DB.update(fridge);
            DB.create(fridgeAdmin);
            DB.update(contributor);

            ctx.redirect("/dash/home");
        } catch (Exception e) {
            Logger.error("Exception ", e);
            ctx.redirect("/dash/home?error=" + e.getMessage());
            return;
        }

    }

    public static void handleMoneyContribution(Context ctx) {
        Contributor contributor = Middlewares.contributorIsAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }
        String amount = ctx.formParam("amount");
        String message = ctx.formParam("message");

        if (!FieldValidator.isInt(amount)) {
            ctx.redirect("/dash/home?error=Enter a valid amount");
            return;

        }
        if (!FieldValidator.isString(message)) {
            ctx.redirect("/dash/home?error=Enter a valid message");
            return;
        }
        if (Integer.parseInt(amount) <= 0) {
            ctx.redirect("/dash/home?error=Enter a valid amount");
            return;
        }

        try {
            MoneyDonation moneyDonation = new MoneyDonation(Integer.parseInt(amount), DateUtils.now(), message);
            moneyDonation.setContributor(contributor);
            List<FridgeOpenLogEntry> entries = ContributorsManager.getInstance().addContributionToContributor(
                    contributor,
                    moneyDonation);
            if (entries == null) {
                ctx.redirect("/dash/home?error=Somenthing went wrong");
                return;
            }
            DB.create(moneyDonation);
            DB.update(contributor);
            ctx.redirect("/dash/home");
        } catch (Exception e) {
            Logger.error("Exception ", e);
            ctx.redirect("/dash/home?error=" + e.getMessage());
            return;
        }
    }

    public static void handlePersonRegistrationContribution(Context ctx) {
        Contributor contributor = Middlewares.contributorIsAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }

        String name = ctx.formParam("personName");
        String dni = ctx.formParam("dni");
        String birth = ctx.formParam("birth");
        String children_count = ctx.formParam("children_count");

        if (!FieldValidator.isString(name)) {
            ctx.redirect("/dash/home?error=Enter a valid name");
            return;
        }
        if (!FieldValidator.isDate(birth)) {
            ctx.redirect("/dash/home?error=Enter a valid birth date");
            return;
        }
        if (!FieldValidator.isInt(dni)) {
            ctx.redirect("/dash/home?error=Enter a valid dni");
            return;
        }
        if (!FieldValidator.isInt(children_count) || Integer.parseInt(children_count) < 0) {
            ctx.redirect("/dash/home?error=Enter a valid children count");
            return;
        }

        try {
            PersonInNeed PIN = new PersonInNeed(name, DateUtils.parseDateYMDString(birth), DateUtils.now(), "",
                    Integer.parseInt(dni), Integer.parseInt(children_count), null);
            PersonRegistration personRegistration = new PersonRegistration(PIN, DateUtils.now(), contributor);
            personRegistration.setContributor(contributor);
            List<FridgeOpenLogEntry> entries = ContributorsManager.getInstance().addContributionToContributor(
                    contributor,
                    personRegistration);
            if (entries == null) {
                ctx.redirect("/dash/home?error=Somenthing went wrong");
                return;
            }
            DB.create(PIN);
            DB.create(personRegistration);
            DB.update(contributor);
            ctx.redirect("/dash/home");
        } catch (Exception e) {
            Logger.error("Exception ", e);
            ctx.redirect("/dash/home?error=" + e.getMessage());
            return;
        }
    }

    public static void handleRewardContribution(Context ctx) {
        Contributor contributor = Middlewares.contributorIsAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }

        String name = ctx.formParam("rewardName");
        String stock = ctx.formParam("stock");
        String description = ctx.formParam("description");
        String points = ctx.formParam("points");
        String category = ctx.formParam("category");
        UploadedFile picture = ctx.uploadedFile("picture");

        if (!FieldValidator.isString(name)) {
            ctx.redirect("/dash/home?error=Enter a valid name");
            return;
        }
        if (!FieldValidator.isString(description)) {
            ctx.redirect("/dash/home?error=Enter a valid description");
            return;
        }
        if (!FieldValidator.isInt(stock) || Integer.parseInt(stock) < 0) {
            ctx.redirect("/dash/home?error=Enter a valid stock amount");
            return;
        }
        if (!FieldValidator.isInt(points) || Integer.parseInt(points) < 0) {
            ctx.redirect("/dash/home?error=Enter a valid points amount");
            return;
        }
        if (!FieldValidator.isValidEnumValue(RewardCategory.class, category)) {
            ctx.redirect("/dash/home?error=Enter a valid category");
            return;
        }
        if (picture != null && picture.size() > 0) {
            if (!picture.contentType().contains("image")) {
                ctx.redirect("/dash/home?error=Invalid file type. Only images are allowed");
                return;
            }
            if (picture.size() > 20 * 1024 * 1024) {
                ctx.redirect("/dash/home?error=File size exceeds the limit of 20MB");
                return;
            }
            Logger.info("Uploaded file: " + picture.filename());
            Logger.info("Uploaded file type: " + picture.contentType());
            Logger.info("Uploaded file size: " + picture.size());
            Logger.info("Uploaded file extension: " + picture.extension());
            Logger.info("Uploaded file content: " + picture.content());
        }

        try {
            Reward reward = new Reward(name, Float.parseFloat(points), "", RewardCategory.valueOf(category));
            reward.setDescription(description);
            reward.setQuantity(Integer.parseInt(stock));

            if (picture != null && picture.size() > 0) {
                String folderPath = "src/main/resources/public/rewardImages";
                File folder = new File(folderPath);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                String fileName = reward.getId() + "_" + System.currentTimeMillis() + picture.extension();
                File file = new File(folder, fileName);

                try (InputStream inputStream = picture.content()) {
                    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    reward.setImageUrl("/public/rewardImages/" + fileName);
                    Logger.info("File saved: " + reward.getImageUrl());
                } catch (IOException e) {
                    Logger.error("Error while saving uploaded file", e);
                    ctx.redirect("/dash/home?error=Error while saving uploaded file");
                    return;
                }
            }

            RewardContribution rewardContribution = new RewardContribution(reward, DateUtils.now());
            rewardContribution.setContributor(contributor);
            List<FridgeOpenLogEntry> entries = ContributorsManager.getInstance().addContributionToContributor(
                    contributor,
                    rewardContribution);
            if (entries == null) {
                ctx.redirect("/dash/home?error=Somenthing went wrong");
                return;
            }
            DB.create(reward);
            DB.create(rewardContribution);
            DB.update(contributor);

            ctx.redirect("/dash/home");
        } catch (Exception e) {
            Logger.error("Exception ", e);
            ctx.redirect("/dash/home?error=" + e.getMessage());
            return;
        }
    }
}
