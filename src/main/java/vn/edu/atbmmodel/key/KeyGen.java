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

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

public class KeyGen {
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public static PublicKey keyPubKey(byte[] data) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(data));
        return publicKey;
    }
    public static SecretKey getKeySymmetric(String algorithm,int size) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(size);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static X509Certificate genCertificate(KeyPair key) throws NoSuchAlgorithmException, OperatorCreationException, CertificateException {
        Security.addProvider(new BouncyCastleProvider());
        KeyPair keyPair = key;
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

        X500Name issuerName = new X500Name("CN=hoang hai");
        X500Name subjectName = issuerName; // Chứng chỉ tự ký có cả thông tin người phát hành và người sử dụng giống nhau
        BigInteger serialNumber = new BigInteger(64, new java.security.SecureRandom());
        Date startDate = new Date();
        Date endDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000); // 1 năm
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(rsaPublicKey.getEncoded());
//        System.out.println(publicKeyInfo);
        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(issuerName, serialNumber, startDate, endDate, subjectName, publicKeyInfo);
        JcaContentSignerBuilder signerBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
        ContentSigner contentSigner = signerBuilder.build(rsaPrivateKey);
        X509CertificateHolder selfSignedCert = certBuilder.build(contentSigner);
        X509Certificate certificate = new JcaX509CertificateConverter().getCertificate(selfSignedCert);
        return certificate;

    }

    public static void main(String[] args) throws NoSuchAlgorithmException, OperatorCreationException, CertificateException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Key key = keyGenerator.generateKey();
        try{
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showSaveDialog(null);
            FileOutputStream fos = new FileOutputStream(jFileChooser.getSelectedFile());
//            FileOutputStream fos = new FileOutputStream("key.AES");
            fos.write(key.getEncoded());
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
