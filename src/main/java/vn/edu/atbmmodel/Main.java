package vn.edu.atbmmodel;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.Security;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


public class Main {
        public static void main(String[] args) throws Exception {
            // Thêm Bouncy Castle Provider
            Security.addProvider(new BouncyCastleFipsProvider());

            // Dữ liệu cần mã hóa
            String plainText = "Hello, World!";
            byte[] plaintextBytes = plainText.getBytes("UTF-8");

            // Khóa bí mật và vectơ khởi đầu
            byte[] keyBytes = "1234567890123456".getBytes("UTF-8");


            Key key = new SecretKeySpec(keyBytes, "AES");


            // Mã hóa
            Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BCFIPS");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = encryptCipher.doFinal(plaintextBytes);

            // Giải mã
            Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BCFIPS");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = decryptCipher.doFinal(encryptedBytes);

            // In kết quả
            String decryptedText = new String(decryptedBytes, "UTF-8");
            System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encryptedBytes));
            System.out.println("Decrypted: " + decryptedText);
        }


}