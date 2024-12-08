package org.grupo11.Api.Controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.List;

import org.grupo11.DB;
import org.grupo11.Logger;
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
import org.grupo11.Services.Fridge.FridgeOpenLogEntry;
import org.grupo11.Services.Fridge.Sensor.MovementSensorManager;
import org.grupo11.Services.Fridge.Sensor.TemperatureSensorManager;
import org.grupo11.Services.PersonInNeed.PersonInNeed;
import org.grupo11.Services.Rewards.Reward;
import org.grupo11.Services.Rewards.RewardCategory;
import org.grupo11.Utils.DateUtils;
import org.grupo11.Utils.FieldValidator;
import org.hibernate.Session;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import jakarta.persistence.criteria.CriteriaBuilder.In;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jetty.util.log.Log;


public class ContributionsController {

    public static void handleMealContribution(Context ctx) {
        System.out.println(ctx.body());
        Contributor contributor = Middlewares.contributorIsAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }
        if (contributor instanceof LegalEntity) {
            ctx.redirect("/dash/home?error=No puede contribuir de esta forma");
            return;
        }

        String type = ctx.formParam("type");
        String expirationDate = ctx.formParam("expirationDate");
        Logger.info("expirationDate: " + expirationDate);
        String fridge_address = ctx.formParam("fridge_address");
        String calories = ctx.formParam("calories");
        String weight = ctx.formParam("weight");

        if (!FieldValidator.isString(type)) {
            throw new IllegalArgumentException("invalid type");
        }
        if (!FieldValidator.isString(fridge_address)) {
            throw new IllegalArgumentException("invalid fridge_address");
        }
        if (!FieldValidator.isDate(expirationDate)) {
            throw new IllegalArgumentException("invalid expirationDate");
        }
        if (!FieldValidator.isInt(calories)) {
            throw new IllegalArgumentException("invalid calories");
        }
        if (!FieldValidator.isInt(weight)) {
            throw new IllegalArgumentException("invalid weight");
        }
        if (Integer.parseInt(weight) <= 0) {
            ctx.redirect("/dash/home?error=Ingresar un peso válido");
            return;
        }
        if (Integer.parseInt(calories) <= 0) {
            ctx.redirect("/dash/home?error=Ingresar calorias válidas");
            return;
        }

        System.err.println(ctx.body());

