package org.grupo11.DTOS;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name = "app_user")
public class User {
    @Id
    @GeneratedValue
    public Long id;
    public String mail;
    public String password;

    public User() {
    }

    public User(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }
}
