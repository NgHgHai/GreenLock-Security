package vn.edu.atbmmodel.key;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

public class KeyGen {
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleFipsProvider());
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public static PublicKey keyPubKey(byte[] data) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(data));
        return publicKey;
    }

    public static X509Certificate genCertificate() throws NoSuchAlgorithmException, OperatorCreationException, CertificateException {
        Security.addProvider(new BouncyCastleFipsProvider());
        KeyPair keyPair = getKeyPair();
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
        X509Certificate certificate  = new JcaX509CertificateConverter().getCertificate(selfSignedCert);
        return certificate;

    }

    public static void main(String[] args) throws NoSuchAlgorithmException, OperatorCreationException, CertificateException {
//        try {
//            KeyPair keyPair= getKeyPair();
//
//            byte[] data = keyPair.getPublic().getEncoded();
//
//            PublicKey publicKey = keyPubKey(data);
//
//            System.out.println(publicKey.equals(keyPair.getPublic()));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            throw new RuntimeException(e);
//        }
        System.out.println(genCertificate().getPublicKey()
        );
    }
}
