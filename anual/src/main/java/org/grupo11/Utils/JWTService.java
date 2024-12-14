package org.grupo11.Utils;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Date;
import java.util.Map;

import org.grupo11.Config.Env;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTService {
    public static DecodedJWT validate(String token) throws Exception {
        DecodedJWT decodedJWT;
        try {
            ECPublicKey publicKey = Crypto.loadPublicKey(Env.getJWTPublicKey());
            Algorithm algorithm = Algorithm.ECDSA256(publicKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("fridge-bridge")
                    .build();
            decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (Exception exception) {
            throw new Exception("Invalid jwt");
        }
    }

    public static String generate(Map<String, ?> payload, long expirationTimeInSeconds) throws Exception {
        try {
            ECPrivateKey privateKey = Crypto.loadPrivateKey(Env.getJWTPrivateKey());
            Algorithm algorithm = Algorithm.ECDSA256(privateKey);
            long expirationTimeMillis = System.currentTimeMillis() + expirationTimeInSeconds * 1000;
            Date expirationDate = new Date(expirationTimeMillis);
            String token = JWT.create()
                    .withIssuer("fridge-bridge")
                    .withPayload(payload)
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
            return token;
        } catch (Exception exception) {
            throw new Exception("Could not generate jwt");
        }
    }
}
