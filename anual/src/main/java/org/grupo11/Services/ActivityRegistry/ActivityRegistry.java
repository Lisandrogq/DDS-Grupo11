package org.grupo11.Services.ActivityRegistry;


public abstract class ActivityRegistry {
    private int id;
   // private PersonInNeed owner; //debería ser 'ROL'
   // private List<CardUsage> usages; // capaz se puede abstraer los usos deambas tarjetas, no se si vale la pena

    


    // getters and setters

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

  /*public PersonInNeed getOwner() { //// DEBERÍA SER DE TIPO 'ROL' PERO TODAVIA NO ESTA IMPLEMENTADO
        return this.owner;
    }
    public void setOwner(PersonInNeed owner) {
        this.owner = owner;
    }
    */

}
