package org.grupo11.Services;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeMapper;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class FridgeMapperTest {

    private List<Fridge> fridges;

    @Before
    public void setUp() {
        // Initialize some fridges for testing
        fridges = new ArrayList<>();
        fridges.add(new Fridge(-74.006, 40.7128, "Caballito", "Fridge A", 100, 2020, null, null, null));
        fridges.add(new Fridge(-118.2437, 34.0522, "Almagro", "Fridge B", 150, 2019, null, null, null));
        fridges.add(new Fridge(-0.1278, 51.5074, "Lugano", "Fridge C", 120, 2021, null, null, null));
    }

    @Test
    public void testGetSingleFridgeMapLocation() {
        String expectedUrl = "https://www.google.com/maps/dir/?api=1&waypoints=40.7128,-74.006";

        String resultUrl = FridgeMapper.getSingleFridgeMapLocation(fridges.get(0));

       

        assertEquals("Single fridge map location URL does not match", expectedUrl, resultUrl);
    }

    @Test
    public void testGetFridgesMapLocations() {
        String expectedUrl = "https://www.google.com/maps/dir/?api=1&waypoints=40.7128,-74.006|34.0522,-118.2437|51.5074,-0.1278";

        String resultUrl = FridgeMapper.getFridgesMapLocations(fridges);

        assertEquals("Multiple fridges map locations URL does not match", expectedUrl, resultUrl);
    }

    @Test
    public void testGetFridgesMapLocationsEmptyList() {
        List<Fridge> emptyFridges = new ArrayList<>();

        String expectedUrl = "https://www.google.com/maps/dir/?api=1";

        String resultUrl = FridgeMapper.getFridgesMapLocations(emptyFridges);

        assertEquals("Empty fridges list should return base URL", expectedUrl, resultUrl);
    }
}
