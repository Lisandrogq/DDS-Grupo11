package org.grupo11.Utils;

public class FieldValidator {

    public static <E extends Enum<E>> boolean isValidEnumValue(Class<E> enumClass, String value) {
        if (value == null) {
            return false;
        }
        try {
            Enum.valueOf(enumClass, value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public static boolean isEmail(String value) {
        // Simple regex check for email format
        if (value == null || !value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return false;
        }
        return true;
    }

    public static boolean isDate(String value) {
        try {
            DateUtils.parseDateString(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
