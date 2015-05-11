package tk.wlemuel.cotable.utils;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Utils for encryption and decryption.
 *
 * @author Winter Lau
 * @updated Steve Lemuel
 * @created 2011-12-26
 */
public class CyptoUtils {

    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

    /**
     * DES Algorithm，encryption
     *
     * @param data the string needs to be encrypted.
     * @param key  secretKey, longer than 8 bytes.
     * @return the encrypted data.
     */
    public static String encode(String key, String data) {
        if (data == null)
            return null;
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // The secretkey ought to longer than 8 bytes.
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
//            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec;
            paramSpec = new IvParameterSpec("12345678".getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            byte[] bytes = cipher.doFinal(data.getBytes());
            return byte2hex(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * DES Algorithm，decryption
     *
     * @param data the string needs to be decrypted.
     * @param key  secretKey, longer than 8 bytes.
     * @return the decrypted data.
     */
    public static String decode(String key, String data) {
        if (data == null)
            return null;
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // The secretkey ought to longer than 8 bytes.
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
//            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec;
            paramSpec = new IvParameterSpec("12345678".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return new String(cipher.doFinal(hex2byte(data.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * Convert the binary to String
     *
     * @param b the byte array.
     * @return {@code String}
     */
    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    // TODO Don't understand this method.
    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

}
