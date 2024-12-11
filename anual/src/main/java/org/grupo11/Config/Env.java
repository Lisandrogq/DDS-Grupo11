package org.grupo11.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Env {
    private static final String ENV_FILE = ".env";
    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream inputStream = Env.class.getClassLoader().getResourceAsStream(ENV_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new IOException(".env file not found in resources.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSendGridApiKey() {
        return properties.getProperty("SENDGRID_API_KEY");
    }

    public static String getCompanyMail() {
        return properties.getProperty("COMPANY_MAIL");
    }

    public static String getDBUrl() {
        return properties.getProperty("POSTGRES_URL");
    }

    public static String getDBUser() {
        return properties.getProperty("POSTGRES_USER");
    }

    public static String getDBPassword() {
        return properties.getProperty("POSTGRES_PASSWORD");
    }

    public static String getRabbitHost() {
        return properties.getProperty("RABBIT_HOST");
    }

    public static String getRabbitPort() {
        return properties.getProperty("RABBIT_PORT");
    }

    public static String getRabbitUser() {
        return properties.getProperty("RABBIT_USER");
    }

    public static String getRabbitPassword() {
        return properties.getProperty("RABBIT_PASSWORD");
    }

    public static String getJWTPublicKey() {
        return properties.getProperty("JWT_PUB_KEY");
    }

    public static String getJWTPrivateKey() {
        return properties.getProperty("JWT_PRIV_KEY");
    }

    public static String getGoogleOAuthClientId() {
        return properties.getProperty("GOOGLE_OAUTH_CLIENT_ID");
    }
}
