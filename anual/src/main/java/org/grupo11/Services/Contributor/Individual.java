package org.grupo11.Services.Contributor;

import java.util.List;

import org.grupo11.Services.Contributions.ContributionType;

public class Individual extends Contributor {
    private String name;
    private String surname;
    private String birth;

    public Individual(List<ContributionType> possibleContributions) {
        super(possibleContributions);

    }

    public Individual(String address, List<ContributionType> possibleContributions) {
        super(address, possibleContributions);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirth() {
        return this.birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

}
