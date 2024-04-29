package org.grupo11.app;

import org.grupo11.Services.PasswordValidator;

public class App {

    public void ValidatePassword(String pw) {
        PasswordValidator.Result result = PasswordValidator.IsValidPassword(pw);
        if (!result.valid) {
            System.out.println("Your password is invalid, reason: " + result.reasons.toString());
            return;
        }

        System.out.println("Password is valid");
    }
}
