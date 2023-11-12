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

import javax.crypto.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
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

    public KeyPair getKeyPair(int size) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(size);
        return keyPairGenerator.generateKeyPair();
    }

    public PublicKey getPublicKeyformBytes(byte[] data) {
        PublicKey publicKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
            publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(data));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public PrivateKey getPrivateKeyformBytes(byte[] key) {
        PrivateKey privateKey = null;
        try {
            privateKey = KeyFactory.getInstance("RSA", "BC").generatePrivate(new PKCS8EncodedKeySpec(key));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public SecretKey getKeySymmetric(String algorithm, int size) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, "BC");
        keyGenerator.init(size);
        return keyGenerator.generateKey();
    }

    public X509Certificate getCertificateFormBytes(byte[] data) {
        X509Certificate certificate = null;
        try {
            certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(data));
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return certificate;
    }


    public X509Certificate genCertificate(PrivateKey caPrivateKey, PublicKey publicKey, String issuerNameString, BigInteger seri) throws NoSuchAlgorithmException, OperatorCreationException, CertificateException {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
        RSAPrivateKey caRsaPrivateKey = (RSAPrivateKey) caPrivateKey;

        X500Name issuerName = new X500Name("CN=2013016@, O=GreenTea Group , OU=Students , L=Thu Duc, ST=HCM, C=vietnamese");
        X500Name subjectName = new X500Name("CN=" + issuerNameString);
        Date startDate = new Date();
        Date endDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000); // 1 năm
//        BigInteger serial = new BigInteger(64, new SecureRandom());

        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(rsaPublicKey.getEncoded());

        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(issuerName, seri, startDate, endDate, subjectName, publicKeyInfo);
        JcaContentSignerBuilder signerBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

        ContentSigner contentSigner = signerBuilder.build(caRsaPrivateKey);
        X509CertificateHolder selfSignedCert = certBuilder.build(contentSigner);

        return new JcaX509CertificateConverter().getCertificate(selfSignedCert);

    }

    public byte[] genKeyStore(PrivateKey privateKey, X509Certificate certificate, char[] password) {
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

    public X509Certificate genCertificate(PublicKey publicKey, String name, BigInteger seri) throws CertificateException, NoSuchAlgorithmException, OperatorCreationException {
        KeyGen keyGen = KeyGen.getInstance();
        PrivateKey privateKey;
        try {
            privateKey = keyGen.getPrivateKeyformBytes(ReadKeyFormFile.readKeyFromFile("src/greenlock_ca/GreenLockPrivateKey.key"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return genCertificate(privateKey, publicKey, name, seri);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, OperatorCreationException, CertificateException, IOException, KeyStoreException, NoSuchProviderException, UnrecoverableKeyException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyGen keyGen = KeyGen.getInstance();
        KeyPair keyPair = keyGen.getKeyPair(2048);
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        X509Certificate certificate = keyGen.genCertificate(privateKey, publicKey, "GreenTea", new BigInteger(64, new SecureRandom()));
        byte[] keyStore = keyGen.genKeyStore(privateKey, certificate, "000000".toCharArray());
        KeyStore keyStore1 = KeyStore.getInstance("PKCS12", "BC");
        FileOutputStream fos = new FileOutputStream("src/greenlock_ca/GreenLockKeyStorePass000000.p12");
        fos.write(keyStore);
        fos.flush();
        fos.close();
        fos = new FileOutputStream("src/greenlock_ca/GreenLockPublicKey.key");
        fos.write(publicKey.getEncoded());
        fos.flush();
        fos.close();
        fos = new FileOutputStream("src/greenlock_ca/GreenLockPrivateKey.key");
        fos.write(privateKey.getEncoded());
        fos.flush();
        fos.close();
        fos = new FileOutputStream("src/greenlock_ca/GreenLockCertificate.crt");
        fos.write(certificate.getEncoded());
        fos.flush();
        fos.close();


    }


}
