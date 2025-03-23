package pt.nb.dsi;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Utils1 {

    /**
     * Computes the SHA-256 hash of the input string and returns it as a
     * base64-encoded string.
     * 
     * @param input the input string
     * @return the SHA-256 hash of the input string as a base64-encoded string
     */
    public static String sha256Base64(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception (e.g., log it, throw a runtime exception)
            // It's highly unlikely that SHA-256 is not available.
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

}
