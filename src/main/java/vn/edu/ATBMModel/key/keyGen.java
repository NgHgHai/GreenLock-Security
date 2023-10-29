package vn.edu.ATBMModel.key;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class keyGen {
    public static KeyPair keyGen() throws NoSuchAlgorithmException {
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        return keyPair;
    }
    public static PublicKey keyPubKey(byte[] data) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(data));
        return publicKey;
    }

    public static void main(String[] args) {
        try {
            KeyPair keyPair= keyGen();

            byte[] data = keyPair.getPublic().getEncoded();

            PublicKey publicKey = keyPubKey(data);

            System.out.println(publicKey.equals(keyPair.getPublic()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
