package org.grupo11.Services.Fridge;

public class MovementMonitor {
    private boolean active;

    public MovementMonitor() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
        onMovementDetected();
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void onMovementDetected() {
        // here we would add the system alert
        // i think for now, a log is enough
        System.out.println("Unusual movement detected.");
    }
}
