package kr.kshgroup.blockchain.utils;

import java.security.MessageDigest;

public class HashUtils {
    public static String SHA256(String input) {
        return SHA256(input, "UTF-8");
    }

    public static String SHA256(String input, String encoding) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(encoding));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
