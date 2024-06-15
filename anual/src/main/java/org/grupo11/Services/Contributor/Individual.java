package org.grupo11.Services.Contributor;

import java.util.ArrayList;

import org.grupo11.Enums.DocumentType;

public class Individual extends Contributor {
    private String surname;
    private String birth;
    private int document;
    private DocumentType documentType;

    public Individual(String name, String surname, String address, String birth, int document,
            DocumentType documentType) {
        super(name, address, new ArrayList<>());
        this.surname = surname;
        this.birth = birth;
        this.document = document;
        this.documentType = documentType;
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
