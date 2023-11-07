package vn.edu.atbmmodel.key;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import vn.edu.atbmmodel.tool.ReadKeyFormFile;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

public class KeyGen {

    public static KeyGen getInstance() {
        if (instance == null) {
            instance = new KeyGen();
        }
        return instance;
    }

    private static KeyGen instance;

    private KeyGen() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public  KeyPair getKeyPair(int size) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(size);
        return keyPairGenerator.generateKeyPair();
    }

    public  PublicKey getPublicKeyformBytes(byte[] data) {
        PublicKey publicKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
            publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(data));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public  PrivateKey getPrivateKeyformBytes(byte[] key) {
        PrivateKey privateKey = null;
        try {
            privateKey = KeyFactory.getInstance("RSA", "BC").generatePrivate(new PKCS8EncodedKeySpec(key));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public  SecretKey getKeySymmetric(String algorithm, int size) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, "BC");
        keyGenerator.init(size);
        return keyGenerator.generateKey();
    }

    public  X509Certificate genCertificate(KeyPair key, String issuerNameString, BigInteger seri) throws NoSuchAlgorithmException, OperatorCreationException, CertificateException {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) key.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) key.getPrivate();

        X500Name issuerName = new X500Name("CN=" + issuerNameString);
        X500Name subjectName = new X500Name("CN=Hoang Hai, O=GreenTea Group , OU=Students , L=Thu Duc, ST=HCM, C=vietnamese"); // Chứng chỉ tự ký có cả thông tin người phát hành và người sử dụng giống nhau
        Date startDate = new Date();
        Date endDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000); // 1 năm

        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(rsaPublicKey.getEncoded());

        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(issuerName, seri, startDate, endDate, subjectName, publicKeyInfo);
        JcaContentSignerBuilder signerBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

        ContentSigner contentSigner = signerBuilder.build(rsaPrivateKey);
        X509CertificateHolder selfSignedCert = certBuilder.build(contentSigner);

        return new JcaX509CertificateConverter().getCertificate(selfSignedCert);

    }   public  X509Certificate genCertificate(PrivateKey privateKey,PublicKey publicKey, String issuerNameString,BigInteger seri ) throws NoSuchAlgorithmException, OperatorCreationException, CertificateException {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;

        X500Name issuerName = new X500Name("CN=" + issuerNameString);
        X500Name subjectName = new X500Name("CN=Hoang Hai, O=GreenTea Group , OU=Students , L=Thu Duc, ST=HCM, C=vietnamese");
        Date startDate = new Date();
        Date endDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000); // 1 năm
//        BigInteger serial = new BigInteger(64, new SecureRandom());

        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(rsaPublicKey.getEncoded());

        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(issuerName, seri, startDate, endDate, subjectName, publicKeyInfo);
        JcaContentSignerBuilder signerBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

        ContentSigner contentSigner = signerBuilder.build(rsaPrivateKey);
        X509CertificateHolder selfSignedCert = certBuilder.build(contentSigner);

        return new JcaX509CertificateConverter().getCertificate(selfSignedCert);

    }

    public  byte[] genKeyStore(PrivateKey privateKey, X509Certificate certificate, char[] password) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
            keyStore.load(null, null);
            keyStore.setKeyEntry("privateKey", privateKey, password, new X509Certificate[]{certificate});
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            keyStore.store(outputStream, password);
            return outputStream.toByteArray();
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException |
                 NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, OperatorCreationException, CertificateException, IOException, KeyStoreException, NoSuchProviderException, UnrecoverableKeyException {
//        KeyGen gen = KeyGen.getInstance();
//        KeyPair keyPair =gen.getKeyPair(2048);
//        PrivateKey privateKey = keyPair.getPrivate();
//        PublicKey publicKey = keyPair.getPublic();
//        X509Certificate certificate = gen.genCertificate(keyPair, "Nguyen Hoang Hai", new BigInteger(64, new SecureRandom()));
//        byte[] keyStore = gen.genKeyStore(privateKey, certificate, "123456".toCharArray());
//        FileOutputStream fileOutputStream = new FileOutputStream("src/keystore.p12");
//        fileOutputStream.write(keyStore);
//        fileOutputStream.close();
//        fileOutputStream = new FileOutputStream("src/publicKey.key");
//        fileOutputStream.write(publicKey.getEncoded());
//        fileOutputStream.close();
//        fileOutputStream = new FileOutputStream("src/privateKey.key");
//        fileOutputStream.write(privateKey.getEncoded());
//        fileOutputStream.close();
//        fileOutputStream = new FileOutputStream("src/Certificate.cer");
//        fileOutputStream.write(certificate.getEncoded());
//        fileOutputStream.close();
//        System.out.println("Done");

        RSAPublicKey publicKey = (RSAPublicKey) KeyGen.getInstance().getPublicKeyformBytes(ReadKeyFormFile.readKeyFromFile("src/publicKey.key"));
        System.out.println(publicKey);
    }


}