        try (Session session = DB.getSessionFactory().openSession()) {
            String hql = "SELECT f " +
                    "FROM Fridge f " +
                    "WHERE f.address = :fridge_address";
            org.hibernate.query.Query<Fridge> query = session.createQuery(hql, Fridge.class);
            query.setParameter("fridge_address", fridge_address);
            Fridge fridge = query.uniqueResult();

            if (fridge == null) {
                ctx.redirect("/dash/home?error=La heladera no existe");
                return;
            }
            if (fridge.getIsActive() == false) {
                ctx.redirect("/dash/home?error=La heladera esta desactivada");
                return;
            }
            if(fridge.getCapacity() >= fridge.getMeals().size()){
                ctx.redirect("/dash/home?error=La heladera esta llena");
                return;
            }
            Meal meal = new Meal(type, DateUtils.parseDateString(expirationDate), DateUtils.now(), fridge, "",
                    Integer.parseInt(calories), Integer.parseInt(weight));
            MealDonation mealDonation = new MealDonation(meal, DateUtils.now());
            mealDonation.setContributor(contributor);
            List<FridgeOpenLogEntry> entries = ContributorsManager.getInstance().addContributionToContributor(
                    contributor,
                    mealDonation);
            if (entries == null) {
                ctx.redirect("/dash/home?error=Algo salio mal");
                return;
            }
            DB.update(contributor);
            DB.create(entries.get(0));
            fridge.addMeal(meal);
            DB.create(meal);
            DB.create(mealDonation);
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
        String origin_address = ctx.formParam("origin_address");
        String destiny_address = ctx.formParam("destiny_address");

        try (Session session = DB.getSessionFactory().openSession()) {

            if (!FieldValidator.isString(reason)) {
                throw new IllegalArgumentException("invalid reason");
            }
            if (!FieldValidator.isString(origin_address)) {
                throw new IllegalArgumentException("invalid origin_address");
            }
            if (!FieldValidator.isString(destiny_address)) {
                throw new IllegalArgumentException("invalid destiny_address");
            }

            String origin_hql = "SELECT f " +
                    "FROM Fridge f " +
                    "WHERE f.address = :origin_address";
            org.hibernate.query.Query<Fridge> origin_query = session.createQuery(origin_hql, Fridge.class);
            origin_query.setParameter("origin_address", origin_address);
            String destiny_hql = "SELECT f " +
                    "FROM Fridge f " +
                    "WHERE f.address = :destiny_address";
            org.hibernate.query.Query<Fridge> destiny_query = session.createQuery(destiny_hql, Fridge.class);
            destiny_query.setParameter("destiny_address", destiny_address);

            Fridge origin_fridge = origin_query.getSingleResult();
            Fridge destiny_fridge = destiny_query.getSingleResult();
            if (origin_fridge == null) {
                ctx.redirect("/dash/home?error=No existe la heladera de origen");
                return;
            }
            if (destiny_fridge == null) {
                ctx.redirect("/dash/home?error=No existe la heladera de destino");
                return;
            }
            if (origin_fridge.getIsActive() == false) {
                ctx.redirect("/dash/home?error=La heladera de origen esta desactivada");
                return;
            }
            if (destiny_fridge.getIsActive() == false) {
                ctx.redirect("/dash/home?error=La heladera de destino esta desactivada");
                return;
            }

            int i = 0;
            int max = 0;
            for (i = 0; i < 10; i++) { // primero se valida que todas las comidas estén . se podría hacer con
                                       // transacciones pero notiempo
                String meal_type = ctx.formParam("meal_" + i);
                if (meal_type != null) {
                    Meal meal = origin_fridge.getMealByType(meal_type);
                    max++;
                    if (meal == null) {
                        ctx.redirect("/dash/home?error=No existe la comida " + meal_type + " en la heladera de origen");
                        return;
                    }
                }
            }
            if (max == 0) {
                ctx.redirect("/dash/home?error=No se seleccionaron comidas");
                return;
            }
            if (max > destiny_fridge.getCapacity() - destiny_fridge.getMeals().size()) {
                ctx.redirect("/dash/home?error=La heladera de destino no tiene capacidad suficiente");
                return;
            }
            MealDistribution mealDistribution = new MealDistribution(origin_fridge, destiny_fridge, max, reason,
                    DateUtils.now());
            List<FridgeOpenLogEntry> entries = ContributorsManager.getInstance().addContributionToContributor(
                    contributor,
                    mealDistribution);
            if (entries == null) {
                ctx.redirect("/dash/home?error=Algo salio mal");
                return;
            }
            DB.update(contributor); // haya openSolicitude
            DB.create(entries.get(0));// origin entry
            DB.create(entries.get(1));// destiny entry
            for (i = 0; i < max; i++) { // luego se realiza el movimiento.
                String meal_type = ctx.formParam("meal_" + i);
                if (meal_type != null) {
                    Meal meal = origin_fridge.getMealByType(meal_type);
                    System.out.println("adad: " + meal_type + " - " + meal.getType());

                    origin_fridge.removeMeal(meal);
                    destiny_fridge.addMeal(meal);
                    meal.setFridge(destiny_fridge);
                    DB.update(meal);

                }
            }
            DB.update(origin_fridge);
            DB.update(destiny_fridge);

            mealDistribution.setContributor(contributor);
            DB.create(mealDistribution);

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

        String name = ctx.formParam("name");
        String address = ctx.formParam("address");
        String capacity = ctx.formParam("capacity");
        String isactive = ctx.formParam("isActive");

        if (!FieldValidator.isString(name)) {
            throw new IllegalArgumentException("invalid name");
        }
        if (!FieldValidator.isString(address)) {
            throw new IllegalArgumentException("invalid address");
        }
        if (!FieldValidator.isInt(capacity)) {
            throw new IllegalArgumentException("invalid capacity");
        }
        if (!FieldValidator.isBool(isactive)) {
            throw new IllegalArgumentException("invalid isactive");
        }
        if (Integer.parseInt(capacity) <= 0) {
            ctx.redirect("/dash/home?error=Ingresar una capacidad valida");
            return;
        }

        try (Session session = DB.getSessionFactory().openSession()) {
            String leHQL = "SELECT le " +
                    "FROM LegalEntity le " +
                    "WHERE le.id = :contributor_id";
            org.hibernate.query.Query<LegalEntity> leQuery = session.createQuery(leHQL, LegalEntity.class);
            leQuery.setParameter("contributor_id", contributor.getId());
            LegalEntity le = leQuery.uniqueResult();
            if (le == null) {
                throw new IllegalArgumentException("You are not a legal entity");
            }

            Fridge fridge = new Fridge(address, name, Integer.parseInt(capacity), 0, new ArrayList<>(), null, null);
            TemperatureSensorManager tManager = new TemperatureSensorManager(fridge, -1, 60);
            MovementSensorManager mManager = new MovementSensorManager(fridge);
            fridge.setIsActive(Boolean.parseBoolean(isactive));
            fridge.setTempManager(tManager);
            fridge.setMovManager(mManager);
            tManager.setFridge(fridge);
            mManager.setFridge(fridge);

            FridgeAdmin fridgeAdmin = new FridgeAdmin(le, fridge, DateUtils.now());
            fridgeAdmin.setContributor(contributor);

            List<FridgeOpenLogEntry> entries = ContributorsManager.getInstance().addContributionToContributor(
                    contributor,
                    fridgeAdmin);
            if (entries == null)
                throw new IllegalArgumentException("no puede contribuir de esta forma");// TODO: validar antes de esto q haya openSolicitude
            DB.update(contributor);
            DB.create(fridge);
            DB.create(fridgeAdmin);

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
            throw new IllegalArgumentException("invalid amount");

        }
        if (!FieldValidator.isString(message)) {
            throw new IllegalArgumentException("invalid message");
        }
        if (Integer.parseInt(amount) <= 0) {
            ctx.redirect("/dash/home?error=Ingresar un monto valido");
            return;
        }

        try {
            MoneyDonation moneyDonation = new MoneyDonation(Integer.parseInt(amount), DateUtils.now(), message);
            moneyDonation.setContributor(contributor);
            List<FridgeOpenLogEntry> entries = ContributorsManager.getInstance().addContributionToContributor(
                    contributor,
                    moneyDonation);
            if (entries == null)
                throw new IllegalArgumentException("no puede contribuir de esta forma");// TODO: validar antes de esto q
                                                                                        // haya openSolicitude
            DB.update(contributor);
            DB.create(moneyDonation);
            ctx.redirect("/dash/home");
        } catch (Exception e) {
            Logger.error("Exception ", e);
            ctx.redirect("/dash/home?error=" + e.getMessage());
            return;
        }
    }

    public static void handlePersonRegistrationContribution(Context ctx) {
        System.out.println(ctx.body());
        Contributor contributor = Middlewares.contributorIsAuthenticated(ctx);
        if (contributor == null) {
            ctx.redirect("/register/login");
            return;
        }

        String name = ctx.formParam("name");
        String dni = ctx.formParam("dni");
        String birth = ctx.formParam("birth");
        String children_count = ctx.formParam("children_count");

        if (!FieldValidator.isString(name)) {
            throw new IllegalArgumentException("invalid name");
        }
        if (!FieldValidator.isDate(birth)) {
            throw new IllegalArgumentException("invalid birth");
        } if (!FieldValidator.isInt(dni)) {
            throw new IllegalArgumentException("invalid dni");
        }
        if (!FieldValidator.isInt(children_count)) {
            throw new IllegalArgumentException("invalid children_count");
        }
        if (Integer.parseInt(children_count) < 0) {
            ctx.redirect("/dash/home?error=Ingresar una cantidad de hijos valida");
            return;
        }

        try {

            PersonInNeed PIN = new PersonInNeed(name,DateUtils.parseDateString(birth),DateUtils.now(),"",Integer.parseInt(dni),Integer.parseInt(children_count),null); 
            PersonRegistration personRegistration = new PersonRegistration(PIN,DateUtils.now(),contributor);
            personRegistration.setContributor(contributor);
            List<FridgeOpenLogEntry> entries = ContributorsManager.getInstance().addContributionToContributor(
                    contributor,
                    personRegistration);
            if (entries == null)
                throw new IllegalArgumentException("no puede contribuir de esta forma");// TODO: validar antes de esto q
                                                                                        // haya openSolicitude
            DB.update(contributor);
            DB.create(PIN);
            DB.create(personRegistration);
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

        String name = ctx.formParam("name");
        String stock = ctx.formParam("stock");
        String description = ctx.formParam("description");
        String points = ctx.formParam("points");
        String category = ctx.formParam("category");
        UploadedFile picture = ctx.uploadedFile("picture");

        if (!FieldValidator.isString(name)) {
            throw new IllegalArgumentException("invalid name");
        }
        if (!FieldValidator.isString(description)) {
            throw new IllegalArgumentException("invalid description");
        }
        if (!FieldValidator.isInt(stock)) {
            throw new IllegalArgumentException("invalid stock");
        }
        if (Integer.parseInt(stock) <= 0) {
            ctx.redirect("/dash/home?error=Ingresar un stock valido");
        }
        if (!FieldValidator.isInt(points)) {
            throw new IllegalArgumentException("invalid points");
        }
        if (Integer.parseInt(points) <= 0) {
            ctx.redirect("/dash/home?error=Ingresar una cantidad de puntos valida");
        }
        if (!FieldValidator.isValidEnumValue(RewardCategory.class, category)) {
            throw new IllegalArgumentException("invalid category");
        }
        if (picture != null && picture.size() > 0) {
            Logger.info("No es NULL por alguna razon");
            if (!picture.contentType().contains("image")) {
                throw new IllegalArgumentException("invalid picture");
            }
            if (picture.size() > 20 * 1024 * 1024) {
                throw new IllegalArgumentException("Picture size must be less than 20MB");
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

                String fileName = reward.getName() + "_" + System.currentTimeMillis() + picture.extension();
                File file = new File(folder, fileName);
                
                try (InputStream inputStream = picture.content()) {
                    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    reward.setImageUrl("/public/rewardImages/" + fileName);
                    Logger.info("File saved: " + reward.getImageUrl());
                } catch (IOException e) {
                    Logger.error("Error while saving uploaded file", e);
                    throw new RuntimeException("Error while saving uploaded file", e);
                }
            }
            
            RewardContribution rewardContribution = new RewardContribution(reward, DateUtils.now());
            rewardContribution.setContributor(contributor);
            List<FridgeOpenLogEntry> entries = ContributorsManager.getInstance().addContributionToContributor(
                    contributor,
                    rewardContribution);
            if (entries == null)
                throw new IllegalArgumentException("no puede contribuir de esta forma");// TODO: validar antes de esto q
                                                                                        // haya openSolicitude
            DB.update(contributor);
            DB.create(reward);
            DB.create(rewardContribution);
            // Pausar para dar tiempo a que se actualice la base de datos
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.redirect("/dash/home");
        } catch (Exception e) {
            Logger.error("Exception ", e);
            ctx.redirect("/dash/home?error=" + e.getMessage());
            return;
        }
    }
}
