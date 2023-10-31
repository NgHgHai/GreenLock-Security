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


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;

import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SignInData {
    public static byte[] createDigitalSignature(String data, String hashAlgorithm, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(hashAlgorithm, "BCFIPS");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        return signature.sign();
    }

    public static boolean verifyDigitalSignature(String data, byte[] signature, String hashAlgorithm, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(hashAlgorithm, "BCFIPS");
        sig.initVerify(publicKey);
        sig.update(data.getBytes());
        return sig.verify(signature);
    }

    public static byte[] createDetachedSignature(byte[] data, X509Certificate certificate, String hashAlgorithm, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, IOException, OperatorCreationException, CertificateEncodingException, CMSException {
        List<X509Certificate> certList = new ArrayList<>();
        CMSTypedData msg = new CMSProcessableByteArray(data);
        certList.add(certificate);
        Store certs = new JcaCertStore(certList);
        DigestCalculatorProvider digProvider = new JcaDigestCalculatorProviderBuilder()
                .setProvider("BCFIPS").build();
        JcaSignerInfoGeneratorBuilder signerInfoGeneratorBuilder =
                new JcaSignerInfoGeneratorBuilder(digProvider);
        ContentSigner signer = new JcaContentSignerBuilder(hashAlgorithm + "withRSA")
                .setProvider("BCFIPS").build(privateKey);

        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        gen.addSignerInfoGenerator(signerInfoGeneratorBuilder.build(signer, certificate));
        gen.addCertificates(certs);
        return gen.generate(msg, true).getEncoded();
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
            if (!signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BCFIPS").build(cert))) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws GeneralSecurityException, OperatorCreationException, IOException, CMSException {
        KeyPair pair = KeyGen.getKeyPair();
        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();
        byte[] data = "hoanghai".getBytes();
        X509Certificate certificate = KeyGen.genCertificate(pair);
        byte[] enc = createDetachedSignature(data, certificate, "SHA256", privateKey);
        System.out.println(verifyDetachedData(enc, data));

    }
}

