package org.grupo11.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class PasswordValidator {

    public static class Result {
        public boolean valid;
        public List<String> reasons = new ArrayList<String>();

        public void addReason(String message) {
            reasons.add(message);
        }
    }

    // This function reads a file with the 100000 worst passwords and checks if the
    // password is in the file

    private static boolean IsKnownPassword(String pw) {
        String filePath = "src/main/resources/rockyou.txt";

        try {
            List<String> rockyou = Files.readAllLines(Paths.get(filePath));
            return !rockyou.contains(pw);
        } catch (IOException error) {
            error.printStackTrace();
        }
        return false;

    }

    private static boolean HasMinimumLength(String pw) {
        return pw.length() >= 12;
    }

    private static boolean HasUppercaseLetter(String pw) {
        return Pattern.compile("[A-Z]").matcher(pw).find();
    }

    private static boolean HasNumber(String pw) {
        return Pattern.compile("\\d").matcher(pw).find();
    }

    private static boolean HasSpecialSymbol(String pw) {
        return Pattern.compile("[^a-zA-Z0-9]").matcher(pw).find();
    }

    public static Result IsValidPassword(String pw) {
        Result result = new Result();

        if (!HasMinimumLength(pw))
            result.addReason("the pw has to be at least 12 characters");
        if (!HasUppercaseLetter(pw))
            result.addReason("the pw has to have at least one uppercase letter");
        if (!HasNumber(pw))
            result.addReason("the pw has to have at least one number");
        if (!HasSpecialSymbol(pw))
            result.addReason("the pw has to have at least one special symbol");
        if (!IsKnownPassword(pw))
            result.addReason("the pw is in the top 100000 worst pws");

        result.valid = result.reasons.size() == 0;

        return result;
    }
}
