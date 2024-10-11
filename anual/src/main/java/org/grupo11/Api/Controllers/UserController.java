package org.grupo11.Api.Controllers;

import java.util.Map;

import io.javalin.http.Context;

public class UserController {
    public static void handleUserLogin(Context ctx) {
        try {
            String mail = ctx.formParam("mail");
            String contrasena = ctx.formParam("contrasena");
            // TODO(marcos): validate user input
            if (mail.equals("admin@admin.com") && contrasena.equals("admin")) {
                ctx.redirect("/dash/home");
            } else {
                ctx.render("/templates/register/login.html", Map.of("error", "invalid credentials"));
            }
        } catch (Exception e) {
            ctx.status(500);
        }
    }

    public static void handleUserSignup(Context ctx) {
        ctx.redirect("/dash/home");
    }
}
