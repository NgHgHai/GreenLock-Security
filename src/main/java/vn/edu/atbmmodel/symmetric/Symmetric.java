package vn.edu.atbmmodel.symmetric;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;


public class Symmetric {
    int ivLength;
    byte[] iv; // Kích thước IV cho AES-CBC là 16 byte
    Cipher encryptCipher;
    Cipher decryptCipher;
    String algorithm;

    public void init(String algorithm, String mode, String padding, int ivLength) throws Exception {
        this.algorithm = algorithm;
        this.ivLength = ivLength;
        Security.addProvider(new BouncyCastleProvider());
        encryptCipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding, "BC");
        decryptCipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding, "BC");
    }

    static Symmetric symmetric = new Symmetric();

    public static Symmetric getInstance() {
        if (symmetric == null)
            symmetric = new Symmetric();
        return symmetric;
    }

    Symmetric() {
    }
    public int getIvLength(String algorithm) {
        if (algorithm.equals("AES"))
            return 16;
        else if (algorithm.equals("DES"))
            return 8;
        else if (algorithm.equals("DESede"))
            return 8;
        else if (algorithm.equals("RC2"))
            return 8;
        else if (algorithm.equals("RC4"))
            return 8;
        else if (algorithm.equals("Blowfish"))
            return 8;
        else if (algorithm.equals("IDEA"))
            return 8;
        else if (algorithm.equals("RC5"))
            return 8;
        else if (algorithm.equals("RC6"))
            return 8;
        else if (algorithm.equals("SEED"))
            return 8;
        else if (algorithm.equals("Skipjack"))
            return 8;
        else if (algorithm.equals("TEA"))
            return 8;
        else if (algorithm.equals("XTEA"))
            return 8;
        else if (algorithm.equals("GOST28147"))
            return 8;
        else if (algorithm.equals("Noekeon"))
            return 8;
        else if (algorithm.equals("Serpent"))
            return 8;
        else if (algorithm.equals("Twofish"))
            return 8;
        else if (algorithm.equals("CAST5"))
            return 8;
        else if (algorithm.equals("CAST6"))
            return 8;
        else if (algorithm.equals("VMPC"))
            return 8;
        else if (algorithm.equals("VMPC-KSA3"))
            return 8;
        else if (algorithm.equals("XTEA"))
            return 8;
        else if (algorithm.equals("HC-128"))
            return 16;
        else if (algorithm.equals("HC-256"))
            return 32;
        else if (algorithm.equals("ISAAC"))
            return 8;
        else if (algorithm.equals("ISAAC-64"))
            return 8;
        else if (algorithm.equals("Salsa20"))
            return 8;
        else if (algorithm.equals("Salsa20-12"))
            return 8;
        return 0;
    }
    public byte[] readKeyFromFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        byte[] key = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);    //read file
        fis.read(key);    //store file data in key
        return key;
    }
    public byte[] encrypt(byte[] plaintextBytes, byte[] key) throws Exception {
        iv = new SecureRandom().generateSeed(ivLength);
        encryptCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, algorithm), new IvParameterSpec(iv));
        byte[] enc = encryptCipher.doFinal(plaintextBytes);
        byte[] result = new byte[ivLength + enc.length];
        System.arraycopy(iv, 0, result, 0, ivLength);
        System.arraycopy(enc, 0, result, ivLength, enc.length);
        return result;
    }

    public byte[] decrypt(byte[] encryptedBytes, byte[] key) throws Exception {
        byte[] result;
        iv = new byte[ivLength]; // Kích thước IV cho AES-CBC là 16 byte
        System.arraycopy(encryptedBytes, 0, iv, 0, ivLength);
        decryptCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, algorithm), new IvParameterSpec(iv));
        result = decryptCipher.doFinal(encryptedBytes, ivLength, encryptedBytes.length - ivLength);
        return result;
    }

    public boolean encryptFile(String source, byte[] key, String des) throws Exception {
        File file = new File(source);
        if (!file.exists()) {
            return false;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        iv = new SecureRandom().generateSeed(ivLength);
        Key key1 = new SecretKeySpec(key, algorithm);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key1, new IvParameterSpec(iv));
        FileOutputStream fileOutputStream = new FileOutputStream(des);
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

    public boolean decryptFile(String source, byte[] key, String des) throws Exception {
        iv = new byte[ivLength]; // Kích thước IV cho AES-CBC là 16 byte
        File file = new File(source);
        if (!file.exists()) {
            return false;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(iv);
        decryptCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, algorithm), new IvParameterSpec(iv));
        FileOutputStream fileOutputStream = new FileOutputStream(des );
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

    private String getExtension(String source) {
        String extension = "";
        int i = source.lastIndexOf('.');
        if (i > 0) {
            extension = source.substring(i);
        }
        return extension;
    }

    public static void main(String[] args) throws Exception {
//        try {
//            symmetric.init("AES", "CBC", "PKCS5Padding");
////            symmetric.encryptFile("I:\\swingATBM\\test.txt", "1234567890123456", "128");
//            symmetric.decryptFile("I:\\swingATBM\\test.txt-encrypted.enc", "1234567890123456", "128");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        symmetric.init("AES", "CBC", "PKCS7Padding", Symmetric.getInstance().getIvLength("AES"));
        String key = "1234567890123456";
        try {
            byte[] enc = symmetric.encrypt("Hello, World!".getBytes(), key.getBytes());
            String encBase64 = Base64.getEncoder().encodeToString(enc);
            byte[] dec = symmetric.decrypt(Base64.getDecoder().decode(encBase64), key.getBytes());
            System.out.println(new String(dec));
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
//        System.out.println(new String(enc, "UTF-8"));


    }


}
