package vn.edu.atbmmodel.symmetric;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;


public class Symmetric {
    int ivLength;
    byte[] iv; // Kích thước IV cho AES-CBC là 16 byte
    Cipher encryptCipher;
    Cipher decryptCipher;
    String algorithm;

    public void init(String algorithm, String mode, String standard, int ivLength) throws Exception {
        this.algorithm = algorithm;
        this.ivLength = ivLength;
        Security.addProvider(new BouncyCastleProvider());
        encryptCipher = Cipher.getInstance(algorithm + "/" + mode + "/" + standard, "BCFIPS");
        decryptCipher = Cipher.getInstance(algorithm + "/" + mode + "/" + standard, "BCFIPS");
    }

    static Symmetric symmetric = new Symmetric();

    public static Symmetric getInstance() {
        if (symmetric == null)
            symmetric = new Symmetric();
        return symmetric;
    }

    Symmetric() {
    }

    public byte[] encrypt(byte[] plaintextBytes, String key) throws Exception {
        iv = new SecureRandom().generateSeed(ivLength);
        encryptCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("UTF-8"), algorithm), new IvParameterSpec(iv));
        byte[] enc = encryptCipher.doFinal(plaintextBytes);
        byte[] result = new byte[ivLength + enc.length];
        System.arraycopy(iv, 0, result, 0, ivLength);
        System.arraycopy(enc, 0, result, ivLength, enc.length);
        return result;
    }

    public byte[] decrypt(byte[] encryptedBytes, String key) throws Exception {
        byte[] result;
        iv = new byte[ivLength]; // Kích thước IV cho AES-CBC là 16 byte
        System.arraycopy(encryptedBytes, 0, iv, 0, ivLength);
        decryptCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("UTF-8"), algorithm), new IvParameterSpec(iv));
        result = decryptCipher.doFinal(encryptedBytes, ivLength, encryptedBytes.length - ivLength);
        return result;
    }

    public boolean encryptFile(String source, String key, String keyLength) throws Exception {
        File file = new File(source);
        if (!file.exists()) {
            return false;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        iv = new SecureRandom().generateSeed(ivLength);
        Key key1 = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key1, new IvParameterSpec(iv));
        FileOutputStream fileOutputStream = new FileOutputStream(file.getPath() + "-encrypted" + ".enc");
        CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, encryptCipher);
        cipherOutputStream.write(iv);
        cipherOutputStream.flush();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fileInputStream.read(buffer)) != -1) {
            cipherOutputStream.write(buffer, 0, length);
        }
        fileInputStream.close();
        cipherOutputStream.flush();
        cipherOutputStream.close();
        return true;
    }

    public boolean decryptFile(String source, String key, String keyLength) throws Exception {
        iv = new byte[ivLength]; // Kích thước IV cho AES-CBC là 16 byte
        File file = new File(source);
        if (!file.exists()) {
            return false;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(iv);
        decryptCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("UTF-8"), algorithm), new IvParameterSpec(iv));
        FileOutputStream fileOutputStream = new FileOutputStream(file.getPath() + "-decrypted" + ".txt");
        CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, decryptCipher);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fileInputStream.read(buffer)) != -1) {
            cipherOutputStream.write(buffer, 0, length);
        }
        fileInputStream.close();
        cipherOutputStream.flush();
        cipherOutputStream.close();
        return true;
    }

    public static void main(String[] args) throws Exception {
//        try {
//            symmetric.init("AES", "CBC", "PKCS5Padding");
////            symmetric.encryptFile("I:\\swingATBM\\test.txt", "1234567890123456", "128");
//            symmetric.decryptFile("I:\\swingATBM\\test.txt-encrypted.enc", "1234567890123456", "128");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        symmetric.init("CAST5 ", "CFB", "NoPadding", 8);
        String key = "1234";
        byte[] enc = symmetric.encrypt("Hello, World!".getBytes("UTF-8"), key);
//        System.out.println(new String(enc, "UTF-8"));
        byte[] dec = symmetric.decrypt(enc, key);
        System.out.println(new String(dec));
    }
}
