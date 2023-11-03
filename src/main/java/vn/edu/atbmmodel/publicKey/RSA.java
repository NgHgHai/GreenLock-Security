package vn.edu.atbmmodel.publicKey;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import vn.edu.atbm_gui.SymmetricKeyEncpt;
import vn.edu.atbmmodel.key.KeyGen;
import vn.edu.atbmmodel.symmetric.Symmetric;

import javax.crypto.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.*;

public class RSA {
    private KeyPair keyPair;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private KeyStore keyStore;
    private Cipher cipher;

    public RSA() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
    }

    public byte[] encrypt(PublicKey publicKey, byte[] data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public byte[] decrypt(PrivateKey privateKey, byte[] data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }
    public boolean encryptFile(PublicKey publicKey, String source, String dest) throws InvalidKeyException, IOException, FileNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        Cipher cipherAES = Cipher.getInstance("AES/ECB/PKCS5Padding");
        CipherInputStream cipherInputStream = new CipherInputStream(new FileInputStream(source), cipherAES);
        FileOutputStream fos = new FileOutputStream(dest);
        SecretKey keyAES = KeyGen.getKeySymmetric("AES", 128);
        cipherAES.init(Cipher.ENCRYPT_MODE, keyAES);
        int ivlength = Symmetric.getInstance().getIvLength("AES");
        byte[] iv =
        byte[] buffer = new byte[1024];
        int read = 0;


        return true;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        KeyPair pair = KeyGen.getKeyPair();
        PublicKey publicKey1 = pair.getPublic();
        PrivateKey privateKey1 = pair.getPrivate();
        RSA rsa = new RSA();
        byte[] enc = rsa.encrypt(publicKey1, "hoanghai");
        System.out.println();

    }


}
