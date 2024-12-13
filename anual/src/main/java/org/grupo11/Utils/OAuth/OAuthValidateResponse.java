package org.grupo11.Utils.OAuth;

public class OAuthValidateResponse {
    String email;
    boolean emailVerified;
    String name;

    public OAuthValidateResponse(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }
}
