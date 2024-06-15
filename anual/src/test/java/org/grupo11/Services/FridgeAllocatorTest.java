package org.grupo11.Services;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import org.grupo11.Services.Fridge.FridgeAllocator;
import org.grupo11.Services.Fridge.FridgeAllocatorRes;

public class FridgeAllocatorTest {
    @Test
    public void shouldReturnGoodLocations() {
       List<FridgeAllocatorRes.Location> locations = FridgeAllocator.getFridgeGoodLocations(10, 10, 10);

        assertNotNull("The locations list should not be null", locations);

        // Assert that the list is not empty
        assertFalse("The locations list should not be empty", locations.isEmpty()) ;

        // log the actual locations
        for (FridgeAllocatorRes.Location location : locations) {
            System.out.println("Location: \n\tlon: " + location.lon + "\n\tlat: " + location.lat);
        }
    }
}
