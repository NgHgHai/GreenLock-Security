package vn.edu.atbmmodel.hash;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    private String hashArigothorm ;
    MessageDigest md ;
    public Hash(String hashArigothorm) throws NoSuchAlgorithmException {
        this.hashArigothorm = hashArigothorm;
        md = MessageDigest.getInstance(hashArigothorm);
    }
    public String hash(String data){
        byte[] messageDigest = md.digest(data.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        return no.toString(16);
    }
    public String hashFile(String path) throws IOException {
        DigestInputStream dis = new DigestInputStream(new BufferedInputStream(new FileInputStream(path)), md);
        byte[] buffer = new byte[1024];
        int i;
        do{
            i = dis.read(buffer);
        }while (i!=-1);
        BigInteger no = new BigInteger(1, md.digest());
        return no.toString(16);
    }

//    public static void main(String[] args) {
//        try {
//            Hash hash = new Hash("SHA-256");
//            System.out.println(hash.hashFile("C:\\Users\\H\\Downloads\\openjdk-11.0.2_windows-x64_bin.zip"));
//        } catch (NoSuchAlgorithmException | IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
