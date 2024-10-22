package org.grupo11.Services.Contributor;

import java.util.ArrayList;

import org.grupo11.Enums.DocumentType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Individual extends Contributor {
    @Id
    private int document;
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;
    private String name;
    private String surname;
    private String birth;

    public Individual() {
        super();
    }

    public Individual(String name, String surname, String address, String birth, int document,
            DocumentType documentType) {
        super(name, address, new ArrayList<>());
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

    public String getBirth() {
        return this.birth;
    }

    public void setBirth(String birth) {
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
}
