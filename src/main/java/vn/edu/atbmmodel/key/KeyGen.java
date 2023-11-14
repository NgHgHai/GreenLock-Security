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
import java.util.Base64;
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
//        KeyPair keyPair = keyGen.getKeyPair(2048);
//        PublicKey publicKey = keyPair.getPublic();
//        PrivateKey privateKey = keyPair.getPrivate();
//        X509Certificate certificate = keyGen.genCertificate(privateKey, publicKey, "GreenTea", new BigInteger(64, new SecureRandom()));
//        byte[] keyStore = keyGen.genKeyStore(privateKey, certificate, "000000".toCharArray());
//        KeyStore keyStore1 = KeyStore.getInstance("PKCS12", "BC");
//        FileOutputStream fos = new FileOutputStream("src/greenlock_ca/GreenLockKeyStorePass000000.p12");
//        fos.write(keyStore);
//        fos.flush();
//        fos.close();
//        fos = new FileOutputStream("src/greenlock_ca/GreenLockPublicKey.key");
//        fos.write(publicKey.getEncoded());
//        fos.flush();
//        fos.close();
//        fos = new FileOutputStream("src/greenlock_ca/GreenLockPrivateKey.key");
//        fos.write(privateKey.getEncoded());
//        fos.flush();
//        fos.close();
//        fos = new FileOutputStream("src/greenlock_ca/GreenLockCertificate.crt");
//        fos.write(certificate.getEncoded());
//        fos.flush();
//        fos.close();
        PrivateKey privateKey = keyGen.getPrivateKeyformBytes(Base64.getDecoder().decode("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC9Q+8BDVQKYc4Mv7SizmKq4Ij/+RrQzKv0NOMvJznaED74GaKTaoAbhB3QfTIOkkBfhKyA4byXsAA38GeBXAOd9AaJCIlhstEd+X3lOWLNXdCu7kr+jFW4IQO59jD0dS6wLFfjiAfVpCCzs8qrBUV9uOGSYJhJo+/cEFjTsW7yQW8Jw+rzN7bSH9PzHSPhIK79vlz710i+BlDxusrPoh7mcgidD7DdDoeQZfGMaCZS7FA1MPOkMoRTYv4IR6dJ79jwYMOasDJ+JpAg2kdomdI6MUHRTe5KXFyvTsr7oBKFMXhoxK971QnUYtW4NJb8vhyFygaKtWdZxdrrZy5vs8qBAgMBAAECggEAMusVYFuVGgvxEtX6g3hx7BXnVbJPaBBATL1zx2pOsp9/SWEgL3fOClkTalVcQ7ZOEkSP4kaY5dhIXsRTHGbEZFjT70b4KuwgPWezWpmDPsv4Ci/6xhu8LgLXzU5yIjpqeqQbaw/cR7pVv/LlWfhblzTHnwlUvy8XJ0XQl/da6VqBSwFsOMqjRbQ+psPzU9VTOnCrOfZpftOfL3aQshaaeG8ZIBaNnaD0CfZW1UM1YvW4htjRDn5l3YMR+rBnWh8MHSdwezPIcPrk3wFdk5+Ive/S3K+kkzrjBV1r1tfSzWIxBT2YbS8yK07XuF6tAYFdOPJsoMWoK02n0+eZ6Msu3wKBgQDyEnCXuf2NYuSJLYnxYguSqfCiP107Kx5JLgs2tJU9OcJF/f47UsgQ17M8J6LrzbtdVrAEqzMwNJaXMb29HG5+0GdW1Ekw68cVXUQH9XtFxowqREyy0Ja35sN75U5tiH4h9oRwwJk3QcZzuAbVBvakbOyaamRk+xMWmSDwbe3RJwKBgQDIJ6/L2SHTiEjiZ9uL4aNt+Ua3vkopQk6eEZYfpI13EJgSJEvWxj1ZsOckLtZWbnd4TzEG6SZo5/7i0sITUXxpee+ombUl2EeCk7LBcVE7FcaMOqZ7Wx1fuaIub7LKyRkzTYtHe4j1l/YMISWOU4Lw8gXx8watfVvUZMBeP7oAFwKBgQCU+Rwpg+CyfkW+1jOqRXhxzfu1HeG2ZBbDWn60L/YYM8+HW4R15cAR/WzkfFAbZgUEsDCq7uEVrYdvjMED/NdTW9X87bANV0yxrTAM5dnzWvkvmdDJyuy2SGHNgZT+e52EMT3YVe70vpnu5TXvK9Xl4tLJROh77LgWlC5dSImuyQKBgGQafTCdiu5ppdIeEU6Xbg5NctHoyNxzvjAOKZ/vw5S95uCD6K4k/YADGscc6bPWkcfJC4MiTkR/axn0QH467dcu1e7j2RZxYeprhiZ7Tx4W2PM/Tg451qoOhI9m6+vBwenHqZX5sdjqxHLyjP7uw3lQAiss2bUhEjFapl88zgDHAoGBALGXGXZwkgO3jVIYVYoNCVj2DtMnDyltAv6YyIpdch0LEP7E1jErN8qga0InDyhfDoKfw7zeeRsb2an4pUCoS2NU5SHPlCr7FrFhnd3tqaLdkuf+AKmQ+Mv7uKLiphN0S8Vddkw1PxmdAqubHLtNxOxM6vvwPR/DLm7OnT9tZA95"));
        System.out.println(privateKey.getAlgorithm());

    }


}
