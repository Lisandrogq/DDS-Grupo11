package org.grupo11.Utils;

public class Crypto {
    public static int getRandomId(int length) {
        return Integer.valueOf(String.valueOf(System.nanoTime()).substring(0, length));
    }
}
