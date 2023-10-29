package vn.edu.ATBMModel.publicKey;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

public class RSA {
    private KeyPair keyPair;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private KeyStore keyStore;
    private Cipher cipher;

    public RSA() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BCFIPS");
    }

}
