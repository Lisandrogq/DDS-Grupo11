package org.grupo11.Api.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.DTOS.User;

import io.javalin.http.Context;

public class UserController {
    public static void handleUserLogin(Context ctx) {
        try {
            String mail = ctx.formParam("mail");
            String password = ctx.formParam("contrasena");
            try {
                User user = new User(mail, password);
                DB.create(user);
            } catch (Exception e) {
                Logger.error("Could not create user");
            }
            // TODO(marcos): validate user input
            if (mail.equals("admin@admin.com") && password.equals("admin")) {
                ctx.redirect("/dash/home");
            } else {
                Map<String, String> error_map = new HashMap<>();
                error_map.put("error", "invalid credentials");
                ctx.render("/templates/register/login.html", error_map); // Map.of("error", "invalid credentials")--CAPAZ POR
                                                                   // LA VERSION DE JAVA ESTO NO ANDA
            }
        } catch (Exception e) {
            ctx.status(500);
        }
    }

    public static void handleUserSignup(Context ctx) {
        ctx.redirect("/dash/home");
    }
}
