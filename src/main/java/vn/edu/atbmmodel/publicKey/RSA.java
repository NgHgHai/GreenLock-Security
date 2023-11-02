package vn.edu.atbmmodel.publicKey;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import vn.edu.atbmmodel.key.KeyGen;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

public class RSA {
    private KeyPair keyPair;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private KeyStore keyStore;
    private Cipher cipher;

    public RSA() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BCFIPS");
    }

    public byte[] encrypt(PublicKey publicKey, String data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data.getBytes());
    }

    public String decrypt(PrivateKey privateKey, byte[] data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] plain = cipher.doFinal(data);
        return new String(plain);
    }

//    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
//        KeyPair pair = KeyGen.getKeyPair();
//        PublicKey publicKey1 = pair.getPublic();
//        PrivateKey privateKey1 = pair.getPrivate();
//        RSA rsa = new RSA();
//        byte[] enc = rsa.encrypt(publicKey1, "hoanghai");
//        System.out.println(rsa.decrypt(privateKey1, enc));
//    }


}
