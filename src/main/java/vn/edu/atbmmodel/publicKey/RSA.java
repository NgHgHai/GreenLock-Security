package vn.edu.atbmmodel.publicKey;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import vn.edu.atbmmodel.key.KeyGen;
import vn.edu.atbmmodel.symmetric.Symmetric;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Base64;

public class RSA {
    private KeyPair keyPair;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private KeyStore keyStore;
    private Cipher cipher;

    static RSA rsa = new RSA();
    public static RSA getInstance(){
        if(rsa == null)
            rsa = new RSA();
        return rsa;
    }
    public void init(String algorithm, String mode, String padding) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding, "BC");
    }

    public byte[] encrypt(PublicKey publicKey, byte[] data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public byte[] decrypt(PrivateKey privateKey, byte[] data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    public boolean encryptFile(PublicKey publicKey, String source, String dest) throws InvalidKeyException, IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        try {
            KeyGen keyGen = KeyGen.getInstance();
            Cipher cipherAES = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey keyAES = keyGen.getKeySymmetric("AES", 128);
            cipherAES.init(Cipher.ENCRYPT_MODE, keyAES);

            File sourceFile = new File(source);

            CipherInputStream cipherInputStream = new CipherInputStream(new FileInputStream(sourceFile), cipherAES);
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(dest)));

            int ivlength = Symmetric.getInstance().getIvLength("AES");
            byte[] iv = new SecureRandom().generateSeed(ivlength);
            byte[] AESKeyEncodeWithRSA = encrypt(publicKey, keyAES.getEncoded());

            dos.writeUTF(Base64.getEncoder().encodeToString(AESKeyEncodeWithRSA));
            dos.writeUTF(Base64.getEncoder().encodeToString(iv));
            dos.writeLong(sourceFile.length());

            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = cipherInputStream.read(buffer)) != -1) {
                dos.write(buffer, 0, read);
                dos.flush();
            }
            dos.flush();
            dos.close();
            cipherInputStream.close();
            return true;
        } catch (Exception e) {
            return false;

        }

    }

    public boolean decryptFile(PrivateKey privateKey, String source, String dest) {
        try {
            Cipher cipherAES = Cipher.getInstance("AES/ECB/PKCS5Padding");

            DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(source)));

            String key = dis.readUTF();
            String ivString = dis.readUTF();
            byte[] iv = Base64.getDecoder().decode(ivString);
            long length = dis.readLong();

            SecretKey keyAES = new SecretKeySpec(decrypt(privateKey, Base64.getDecoder().decode(key)), "AES");
            cipherAES.init(Cipher.DECRYPT_MODE, keyAES);

            CipherOutputStream cipherOutputStream = new CipherOutputStream(new FileOutputStream(dest), cipherAES);

            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = dis.read(buffer)) != -1) {
                cipherOutputStream.write(buffer, 0, read);
                cipherOutputStream.flush();
            }
            cipherOutputStream.flush();
            cipherOutputStream.close();
            dis.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {


    }


}
