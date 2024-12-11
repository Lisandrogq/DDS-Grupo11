package org.grupo11.Utils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.grupo11.Logger;
import org.grupo11.Enums.DocumentType;

public class CSVImput {
    DocumentType documentType;
    int document;
    String name;
    String surname;
    String mail;
    long contributionDate;
    ContributionTypeField contributionType;
    int quantity;

    public CSVImput(DocumentType documentType, int document, String name, String surname, String mail, long contributionDate, ContributionTypeField contributionType, int quantity) {
        this.documentType = documentType;
        this.document = document;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.contributionDate = contributionDate;
        this.contributionType = contributionType;
        this.quantity = quantity;
    }

    public static CSVImput processField(String field) throws ParseException {
        try {
            List<String> fields = Arrays.asList(field.split(","));

            // DocumentType
            if (!Arrays.stream(DocumentType.values()).anyMatch((DocumentType d) -> d.name().equals(fields.get(0)))) {
                throw new Exception("Invalid DocumentType");}
            DocumentType documentType = DocumentType.valueOf(fields.get(0));

            // Document (Max 10 digits)
            if (fields.get(1).length() > 10) {
                throw new Exception("Document is too long");}
            int document = Integer.parseInt(fields.get(1));

            // Name (Max 50 digits)
            if (fields.get(2).length() > 50) {
                throw new Exception("Name is too long");}
            String name = fields.get(2);

            // Surname (Max 50 digits)
            if (fields.get(3).length() > 50) {
                throw new Exception("Surname is too long");}
            String surname = fields.get(3);

            // Email (Max 50 digits)
            if (fields.get(4).length() > 50) {
                throw new Exception("Email is too long");}
            String mail = fields.get(4);
            if (!FieldValidator.isEmail(mail)) {
                throw new Exception("Invalid email");}

            // ContributionDate (dd/MM/yyyy)
            if (fields.get(5).length() != 10) {
                throw new Exception("Invalid date");}
            long contributionDate = DateUtils.parseDateString(fields.get(5));
            Logger.info("Parsed date: " + contributionDate);

            // ContributionType
            if (!Arrays.stream(ContributionTypeField.values()).anyMatch((ContributionTypeField c) -> c.name().equals(fields.get(6)))) {
                throw new Exception("Invalid ContributionType");}
            ContributionTypeField contributionType = ContributionTypeField.valueOf(fields.get(6));

            // Quantity (Max 7 digits)
            if (fields.get(7).length() > 7) {
                throw new Exception("Quantity is too long");}
            int quantity = Integer.parseInt(fields.get(7));

            return new CSVImput(documentType, document, name, surname, mail, contributionDate, contributionType, quantity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ContributionTypeField getType(String type) {
        switch (type) {
            case "DINERO":
                return ContributionTypeField.DINERO;
            case "DONACION_VIANDAS":
                return ContributionTypeField.DONACION_VIANDAS;
            case "REDISTRIBUCION_VIANDAS":
                return ContributionTypeField.REDISTRIBUCION_VIANDAS;
            case "ENTREGA_TARJETAS":
                return ContributionTypeField.ENTREGA_TARJETAS;
            default:
                return null;
        }
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public int getDocument() {
        return document;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getMail() {
        return mail;
    }

    public long getContributionDate() {
        return contributionDate;
    }

    public ContributionTypeField getContributionType() {
        return contributionType;
    }

    public int getQuantity() {
        return quantity;
    }

}
