package org.grupo11.Services.ActivityRegistry;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class ActivityRegistry {
    private int id;

    // getters and setters
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*
     * public PersonInNeed getOwner() { //// DEBERÍA SER DE TIPO 'ROL' PERO TODAVIA
     * NO ESTA IMPLEMENTADO
     * return this.owner;
     * }
     * public void setOwner(PersonInNeed owner) {
     * this.owner = owner;
     * }
     */

}
