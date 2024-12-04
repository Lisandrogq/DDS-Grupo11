package org.grupo11.Api;

import org.grupo11.Utils.JWTService;

import com.auth0.jwt.interfaces.DecodedJWT;

import io.javalin.http.Context;

public class Middlewares {
    public static DecodedJWT isAuthenticated(Context ctx) {
        String token = ctx.cookie("access-token");
        if (token == null || token.isEmpty()) {
            return null;
        }

        try {
            DecodedJWT decoded = JWTService.validate(token);
            return decoded;
        } catch (Exception e) {
            return null;
        }
    }
}
