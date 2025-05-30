/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.crypto.*;
import java.io.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AESFileEncryptor {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private final byte[] IV = new byte[16]; // Initialization Vector

    public AESFileEncryptor() {
        // Initialize IV with secure random bytes
        new SecureRandom().nextBytes(IV);
    }

    public static SecretKey generateRandomAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256, new SecureRandom()); // 256-bit AES with secure random
        return keyGen.generateKey();
    }

    public void encryptFile(String password, File inputFile, File outputFile) throws Exception {
        // Generate key from password
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), IV, ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        // Initialize cipher
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(IV));

        // Read input file
        byte[] inputBytes = Files.readAllBytes(inputFile.toPath());

        // Encrypt
        byte[] outputBytes = cipher.doFinal(inputBytes);

        // Write encrypted file
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(outputBytes);
        }
    }

    public void decryptFile(String password, File inputFile, File outputFile) throws Exception {
        // Generate key from password
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), IV, ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        // Initialize cipher
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(IV));

        // Read encrypted file
        byte[] inputBytes = Files.readAllBytes(inputFile.toPath());

        // Decrypt
        byte[] outputBytes = cipher.doFinal(inputBytes);

        // Write decrypted file
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(outputBytes);
        }
    }

    public static void main(String[] args) {
        try {
            AESFileEncryptor fileEncryptor = new AESFileEncryptor();

            // Generate and display a random key (for demonstration)
            SecretKey randomKey = generateRandomAESKey();
            // Example usage
            String password = Base64.getEncoder().encodeToString(randomKey.getEncoded()); // Use a stronger password in production
            System.out.println("Random AES Key: "
                    + password);
            File selectedFile = new File("/home/ahmad/الحسابات.pdf");
            File encryptedFile = new File(selectedFile.getAbsolutePath() + ".enc");
            File decryptedFile = new File(selectedFile.getParent(), "decrypted_" + selectedFile.getName());

            // Encrypt
            fileEncryptor.encryptFile(password, selectedFile, encryptedFile);
            System.out.println("File encrypted successfully: " + encryptedFile.getName());

            // Decrypt
            fileEncryptor.decryptFile(password, encryptedFile, decryptedFile);
            System.out.println("File decrypted successfully: " + decryptedFile.getName());

        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
