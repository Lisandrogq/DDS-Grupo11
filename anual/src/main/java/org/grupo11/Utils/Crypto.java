package org.grupo11.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypto {
    public static int getRandomId(int length) {
        return Integer.valueOf(String.valueOf(System.nanoTime()).substring(0, length));
    }

    public static Long genId() {
        return Long.valueOf(String.valueOf(System.nanoTime()).substring(0, 14));
    }

    public static String sha256Hash(byte[] input) {
        try {
            MessageDigest hasher = MessageDigest.getInstance("SHA-256");
            byte[] digest = hasher.digest(input);
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }
}
