package org.grupo11.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationHandler {

    private static final String NOMINATIM_API_URL = "https://nominatim.openstreetmap.org/search";

    public static double[] getCoordinates(String address) throws Exception {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }

        String query = String.format("%s?q=%s&format=json", NOMINATIM_API_URL,
                java.net.URLEncoder.encode(address, "UTF-8"));
        URI uri = new URI(query);
        URL url = uri.toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; Grupo11/1.0)");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("Failed to get coordinates. HTTP error code: " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }
        reader.close();

        String response = responseBuilder.toString();
        Pattern latPattern = Pattern.compile("\"lat\":\"(.*?)\"");
        Pattern lonPattern = Pattern.compile("\"lon\":\"(.*?)\"");

        Matcher latMatcher = latPattern.matcher(response);
        Matcher lonMatcher = lonPattern.matcher(response);

        if (latMatcher.find() && lonMatcher.find()) {
            double lat = Double.parseDouble(latMatcher.group(1));
            double lon = Double.parseDouble(lonMatcher.group(1));
            return new double[] { lat, lon };
        } else {
            throw new Exception("Failed to get coordinates. No matches found.");
        }
    }
}
