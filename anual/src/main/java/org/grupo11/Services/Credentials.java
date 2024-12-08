package org.grupo11.Services;

import org.grupo11.Enums.UserTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Credentials {
    String mail;
    String password;
    @Id
    Long ownerId;
    @Enumerated(EnumType.STRING)
    UserTypes userType;

    public Credentials(String mail, String password, UserTypes userType, Long ownerId) {
        this.mail = mail;
        this.password = password;
        this.ownerId = ownerId;
        this.userType = userType;
    }

    public Credentials() {
    }

    public String getMail() {
        return this.mail;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public UserTypes getUserType() {
        return this.userType;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setUserType(UserTypes type) {
        this.userType = type;
    }
}
