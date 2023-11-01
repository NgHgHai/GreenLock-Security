package vn.edu.atbmmodel.digitalsignature;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.*;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import vn.edu.atbmmodel.key.KeyGen;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;


public class SignInPdf {
    public void sign(String src, String temp, String dest, Certificate[] chain, PrivateKey pk,
                     String digestAlgorithm, String provider, PdfSigner.CryptoStandard subfilter,
                     String reason, String location)
            throws GeneralSecurityException, IOException {
        PdfReader reader = new PdfReader(src);

        // Pass the temporary file's path to the PdfSigner constructor
        PdfSigner signer = new PdfSigner(reader, new FileOutputStream(dest), temp, new StampingProperties());

        // Create the signature appearance
        Rectangle rect = new Rectangle(36, 648, 200, 100);
        PdfSignatureAppearance appearance = signer.getSignatureAppearance();
        appearance
                .setReason(reason)
                .setLocation(location)

                // Specify if the appearance before field is signed will be used
                // as a background for the signed field. The "false" value is the default value.
                .setReuseAppearance(false)
                .setPageRect(rect)
                .setPageNumber(1);
        signer.setFieldName("sig");

        IExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
        IExternalDigest digest = new BouncyCastleDigest();

        // Sign the document using the detached mode, CMS or CAdES equivalent.
        signer.signDetached(digest, pks, chain, null, null, null, 0, subfilter);
    }


    public static final String SRC = "src/hai.pdf";
    public static final String DEST = "src/hai%s.pdf";

    public static void main(String[] args) throws GeneralSecurityException, OperatorCreationException, IOException, CMSException {
        Provider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        KeyPair keyPair = KeyGen.getKeyPair();
        Certificate[] chain = {KeyGen.genCertificate(keyPair)};
        PrivateKey privateKey = keyPair.getPrivate();
        String digestAlgorithm = "SHA256";
        String providerName = provider.getName();
        PdfSigner.CryptoStandard subfilter = PdfSigner.CryptoStandard.CMS;
        String reason = "Test";
        String location = "Ha Noi";
        new SignInPdf().sign(SRC, String.format(DEST, 1), String.format(DEST, 2), chain, privateKey, digestAlgorithm, providerName, subfilter, reason, location);

    }
}
