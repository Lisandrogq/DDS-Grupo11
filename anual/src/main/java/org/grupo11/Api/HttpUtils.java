package org.grupo11.Api;

import java.util.Map;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Services.Contributor.Contributor;
import org.hibernate.Session;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;

public class HttpUtils {
    public static Cookie createHttpOnlyCookie(String name, String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setSecure(true);
        return cookie;
    }

}
