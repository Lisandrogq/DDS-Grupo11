package org.grupo11.Services.Contact;

import org.grupo11.Config.Env;
import org.grupo11.Utils.SendGrid;

public class EmailContact extends Contact {
    private String mail;

    public EmailContact(String mail) {
        this.mail = mail;
    }

    public void SendNotification(String subject, String message) {

        System.out.println(Env.getCompanyMail());
        SendGrid.SendMail(Env.getCompanyMail(), mail, subject, message);
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

}
