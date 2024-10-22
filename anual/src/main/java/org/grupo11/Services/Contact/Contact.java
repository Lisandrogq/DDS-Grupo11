package org.grupo11.Services.Contact;

import jakarta.persistence.Entity;

@Entity
public abstract class Contact {
    public abstract void SendNotification(String subject, String message);
}
