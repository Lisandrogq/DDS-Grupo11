package org.grupo11.Services.Contact;

public class WhatsApp extends Contact {
    private String number;

    public WhatsApp(String number) {
        this.number = number;
    }

    public void SendNotification() {
        // todo later
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
