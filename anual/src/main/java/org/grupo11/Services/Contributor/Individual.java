package org.grupo11.Services.Contributor;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Enums.DocumentType;
import org.grupo11.Services.Credentials;
import org.grupo11.Services.Contributions.ContributionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;

@Entity
public class Individual extends Contributor {
    private int document;
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;
    private String name;
    private String surname;
    private Long birth;
    @OneToMany(mappedBy = "ownerId")
    private List<Credentials> credentials = new ArrayList<>();

    public Individual() {
        super();
        List<ContributionType> possibleContributions = new ArrayList<ContributionType>();
        possibleContributions.add(ContributionType.MEAL_DONATION);
        possibleContributions.add(ContributionType.MEAL_DISTRIBUTION);
        possibleContributions.add(ContributionType.MONEY_DONATION);
        possibleContributions.add(ContributionType.PERSON_REGISTRATION);
        this.setPossibleContributions(possibleContributions);
    }

    public Individual(String name, String surname, String address, Long birth, int document,
            DocumentType documentType) {
        super(name, address, new ArrayList<>());
        List<ContributionType> possibleContributions = new ArrayList<ContributionType>();
        possibleContributions.add(ContributionType.MEAL_DONATION);
        possibleContributions.add(ContributionType.MEAL_DISTRIBUTION);
        possibleContributions.add(ContributionType.MONEY_DONATION);
        possibleContributions.add(ContributionType.PERSON_REGISTRATION);
        this.name = name;
        this.surname = surname;
        this.birth = birth;
        this.document = document;
        this.documentType = documentType;
    }

    public String getName() {
        return this.name;
    }

    public void getName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long getBirth() {
        return this.birth;
    }

    public void setBirth(Long birth) {
        this.birth = birth;
    }

    public int getDocument() {
        return this.document;
    }

    public void setDocument(int document) {
        this.document = document;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public void addCredentials(Credentials credentials) {
        this.credentials.add(credentials);
    }

    public List<Credentials> getCredentials() {
        return credentials;
    }
}
