package org.grupo11.Services.Fridge;

import java.util.List;

public class FridgeAllocatorRes {
    public static class Location {
        public float lon;
        public float lat;
    }

    public String id;
    public long createdAt;
    public List<Location> locations;
}
