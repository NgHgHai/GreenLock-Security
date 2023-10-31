package vn.edu.atbmmodel.digitalsignature;

import com.itextpdf.text.pdf.security.ExternalSignature;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Security;

public class RSASignaure implements ExternalSignature {
   private String hashAlgorithm;
    private PrivateKey privateKey;

    public RSASignaure(String hashAlgorithm, PrivateKey privateKey) {
        this.hashAlgorithm = hashAlgorithm;
        this.privateKey = privateKey;
    }

    @Override
    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    @Override
    public String getEncryptionAlgorithm() {
        return "RSA";
    }

    @Override
    public byte[] sign(byte[] bytes) throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleFipsProvider());
        Cipher cipher = Cipher.getInstance("RSA", "BCFIPS");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encrypted = cipher.doFinal(bytes);
        return encrypted;
    }
}
