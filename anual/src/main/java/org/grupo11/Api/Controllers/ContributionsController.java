package org.grupo11.Api.Controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import org.grupo11.DB;
import org.grupo11.Services.Meal;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributions.MoneyDonation;
import org.grupo11.Services.Contributor.Contributor;
import org.hibernate.Session;

import io.javalin.http.Context;

public class ContributionsController {
    public static void handleMealContribution(Context ctx) {
    }

    public static void handleMealDistributionContribution(Context ctx) {
    }

    public static void handleMoneyContribution(Context ctx) {
        int ammount = Integer.parseInt(ctx.formParam("ammount"));
        String message = ctx.formParam("message");///las validaciones de los tipos las hace DIOS
        Contributor testContributor = new Contributor("name","addres",new ArrayList<>());
        MoneyDonation moneyDonation = new MoneyDonation(ammount,System.currentTimeMillis(),message);

        DB.create(testContributor);
        moneyDonation.setContributor(testContributor);
        DB.create(moneyDonation);//con este create crashea REVISAR
    }

    public static void handlePersonRegistrationContribution(Context ctx) {
    }

    public static void handleRewardContribution(Context ctx) {
    }
}
