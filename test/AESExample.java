/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ahmad
 */
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESExample {

    public static void main(String[] args) throws Exception {
        // Generate a secret key
        SecretKey secretKey = generateKey(128); // 128, 192, or 256 bits
        
        // Generate initialization vector (IV)
        byte[] iv = generateIV();
        
        // Original message
        String originalMessage = "This is a secret message";
        System.out.println("Original: " + originalMessage);
        
        // Encrypt
        String encrypted = encrypt(originalMessage, secretKey, iv);
        System.out.println("Encrypted: " + encrypted);
        
        // Decrypt
        String decrypted = decrypt(encrypted, secretKey, iv);
        System.out.println("Decrypted: " + decrypted);
    }
    
    // Generate AES key
    public static SecretKey generateKey(int keySize) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keySize);
        return keyGenerator.generateKey();
    }
    
    // Generate initialization vector
    public static byte[] generateIV() {
        byte[] iv = new byte[16]; // 16 bytes for AES
        new SecureRandom().nextBytes(iv);
        return iv;
    }
    
    // Encrypt with AES/CBC/PKCS5Padding
    public static String encrypt(String input, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] encrypted = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }
    
    // Decrypt with AES/CBC/PKCS5Padding
    public static String decrypt(String encrypted, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(original);
    }
}