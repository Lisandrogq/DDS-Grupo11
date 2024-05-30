package org.grupo11.Services.Fridge;

import java.util.List;
import java.util.stream.Collectors;

public class FridgeMapper {
    private static String baseUrl = "https://665264aa813d78e6d6d56912.mockapi.io/api/v1/fridge-location";

    public static String getSingleFridgeMapLocation(Fridge fridge) {
        return baseUrl + "&waypoints=" + getLonLatSyntax(fridge.getLat(), fridge.getLon());
    }

    private static String getLonLatSyntax(double lat, double lon) {
        return lat + "," + lon;
    }

    public static String getFridgesMapLocations(List<Fridge> fridges) {
        if (fridges == null || fridges.isEmpty()) {
            return baseUrl;
        }

        String waypoints = fridges.stream()
                .map(fridge -> getLonLatSyntax(fridge.getLat(), fridge.getLon()))
                .collect(Collectors.joining("|"));

        return baseUrl + "&waypoints=" + waypoints;
    }
}
