package org.grupo11.Services.Fridge;

import java.util.ArrayList;
import java.util.List;

public class FridgesManager {
    private List<Fridge> fridges;

    public FridgesManager() {
        this.fridges = new ArrayList<>();
    }

    public void add(Fridge fridge) {
        fridges.add(fridge);
    }

    public void remove(Fridge fridge) {
        fridges.remove(fridge);
    }

    public List<Fridge> getFridges() {
        return this.fridges;
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
}