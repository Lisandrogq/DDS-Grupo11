package org.grupo11.Enums;

import java.util.List;

import org.grupo11.DB;
import org.grupo11.Logger;
import org.grupo11.Services.Credentials;
import org.grupo11.Utils.Crypto;
import org.grupo11.Utils.GithubAPI;
import org.grupo11.Utils.OAuth.GoogleOAuth;
import org.grupo11.Utils.OAuth.OAuthValidateResponse;
import org.hibernate.Session;

// Define your AuthProvider enum
public enum AuthProvider {
    // Enum constants, each implementing the authenticate method
    FridgeBridge {
        @Override
        public Credentials authenticate(String... args) {
            String mail = args[0];
            String pw = args[1];
            try (Session session = DB.getSessionFactory().openSession()) {
                String hashedPassword = Crypto.sha256Hash(pw.getBytes());
                String hql = "SELECT c FROM Credentials c WHERE c.password = :password AND c.mail = :mail";

                org.hibernate.query.Query<Credentials> query = session.createQuery(hql, Credentials.class);
                query.setParameter("mail", mail);
                query.setParameter("password", hashedPassword);
                Credentials credentials = query.getSingleResult();
                return credentials;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public OAuthValidateResponse validateToken(String token) {
            return null;
        }
    },

    Google {
        @Override
        public Credentials authenticate(String... args) {
            String token = args[0];
            OAuthValidateResponse res = GoogleOAuth.validate(token);
            if (res == null) {
                return null;
            }
            try (Session session = DB.getSessionFactory().openSession()) {
                String hql = "SELECT c FROM Credentials c WHERE c.mail = :mail AND c.provider = Google";

                org.hibernate.query.Query<Credentials> query = session.createQuery(hql, Credentials.class);
                query.setParameter("mail", res.getEmail());
                Credentials credentials = query.getSingleResult();
                return credentials;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public OAuthValidateResponse validateToken(String token) {
            return GoogleOAuth.validate(token);
        }
    },

    Github {
        @Override
        public Credentials authenticate(String... args) {
            return null;
        }

        @Override
        public OAuthValidateResponse validateToken(String code) {
            try {
                String token = GithubAPI.getTokenFromCode(code);
                if (token == null)
                    return null;
                List<GithubAPI.GithubEmailResponseItem> emails = GithubAPI.getEmails(token);
                String email = GithubAPI.getPrimaryEmail(emails);
                if (email == null)
                    return null;

                return new OAuthValidateResponse(email, "");
            } catch (Exception e) {
                return null;
            }
        }

    };

    public abstract Credentials authenticate(String... args);

    public abstract OAuthValidateResponse validateToken(String token);

}
