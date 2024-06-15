package org.grupo11.Services.Contact;

public class Phone extends Contact {
    private String number;

    public Phone(String number) {
        this.number = number;
    }

    public void SendNotification(String subject, String message) {
        // todo later
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
