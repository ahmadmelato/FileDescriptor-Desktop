/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class AESKeyGenerator {
    public static String generateRandomAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // Use 256-bit AES
        return java.util.Base64.getEncoder().encodeToString(keyGen.generateKey().getEncoded());
    }
    
    public static void main(String[] args) {
        try {
            System.out.println("Key (Base64): " + generateRandomAESKey());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
