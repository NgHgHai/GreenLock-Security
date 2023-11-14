package vn.edu.atbmmodel.tool;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChooseFile {
    public static String chooseFile(String title) {
        String path = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            path = fileChooser.getSelectedFile().getAbsolutePath();
        }
        return path;
    }
    public static void writeFile(String path, byte[] keyByte) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            fos.write(keyByte);
            fos.flush();
            fos.close();
            System.out.println("Write file success");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(chooseFile("Choose file input"));
    }
}
