package vn.edu.atbmmodel.digitalsignature;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;
import org.bouncycastle.operator.OperatorCreationException;
import vn.edu.atbmmodel.key.KeyGen;

import javax.crypto.Cipher;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;

public class SignInPdf {
    public void sign(String src, String dest,
                     Certificate[] chain, PrivateKey pk, String digestAlgorithm, String provider,
                     MakeSignature.CryptoStandard subfilter, String reason, String location)
            throws GeneralSecurityException, IOException, DocumentException {
        // Creating the reader and the stamper
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
        // Creating the appearance
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setReason(reason);
        appearance.setLocation(location);
        appearance.setVisibleSignature(new Rectangle(36, 748, 144, 780), 1, "sig");
        // Creating the signature
        ExternalDigest digest = new ExternalDigest() {
            @Override
            public MessageDigest getMessageDigest(String s) throws GeneralSecurityException {
                return MessageDigest.getInstance(digestAlgorithm, "BCFIPS");
            }
        };
        ExternalSignature signature =
                new ExternalSignature() {
                    @Override
                    public String getHashAlgorithm() {
                        return "SHA256";
                    }

                    @Override
                    public String getEncryptionAlgorithm() {
                        return "RSA";
                    }

                    @Override
                    public byte[] sign(byte[] bytes) throws GeneralSecurityException {
                        Security.addProvider(new BouncyCastleFipsProvider());
                        Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BCFIPS");
                        cipher.init(Cipher.ENCRYPT_MODE, pk);
                        byte[] cipherText = cipher.doFinal(bytes);
                        return cipherText;
                    }
                };
        MakeSignature.signDetached(appearance, digest, signature, chain,
                null, null, null, 0, subfilter);
    }

    public static final String SRC = "src/hai.pdf";
    public static final String DEST = "src/hai%s.pdf";

    public static void main(String[] args) throws GeneralSecurityException, OperatorCreationException, DocumentException, IOException {
        Provider provider = new BouncyCastleFipsProvider();
        Security.addProvider(provider);
        Certificate certificate = KeyGen.genCertificate();
        Certificate[] chain = {certificate};
        KeyPair keyPair = KeyGen.getKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        SignInPdf signInPdf = new SignInPdf();
        signInPdf.sign(SRC, String.format(DEST, 1), chain, privateKey, DigestAlgorithms.SHA256, provider.getName(), MakeSignature.CryptoStandard.CMS, "test1", "thu duc");
    }
}
