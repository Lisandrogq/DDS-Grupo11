package org.grupo11.Utils;

import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.apache.commons.codec.DecoderException;

public class Crypto {
    public static Long getRandomId(int length) {
        String nanoTime = String.valueOf(System.nanoTime());
        if (nanoTime.length() < length) {
            nanoTime = String.format("%-" + length + "s", nanoTime).replace(' ', '0');
        }
        return Long.valueOf(nanoTime.substring(0, length));
    }

    public static String getRandomIdAsString(int length) {
        String nanoTime = String.valueOf(System.nanoTime());
        if (nanoTime.length() < length) {
            nanoTime = String.format("%-" + length + "s", nanoTime).replace(' ', '0');
        }
        return nanoTime.substring(0, length);
    }

    public static Long genId() {
        String nanoTime = String.valueOf(System.nanoTime());
        if (nanoTime.length() < 14) {
            nanoTime = String.format("%-14s", nanoTime).replace(' ', '0');
        }
        return Long.valueOf(nanoTime.substring(0, 14));
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

    public static ECPrivateKey loadPrivateKey(String privKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException {
        byte[] decoded = Base64.getDecoder().decode(privKey);
        KeyFactory kf = KeyFactory.getInstance("EC");
        EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        ECPrivateKey privateKey = (ECPrivateKey) kf.generatePrivate(keySpec);
        return privateKey;
    }

    public static ECPublicKey loadPublicKey(String pubKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException {
        byte[] decoded = Base64.getDecoder().decode(pubKey);
        KeyFactory kf = KeyFactory.getInstance("EC");
        EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        ECPublicKey publicKey = (ECPublicKey) kf.generatePublic(keySpec);
        return publicKey;
    }
}
