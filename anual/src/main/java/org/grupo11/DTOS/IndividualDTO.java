package org.grupo11.DTOS;

import org.grupo11.Enums.DocumentType;

public class IndividualDTO {
    public String name;
    public String surname;
    public String address;
    public String birth;
    public int document;
    public DocumentType documentType;
    public double points;
    public int mealDonations;

    public IndividualDTO(String name, String surname, String address, String birth, int document,
            DocumentType documentType, double points, int mealDonations) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.birth = birth;
        this.document = document;
        this.documentType = documentType;
        this.points = points;
        this.mealDonations = mealDonations;
    }
}
