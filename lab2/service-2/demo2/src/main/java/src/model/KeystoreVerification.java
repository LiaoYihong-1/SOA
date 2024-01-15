package src.model;

import java.io.FileInputStream;
import java.security.KeyStore;

public class KeystoreVerification {
    public static void main(String[] args) {
        String keystorePath = "src/main/resources/server1.p12";
        String keystorePassword = "123456";

        try {
            FileInputStream fis = new FileInputStream(keystorePath);
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            keystore.load(fis, keystorePassword.toCharArray());

            // If loading was successful, print a success message
            System.out.println("Keystore loaded successfully.");

        } catch (Exception e) {
            // If an exception occurs, print an error message
            System.err.println("Failed to load the keystore: " + e.getMessage());
        }
    }
}