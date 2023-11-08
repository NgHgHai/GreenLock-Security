package vn.edu.atbmmodel.tool;

import javax.swing.*;

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

    public static void main(String[] args) {
        System.out.println(chooseFile("Choose file input"));
    }
}
