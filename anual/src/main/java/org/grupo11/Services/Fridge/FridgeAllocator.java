package org.grupo11.Services.Fridge;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Utils.Fetcher;
import org.grupo11.Utils.JSON;

import com.fasterxml.jackson.core.type.TypeReference;

public class FridgeAllocator {
    private static String baseUrl = "https://665264aa813d78e6d6d56912.mockapi.io/api/v1/fridge-locations";

    public static List<FridgeAllocatorRes.Location> getFridgeGoodLocations(float lon, float lat, int radius) {
        try {
            // !for now we are not passing the query coz the mock api fails
            // String url = baseUrl + "?lon=" + lon + "&lat=" + lat + "&radius=" + radius;
            String url = baseUrl;
            String json = Fetcher
                    .get(url)
                    .body()
                    .string();
            List<FridgeAllocatorRes> res = JSON.parse(json, new TypeReference<List<FridgeAllocatorRes>>() {
            });
            return res.get(0).locations;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
