package org.grupo11.Services.Fridge;

import java.util.ArrayList;
import java.util.List;

public class FridgesManager {
    private List<Fridge> fridges;
    private FridgeAllocator fridgeAllocator;
    private static FridgesManager instance = null;

    private FridgesManager() {
        this.fridges = new ArrayList<>();
        this.fridgeAllocator = new FridgeAllocator();
    }

    public static synchronized FridgesManager getInstance() {
        if (instance == null)
            instance = new FridgesManager();

        return instance;
    }

    public void add(Fridge fridge) {
        fridges.add(fridge);
    }

    public void remove(Fridge fridge) {
        fridges.remove(fridge);
    }

    public void setFridges(List<Fridge> fridges) {
        this.fridges = fridges;
    }

    public Fridge getById(int id) {
        for (Fridge fridge : fridges) {
            if (fridge.getId() == id) {
                return fridge;
            }
        }
        return null;
    }

    public String getFridgesMap() {
        return FridgeMapper.getFridgesMapLocations(fridges);
    }

    public FridgeAllocator getFridgeAllocator() {
        return this.fridgeAllocator;
    }
}
