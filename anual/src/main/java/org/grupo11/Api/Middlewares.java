package org.grupo11.Api;

import org.grupo11.Logger;
import org.grupo11.Enums.UserTypes;
import org.grupo11.Services.Credentials;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Technician.Technician;
import org.grupo11.Utils.JWTService;

import com.auth0.jwt.interfaces.DecodedJWT;

import io.javalin.http.Context;

public class Middlewares {
    public static Contributor contributorIsAuthenticated(Context ctx) {
        String token = ctx.cookie("access-token");
        if (token == null || token.isEmpty()) {
            return null;
        }

        try {
            DecodedJWT decoded = JWTService.validate(token);
            Contributor contributor = HttpUtils.getContributorFromAccessToken(decoded);
            return contributor;
        } catch (Exception e) {
            return null;
        }
    }

    public static Technician technicianIsAuthenticated(Context ctx) {
        String token = ctx.cookie("access-token");
        if (token == null || token.isEmpty()) {
            return null;
        }

        try {
            DecodedJWT decoded = JWTService.validate(token);
            Technician technician = HttpUtils.getTechnicianFromAccessToken(decoded);
            return technician;
        } catch (Exception e) {
            Logger.error("An error during jwt decode.", e);
            return null;
        }
    }

    public static boolean authenticatedAsAdmin(Context ctx) {
        String token = ctx.cookie("access-token");
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            DecodedJWT decoded = JWTService.validate(token);
            Credentials credentials = HttpUtils.getCredentialsFromAccessToken(decoded);
            return credentials.getUserType() == UserTypes.Admin;
        } catch (Exception e) {
            Logger.error("An error during jwt decode.", e);
            return false;
        }
    }

    public static Credentials authenticated(Context ctx) {
        String token = ctx.cookie("access-token");
        if (token == null || token.isEmpty()) {
            return null;
        }

        try {
            DecodedJWT decoded = JWTService.validate(token);
            Credentials credentials = HttpUtils.getCredentialsFromAccessToken(decoded);
            return credentials;
        } catch (Exception e) {
            Logger.error("An error during jwt decode.", e);
            return null;
        }
    }

    public static DecodedJWT authenticatedFromHeader(Context ctx) {
        String authorization = ctx.header("Authorization");
        if (authorization == null || authorization.isEmpty()) {
            return null;
        }
        String[] split = authorization.split(" ");
        String type = split[0];
        if (type.compareTo("Bearer") != 0) {
            return null;
        }

        String token = split[1];
        try {
            DecodedJWT decoded = JWTService.validate(token);
            return decoded;
        } catch (Exception e) {
            Logger.error("An error during jwt decode.", e);
            return null;
        }
    }
}
