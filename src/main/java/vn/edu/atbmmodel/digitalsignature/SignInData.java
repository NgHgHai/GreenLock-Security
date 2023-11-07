package vn.edu.atbmmodel.digitalsignature;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import vn.edu.atbmmodel.key.KeyGen;
import vn.edu.atbmmodel.tool.ReadKeyFormFile;


import java.io.*;
import java.nio.file.Files;
import java.security.*;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

public class SignInData {
    public static byte[] createDigitalSignature(String data, String algorithm, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(algorithm, "BC");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        return signature.sign();
    }

    public static boolean createDigitalSignatureFile(String source, String dest, String algorithm, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, IOException {
        try {
            Signature signature = Signature.getInstance(algorithm, "BC");
            signature.initSign(privateKey);
            FileInputStream fis = new FileInputStream(source);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) == -1) {
                signature.update(buffer, 0, len);
            }

            FileOutputStream fos = new FileOutputStream(dest);
            fos.write(signature.sign());
            fos.flush();
            fos.close();
            fis.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean verifyDigitalSignature(String data, byte[] signature, String algorithm, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(algorithm, "BC");
        sig.initVerify(publicKey);
        sig.update(data.getBytes());
        return sig.verify(signature);
    }

    public static boolean verifyDigitalSignatureFile(String source, String signatureFile, String algorithm, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, IOException {
        Signature sig = Signature.getInstance(algorithm, "BC");
        sig.initVerify(publicKey);
        FileInputStream fis = new FileInputStream(source);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer)) == -1) {
            sig.update(buffer, 0, len);
        }
        fis.close();
        byte[] signature = Files.readAllBytes(new File(signatureFile).toPath());
        return sig.verify(signature);
    }

    public static byte[] createDetachedSignatureWithCert(byte[] data, X509Certificate certificate, String algorithm, PrivateKey privateKey) throws IOException, OperatorCreationException, CertificateEncodingException, CMSException {
        List<X509Certificate> certList = new ArrayList<>();
        certList.add(certificate);
        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        CMSTypedData msg = new CMSProcessableByteArray(data);

        Store certs = new JcaCertStore(certList);
        DigestCalculatorProvider digProvider = new JcaDigestCalculatorProviderBuilder()
                .setProvider("BC").build();
        JcaSignerInfoGeneratorBuilder signerInfoGeneratorBuilder =
                new JcaSignerInfoGeneratorBuilder(digProvider);
        ContentSigner signer = new JcaContentSignerBuilder(algorithm)
                .setProvider("BC").build(privateKey);

        gen.addSignerInfoGenerator(signerInfoGeneratorBuilder.build(signer, certificate));
        gen.addCertificates(certs);
        return gen.generate(msg, true).getEncoded();
    }

    public static boolean createDetachedSignatureWithCertFile(String source, String dest, X509Certificate certificate, String algorithm, PrivateKey privateKey) {
        try {
            List<X509Certificate> certList = new ArrayList<>();
            certList.add(certificate);
            CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
            CMSTypedData msg = new CMSProcessableFile(new File(source));
            Store certs = new JcaCertStore(certList);

            DigestCalculatorProvider digProvider = new JcaDigestCalculatorProviderBuilder()
                    .setProvider("BC").build();
            JcaSignerInfoGeneratorBuilder signerInfoGeneratorBuilder =
                    new JcaSignerInfoGeneratorBuilder(digProvider);
            ContentSigner signer = new JcaContentSignerBuilder(algorithm)
                    .setProvider("BC").build(privateKey);

            gen.addSignerInfoGenerator(signerInfoGeneratorBuilder.build(signer, certificate));
            gen.addCertificates(certs);

            FileOutputStream fos = new FileOutputStream(dest);
            fos.write(gen.generate(msg, true).getEncoded());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean verifyDetachedData(byte[] cmsSignedData, byte[] data)
            throws GeneralSecurityException, OperatorCreationException, CMSException {
        CMSSignedData signedData = new CMSSignedData(
                new CMSProcessableByteArray(data), cmsSignedData);
        Store certStore = signedData.getCertificates();
        SignerInformationStore signers = signedData.getSignerInfos();
        Collection c = signers.getSigners();
        Iterator it = c.iterator();
        while (it.hasNext()) {
            SignerInformation signer = (SignerInformation) it.next();
            Collection certCollection = certStore.getMatches(signer.getSID());
            Iterator certIt = certCollection.iterator();
            X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
            if (!signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert))) {
                return false;
            }
        }
        return true;
    }

    public static boolean verifyDetachedDataFile(String source, String cmsSignedDataFile) {
        try {
            CMSSignedData signedData = new CMSSignedData(
                    new CMSProcessableFile(new File(source)), Files.readAllBytes(new File(cmsSignedDataFile).toPath()));
            Store certStore = signedData.getCertificates();
            SignerInformationStore signers = signedData.getSignerInfos();
            Collection c = signers.getSigners();
            Iterator it = c.iterator();
            while (it.hasNext()) {
                SignerInformation signer = (SignerInformation) it.next();
                Collection certCollection = certStore.getMatches(signer.getSID());
                Iterator certIt = certCollection.iterator();
                X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
                if (!signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert))) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws GeneralSecurityException, OperatorCreationException, IOException, CMSException {
        KeyGen keyGen = KeyGen.getInstance();
        PrivateKey privateKey = keyGen.getPrivateKeyformBytes(ReadKeyFormFile.readKeyFromFile("src/privateKey.key"));
        PublicKey publicKey = keyGen.getPublicKeyformBytes(ReadKeyFormFile.readKeyFromFile("src/publicKey.key"));
        createDigitalSignatureFile("src/privateKey.key", "src/privateKey.sig", "SHA256withRSA", privateKey);
        System.out.println(verifyDigitalSignatureFile("src/privateKey.key", "src/privateKey.sig", "SHA256withRSA", publicKey));

    }
}

