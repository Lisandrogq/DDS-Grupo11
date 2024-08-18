package org.grupo11.Controller.DTOS;

import java.util.List;

public class FridgeTemp {
    public static class Location {
        public float lon;
        public float lat;
    }

    public String id;
    public long createdAt;
    public List<Location> locations;
}
