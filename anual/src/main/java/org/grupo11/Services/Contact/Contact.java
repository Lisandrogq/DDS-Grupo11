package org.grupo11.Services.Contact;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Contact {
    @Id
    @GeneratedValue
    private Long id;

    public abstract void SendNotification(String subject, String message);
}
