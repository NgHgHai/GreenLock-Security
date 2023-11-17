package vn.edu.atbmmodel.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class ReadKeyFormFile {
    public static byte[] readKeyFromFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        byte[] key = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);    //read file
        fis.read(key);    //store file data in key
        return key;
    }
    public static byte[] readKeyFromPath(String path) throws IOException {
        if (path == null) {
            return null;
        }
        File file = new File(path);
        if (file.isFile())
            return readKeyFromFile(path);
        else
            return Base64.getDecoder().decode(path);
    }
}
