package org.grupo11.DTOS;

import org.grupo11.Enums.DocumentType;

public class IndividualDTO {
    public Long id;
    public String name;
    public String surname;
    public String address;
    public Long birth;
    public int document;
    public DocumentType documentType;
    public double points;
    public int mealDonations;

    public IndividualDTO(Long id, String name, String surname, String address, Long birth, int document,
            DocumentType documentType, double points, int mealDonations) {
        this.id = id;
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
