package org.grupo11.Api;

import java.util.List;
import java.util.Map;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Enums.UserTypes;
import org.grupo11.Services.Credentials;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Technician.Technician;
import org.hibernate.Hibernate;
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

    public static Credentials getCredentialsFromAccessToken(DecodedJWT token) {
        String payload = new String(java.util.Base64.getDecoder().decode(token.getPayload()));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> payloadMap;
        try {
            payloadMap = objectMapper.readValue(payload, Map.class);
        } catch (Exception e) {
            return null;
        }
        try {
            Credentials credentials = new Credentials();
            String mail = payloadMap.get("mail").toString();
            Long ownerId = Long.valueOf(payloadMap.get("owner_id").toString());
            UserTypes type = Enum.valueOf(UserTypes.class, payloadMap.get("type").toString());
            if (type == null) {
                return null;
            }

            credentials.setMail(mail);
            credentials.setOwnerId(ownerId);
            credentials.setUserType(type);

            return credentials;
        } catch (Exception e) {
            return null;
        }

    }

    public static Contributor getContributorFromAccessToken(DecodedJWT token) {
        Credentials credentials = getCredentialsFromAccessToken(token);
        if (credentials == null) {
            return null;
        }

        try (Session session = DB.getSessionFactory().openSession()) {
            String entity = credentials.getUserType() == UserTypes.Individual ? "Individual" : "LegalEntity";

            String hql = "SELECT entity " +
                    "FROM " + entity + " entity " +
                    "JOIN Contributor contr ON entity.id = contr.id " +
                    "WHERE entity.id = :owner_id";
            org.hibernate.query.Query<Contributor> query = session.createQuery(hql, Contributor.class);
            query.setParameter("owner_id", credentials.getOwnerId());
            List<Contributor> contributors = query.getResultList();
            if (contributors.isEmpty()) {
                return null;
            } else if (contributors.size() > 1) {
                Logger.error("More than one contributor with the same id.");
                return null;
            } else {
                Contributor contributor = contributors.get(0);
                Hibernate.initialize(contributor.getContributions());
                return contributor;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static Technician getTechnicianFromAccessToken(DecodedJWT token) {
        Credentials credentials = getCredentialsFromAccessToken(token);
        if (credentials == null) {
            return null;
        }

        try (Session session = DB.getSessionFactory().openSession()) {

            String hql = "SELECT t " +
                    "FROM Technician t " +
                    "WHERE t.id = :owner_id";
            org.hibernate.query.Query<Technician> query = session.createQuery(hql, Technician.class);
            query.setParameter("owner_id", credentials.getOwnerId());
            List<Technician> technicians = query.getResultList();
            if (technicians.isEmpty()) {
                return null;
            } else if (technicians.size() > 1) {
                Logger.error("More than one technician with the same id.");
                return null;
            } else {
                return technicians.get(0);
            }
        } catch (Exception e) {
            Logger.error("An error during jwt decode.", e);
            return null;
        }
    }
}
