package org.grupo11.Services.PersonInNeed;

import java.util.ArrayList;
import java.util.List;

public class PersonInNeedManager {
    private List<PersonInNeed> personsInNeed;

    public PersonInNeedManager() {
        this.personsInNeed = new ArrayList<>();
    }

    public List<PersonInNeed> getPersonsInNeed() {
        return this.personsInNeed;
    }

    public void setPersonsInNeed(List<PersonInNeed> personsInNeed) {
        this.personsInNeed = personsInNeed;
    }

    public void add(PersonInNeed person) {
        personsInNeed.add(person);
    }

    public void remove(PersonInNeed person) {
        personsInNeed.remove(person);
    }

    public PersonInNeed getByDNI(int id) {
        for (PersonInNeed person : personsInNeed) {
            if (person.getDNI() == id) {
                return person;
            }
        }
        return null;
    }
}
