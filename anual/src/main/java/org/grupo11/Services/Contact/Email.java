package org.grupo11.Services.Contact;

public class Email extends Contact {
    private String mail;

    public Email(String mail) {
        this.mail = mail;
    }

    public void SendNotification() {
        // todo later
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

}
