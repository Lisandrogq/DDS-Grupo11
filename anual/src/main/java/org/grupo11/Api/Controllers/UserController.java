package org.grupo11.Api.Controllers;

import io.javalin.http.Context;

public class UserController {
    public static void handleUserLogin(Context ctx) {
        ctx.redirect("/dash/home");
    }

    public static void handleUserSignup(Context ctx) {
        ctx.redirect("/dash/home");
    }
}
