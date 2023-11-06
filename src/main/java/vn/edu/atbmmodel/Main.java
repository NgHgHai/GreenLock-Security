package vn.edu.atbmmodel;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.Security;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) {
        String st= "11 22 33 44 55 66 77 88 99 00 11 22 33 44 55 66    ";
        int[] key ;
        try {
            key = Arrays.stream(st.split(" ")).mapToInt(Integer::parseInt).toArray();
            System.out.println(Arrays.toString(key));
        } catch (Exception ex) {
            System.out.println("Key is not valid");
        }
    }


}