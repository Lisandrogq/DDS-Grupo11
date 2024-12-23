package org.grupo11.Services.ActivityRegistry;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Fridge.FridgeSolicitude;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Utils.DateUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class ContributorRegistry extends ActivityRegistry {
    @OneToOne
    private Contributor owner;
    @OneToMany(cascade = CascadeType.ALL)
    private List<FridgeSolicitude> permissions;

    public ContributorRegistry() {
    }

    public ContributorRegistry(Contributor owner) {
        this.owner = owner;
        this.permissions = new ArrayList<>();
    }

    public FridgeSolicitude registerPermission(Fridge fridge) {
        FridgeSolicitude solicitude = new FridgeSolicitude(this, DateUtils.getCurrentTimeInMs(), fridge);
        permissions.add(solicitude);
        fridge.addSolicitudes(solicitude);
        return solicitude;
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
