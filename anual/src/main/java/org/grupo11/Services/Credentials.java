package org.grupo11.Services;

import org.grupo11.Enums.AuthProvider;
import org.grupo11.Enums.UserTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Credentials {
    @Id
    @GeneratedValue
    private Long id;
    public Long ownerId;
    String mail;
    String password;
    @Enumerated(EnumType.STRING)
    UserTypes userType;
    @Enumerated(EnumType.STRING)
    AuthProvider provider;

    public Credentials(String mail, String password, UserTypes userType, Long ownerId, AuthProvider provider) {
        this.mail = mail;
        this.password = password;
        this.ownerId = ownerId;
        this.userType = userType;
        this.provider = provider;
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

    public AuthProvider getProvider() {
        return this.provider;
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

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }
}
