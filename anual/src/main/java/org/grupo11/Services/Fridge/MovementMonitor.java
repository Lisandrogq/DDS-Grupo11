package org.grupo11.Services.Fridge;

public class MovementMonitor {
    private boolean active;

    public MovementMonitor() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
        OnMovementDetected();
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void OnMovementDetected() {
        // here we would add the system alert
        // i think for now, i log is enough
        System.out.println("Unusual movement detected.");
    }
}
