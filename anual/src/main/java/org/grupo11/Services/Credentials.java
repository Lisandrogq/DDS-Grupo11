package org.grupo11.Services;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Enums.AuthProviders;
import org.grupo11.Enums.UserTypes;

import jakarta.persistence.ElementCollection;
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
    @ElementCollection
    @Enumerated(EnumType.STRING)
    List<AuthProviders> providers = new ArrayList<>();

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

    public List<AuthProviders> getProviders() {
        return this.providers;
    }

    public AuthProviders getProvidersByValue(AuthProviders provider) {
        for (AuthProviders x : providers) {
            if (x.compareTo(provider) == 0) {
                return x;
            }
        }
        return null;
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

    public void addProvider(AuthProviders provider) {
        this.providers.add(provider);
    }
}
