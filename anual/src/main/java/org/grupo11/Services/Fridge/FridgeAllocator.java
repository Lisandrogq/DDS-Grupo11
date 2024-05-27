package org.grupo11.Services.Fridge;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Utils.Fetcher;
import org.grupo11.Utils.JSON;

public class FridgeAllocator {
    private static String baseUrl = "https://665264aa813d78e6d6d56912.mockapi.io/api/v1/fridge-location";

    public static List<FridgeAlLocatorRes.Location> getFridgeGoodLocations(float lon, float lat, int radius) {
        try {
            String url = baseUrl + "?lon=" + lon + "&lat=" + lat + "&radius=" + radius;
            String json = Fetcher
                    .get(url)
                    .body()
                    .string();
            FridgeAlLocatorRes res = JSON.parse(json, FridgeAlLocatorRes.class);
            return res.locations;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
