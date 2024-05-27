package org.grupo11.Services.Fridge;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FridgeManager {
    List<Fridge> fridges = new ArrayList<>();
    String baseMapUrl = "https://www.google.com/maps/dir/?api=1";

    public String getFridgesMap() {
        if (fridges == null || fridges.isEmpty()) {
            return baseMapUrl;
        }

        String waypoints = fridges.stream()
                .map(fridge -> fridge.getLat() + "," + fridge.getLon())
                .collect(Collectors.joining("|"));

        return baseMapUrl + "&waypoints=" + waypoints;
    }

}
