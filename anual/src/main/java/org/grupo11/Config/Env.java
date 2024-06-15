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
}