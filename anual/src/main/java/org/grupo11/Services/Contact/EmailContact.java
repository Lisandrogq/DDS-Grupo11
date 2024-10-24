package org.grupo11.Services.Contact;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Config.Env;

import jakarta.persistence.Entity;

@Entity
public class EmailContact extends Contact {
    private String mail;
    private List<String> notifications;

    public EmailContact() {
    }

    public EmailContact(String mail) {
        this.mail = mail;
        this.notifications = new ArrayList<>();
    }

    public void SendNotification(String subject, String message) {
        System.out.println(Env.getCompanyMail());
        // SendGrid.SendMail(Env.getCompanyMail(), mail, subject, message);
        notifications.add(message);
    }

    public String getMail() {
        return this.mail;
    }

    public List<String> getNotifications() {
        return this.notifications;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

}
