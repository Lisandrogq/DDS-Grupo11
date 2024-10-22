package org.grupo11.Services.ActivityRegistry;

import java.util.List;

import org.grupo11.Services.Fridge.FridgeSolicitude;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Utils.DateUtils;

import jakarta.persistence.OneToOne;

public class ContributorRegistry extends ActivityRegistry {
    private int id;
    @OneToOne
    private Contributor owner;
    private List<FridgeSolicitude> permissions;

    public ContributorRegistry(int id, Contributor owner, List<FridgeSolicitude> permissions) {
        this.id = id;
        this.owner = owner;
        this.permissions = permissions;
    }

    public FridgeSolicitude registerPermission(Fridge fridge) {
        FridgeSolicitude solicitude = new FridgeSolicitude(this, DateUtils.getCurrentTimeInMs(), fridge);
        permissions.add(solicitude);
        fridge.addSolicitudes(solicitude);
        return solicitude;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Contributor getOwner() {
        return this.owner;
    }

    public void setOwner(Contributor owner) {
        this.owner = owner;
    }

    public List<FridgeSolicitude> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(List<FridgeSolicitude> permissions) {
        this.permissions = permissions;
    }

}
