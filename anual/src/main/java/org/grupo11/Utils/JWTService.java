package org.grupo11.Utils;

import java.util.Date;
import java.util.Map;

import org.grupo11.Logger;
import org.grupo11.Config.Env;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTService {
    public static DecodedJWT validate(String token) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC256(Env.getJWTSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("fridge-bridge")
                    .build();

            decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            throw exception;
        }
    }

    public static String generate(Map<String, ?> payload, long expirationTimeInSeconds) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(Env.getJWTSecret());
            long expirationTimeMillis = System.currentTimeMillis() + expirationTimeInSeconds * 1000;
            Date expirationDate = new Date(expirationTimeMillis);
            String token = JWT.create()
                    .withIssuer("fridge-bridge")
                    .withPayload(payload)
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw exception;
        }
    }
}
