package org.grupo11.Services.PersonInNeed;

import java.util.ArrayList;
import java.util.List;

public class PersonInNeedManager {
    private List<PersonInNeed> personsInNeed;
    private static PersonInNeedManager instance = null;

    private PersonInNeedManager() {
        this.personsInNeed = new ArrayList<>();
    }

    public static synchronized PersonInNeedManager getInstance() {
        if (instance == null)
            instance = new PersonInNeedManager();

        return instance;
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
