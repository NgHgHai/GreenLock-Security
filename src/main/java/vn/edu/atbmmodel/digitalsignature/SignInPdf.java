package vn.edu.atbmmodel.digitalsignature;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.security.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;
import org.bouncycastle.operator.OperatorCreationException;
import vn.edu.atbmmodel.key.KeyGen;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class SignInPdf {
    public static void addDetachedSignatureToPDF(byte[] detachedSignature, String sourcePdfPath, String destinationPdfPath) throws IOException {
        PDDocument document = PDDocument.load(new File(sourcePdfPath));
        PDPage page = new PDPage();
        document.addPage(page);
        PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
        PDSignatureField signatureField = new PDSignatureField(acroForm);
        signatureField.setPartialName("SignatureField");
        signatureField.getWidgets().get(0).setRectangle(new PDRectangle(100, 100, 200, 50)); // Đặt vị trí và kích thước
        signatureField.setValue(Base64.getEncoder().encodeToString(detachedSignature));

        acroForm.getFields().add(signatureField);
        signatureField.getCOSObject().setNeedToBeUpdated(true);
        document.getSignatureFields().add(signatureField);
        document.save(destinationPdfPath);
        document.close();
    }


    public static final String SRC = "src/hai.pdf";
    public static final String DEST = "src/hai%s.pdf";

    public static void main(String[] args) throws GeneralSecurityException, OperatorCreationException, DocumentException, IOException, CMSException {
        KeyPair keyPair = KeyGen.getKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        X509Certificate certificate = KeyGen.genCertificate(keyPair);
        byte[] enc = SignInData.createDetachedSignature("hoanghai".getBytes(), certificate, "SHA256", privateKey);
        addDetachedSignatureToPDF(enc, SRC, String.format(DEST, "1"));
    }
}
