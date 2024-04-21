package org.grupo11.utils;

import java.util.Random;

public class Crypto {
    public static int generateRandomCode(int length) {
        Random random = new Random();
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;

        return random.nextInt(max - min + 1) + min;
    }
}
