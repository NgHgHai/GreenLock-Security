/*
 * Created by JFormDesigner on Thu Oct 19 12:31:54 ICT 2023
 */

package vn.edu.atbm_gui;


import org.jdesktop.swingx.VerticalLayout;
import vn.edu.atbmmodel.symmetric.Symmetric;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Base64;


/**
 * @author hoang hai
 */
public class SymmetricKeyEncpt extends JPanel {
    public boolean keyIsFile = false;
    public boolean inputIsFile = false;
    File dataSource;
    File key;

    public SymmetricKeyEncpt() {
        initComponents();
    }

    private void btnKeyFile(ActionEvent e) {
        // choose key file
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            jTFKey.setText(fileChooser.getSelectedFile().getAbsolutePath());
            keyIsFile = true;
            jTAStatus.append("key field: " + fileChooser.getSelectedFile().getAbsolutePath() + "\n");
            key = fileChooser.getSelectedFile();
        }

    }

    private void btnResetKeyToPlainText(ActionEvent e) {
        keyIsFile = false;
        jTAStatus.append("key field: " + "reset to plain text" + "\n");
    }

    private void btnChooseFileInput(ActionEvent e) {
        // choose input file
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {

            jTAInput.setText(fileChooser.getSelectedFile().getAbsolutePath());
            inputIsFile = true;
            jTAStatus.append("input field: " + fileChooser.getSelectedFile().getAbsolutePath() + "\n");
            dataSource = fileChooser.getSelectedFile();
        }
    }

    private void btnResetInputToPlainText(ActionEvent e) {
        inputIsFile = false;
        jTAStatus.append("input field: " + "reset to plain text" + "\n");
    }


    private void btnEncrypt(ActionEvent e) {
        jTAStatus.append("encrypting...\n");
        Symmetric symmetric = Symmetric.getInstance();
        byte[] enc;
        byte[] key = null;
        String des = null;
        if (keyIsFile) {
            try {
                key = symmetric.readKeyFromFile(jTFKey.getText());
                jTAStatus.append("key: read from file\n");
            } catch (IOException ex) {
                jTAStatus.append("ERROR : "+ex.getMessage() + "\n");
            }
        } else {
            key = jTFKey.getText().getBytes();
            jTAStatus.append("key: read from text field\n");
        }
        try {
            symmetric.init(jCBAlgorithm.getSelectedItem().toString(), jCBMode.getSelectedItem().toString(), jCBStandard.getSelectedItem().toString(), symmetric.getIvLength(jCBAlgorithm.getSelectedItem().toString()));
            jTAStatus.append("init algorithm: " + jCBAlgorithm.getSelectedItem().toString() + "/" + jCBMode.getSelectedItem().toString() + "/" + jCBStandard.getSelectedItem().toString() + "\n");
            if (inputIsFile) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(jTAInput.getText()));
                fileChooser.setSelectedFile(new File(jTAInput.getText() + "-encrypted" + ".enc"));
                int returnValue = fileChooser.showSaveDialog(this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    des = fileChooser.getSelectedFile().getAbsolutePath();
                }
                boolean b = symmetric.encryptFile(jTAInput.getText(), key, des);
                if (b) {
                    jTAStatus.append("encrypt file success : " + des + "\n");
                } else {
                    jTAStatus.append("encrypt file fail\n");
                }
            } else {
                enc = symmetric.encrypt(jTAInput.getText().getBytes(), key);
                jTAResult.setText(Base64.getEncoder().encodeToString(enc));
                jTAStatus.append("encrypt success\n");
            }
        } catch (Exception ex) {
            jTAStatus.append("ERROR : "+ex.getMessage() + "\n");
        }
    }

    private void btnDecrypt(ActionEvent e) {
        jTAStatus.append("decrypting...\n");
        Symmetric symmetric = Symmetric.getInstance();
        byte[] dec;
        byte[] key = null;
        String des = null;
        if (keyIsFile) {
            try {
                key = symmetric.readKeyFromFile(jTFKey.getText());
                jTAStatus.append("key: read from file\n");
            } catch (IOException ex) {
                jTAStatus.append("ERROR : "+ex.getMessage() + "\n");
            }
        } else {
            key = jTFKey.getText().getBytes();
            jTAStatus.append("key: read from text field\n");
        }
        try {
            symmetric.init(jCBAlgorithm.getSelectedItem().toString(), jCBMode.getSelectedItem().toString(), jCBStandard.getSelectedItem().toString(), symmetric.getIvLength(jCBAlgorithm.getSelectedItem().toString()));
            jTAStatus.append("init algorithm: " + jCBAlgorithm.getSelectedItem().toString() + "/" + jCBMode.getSelectedItem().toString() + "/" + jCBStandard.getSelectedItem().toString() + "\n");
            if (inputIsFile) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(jTAInput.getText()));
//                fileChooser.setSelectedFile(new File(jTAInput.getText()+ "-encrypted" + ".enc"));
                int returnValue = fileChooser.showSaveDialog(this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    des = fileChooser.getSelectedFile().getAbsolutePath();
                }
                boolean b = symmetric.decryptFile(jTAInput.getText(), key, des);
                if (b) {
                    jTAStatus.append("decrypt file success : " + des + "\n");
                } else {
                    jTAStatus.append("decrypt file fail\n");
                }
            } else {
                dec = symmetric.decrypt(Base64.getDecoder().decode(jTAInput.getText()), key);
                jTAResult.setText(new String(dec));
                jTAStatus.append("decrypt success\n");
            }
        } catch (Exception ex) {
            jTAStatus.append("ERROR : "+ex.getMessage() + "\n");
        }
    }

    private void jCBAlgorithm(ActionEvent e) {
        // TODO add your code here
        String algorithm = jCBAlgorithm.getSelectedItem().toString();
        jTAStatus.append("algorithm: " + algorithm + "\n");
        if (algorithm.equals("Hill") || algorithm.equals("Vigener")) {
            jCBMode.setEnabled(false);
            jCBStandard.setEnabled(false);
        } else {
            jCBMode.setEnabled(true);
            jCBStandard.setEnabled(true);
        }
        if (algorithm.equals("AES")) {
            jTAStatus.append("key length: 128 - 256 - 512 bits (16 - 32 - 64 byte)\n");
            jTAStatus.append("iv : 16 bytes\n");
        }
        if (algorithm.equals("DESede")) {
            jTAStatus.append("key length: 128 - 192 bits (16 - 24 byte)\n");
            jTAStatus.append("iv : 8 bytes\n");
        }
        if (algorithm.equals("Blowfish")) {
            jTAStatus.append("key length: 32 --> 448 bits (4 --> 56 byte)\n");
            jTAStatus.append("iv : 8 bytes\n");
        }
        if (algorithm.equals("Camellia")) {
            jTAStatus.append("key length: 128 - 192 - 256 bits (16 - 24 - 32 byte)\n");
            jTAStatus.append("iv : 16 bytes\n");
        }
        if (algorithm.equals("DES")) {
            jTAStatus.append("key length: 56 bits (7 byte)\n");
            jTAStatus.append("iv : 8 bytes\n");
        }
        if (algorithm.equals("CAST5")) {
            jTAStatus.append("key length: 40 - 128 bits (5 - 16 byte)\n");
            jTAStatus.append("iv : 8 bytes\n");
        }
        if (algorithm.equals("Twofish")) {
            jTAStatus.append("key length: 128 - 192 - 256 bits (16 - 24 - 32 byte)\n");
            jTAStatus.append("iv : 16 bytes\n");
        }
        if (algorithm.equals("Hill")) {
            jTAStatus.append("key length: 2x2 matrix\n");
            jTAStatus.append("key là 4 con số từ 0 -> 256\n");
            jTAStatus.append("example: 16 33 55 99\n");
            jTAStatus.append("Có thể mã hóa bảng chữ cái tiếng việt cả hoa lẫn thường\n");
        }
        if (algorithm.equals("Vigener")) {
            jTAStatus.append("key là 1 mảng gồm nhưững con số từ 0 -> 26\n");
            jTAStatus.append("Chỉ mã hóa bảng chữ cái tiếng anh\n");
        }

    }

    public void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - hoanghai
        pnMain = new JPanel();
        pnTop = new JPanel();
        label3 = new JLabel();
        jCBAlgorithm = new JComboBox<>();
        label4 = new JLabel();
        jCBMode = new JComboBox<>();
        label5 = new JLabel();
        jCBStandard = new JComboBox<>();
        btnResetInputToPlainText = new JButton();
        pnCenter = new JPanel();
        pnInput = new JPanel();
        scrollPane1 = new JScrollPane();
        jTAInput = new JTextArea();
        btnChooseFileInput = new JButton();
        pnKey = new JPanel();
        label8 = new JLabel();
        jTFKey = new JTextField();
        btnKeyFile = new JButton();
        btnResetKeyToPlainText = new JButton();
        pnEn_De = new JPanel();
        btnEncrypt = new JButton();
        btnDecrypt = new JButton();
        pnResult = new JPanel();
        scrollPane2 = new JScrollPane();
        jTAResult = new JTextArea();
        scrollPane3 = new JScrollPane();
        jTAStatus = new JTextArea();

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(
        0,0,0,0), "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e",javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder
        .BOTTOM,new java.awt.Font("Dialo\u0067",java.awt.Font.BOLD,12),java.awt.Color.
        red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.
        beans.PropertyChangeEvent e){if("borde\u0072".equals(e.getPropertyName()))throw new RuntimeException();}});
        setLayout(new VerticalLayout());

        //======== pnMain ========
        {
            pnMain.setLayout(new BorderLayout());

            //======== pnTop ========
            {
                pnTop.setLayout(new FlowLayout());

                //---- label3 ----
                label3.setText("Algorithm");
                pnTop.add(label3);

                //---- jCBAlgorithm ----
                jCBAlgorithm.setPreferredSize(new Dimension(86, 30));
                jCBAlgorithm.setModel(new DefaultComboBoxModel<>(new String[] {
                    "AES",
                    "DESede",
                    "Blowfish",
                    "Camellia",
                    "DES",
                    "Twofish",
                    "CAST5",
                    "Hill",
                    "Vigener"
                }));
                jCBAlgorithm.addActionListener(e -> jCBAlgorithm(e));
                pnTop.add(jCBAlgorithm);

                //---- label4 ----
                label4.setText("Mode");
                pnTop.add(label4);

                //---- jCBMode ----
                jCBMode.setPreferredSize(new Dimension(86, 26));
                jCBMode.setModel(new DefaultComboBoxModel<>(new String[] {
                    "CBC",
                    "OFB",
                    "CTR",
                    "CFB",
                    "CFB64"
                }));
                pnTop.add(jCBMode);

                //---- label5 ----
                label5.setText("padding");
                pnTop.add(label5);

                //---- jCBStandard ----
                jCBStandard.setPreferredSize(new Dimension(105, 26));
                jCBStandard.setModel(new DefaultComboBoxModel<>(new String[] {
                    "NoPadding",
                    "PKCS7Padding"
                }));
                pnTop.add(jCBStandard);

                //---- btnResetInputToPlainText ----
                btnResetInputToPlainText.setText("reset input to plain text");
                btnResetInputToPlainText.addActionListener(e -> btnResetInputToPlainText(e));
                pnTop.add(btnResetInputToPlainText);
            }
            pnMain.add(pnTop, BorderLayout.NORTH);

            //======== pnCenter ========
            {
                pnCenter.setLayout(new VerticalLayout());

                //======== pnInput ========
                {
                    pnInput.setLayout(new BoxLayout(pnInput, BoxLayout.X_AXIS));

                    //======== scrollPane1 ========
                    {

                        //---- jTAInput ----
                        jTAInput.setRows(5);
                        jTAInput.setBorder(new TitledBorder(BorderFactory.createEmptyBorder(), "Input", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION));
                        scrollPane1.setViewportView(jTAInput);
                    }
                    pnInput.add(scrollPane1);

                    //---- btnChooseFileInput ----
                    btnChooseFileInput.setText("choose file");
                    btnChooseFileInput.addActionListener(e -> btnChooseFileInput(e));
                    pnInput.add(btnChooseFileInput);
                }
                pnCenter.add(pnInput);

                //======== pnKey ========
                {
                    pnKey.setLayout(new FlowLayout());

                    //---- label8 ----
                    label8.setText("key");
                    pnKey.add(label8);

                    //---- jTFKey ----
                    jTFKey.setPreferredSize(new Dimension(500, 30));
                    pnKey.add(jTFKey);

                    //---- btnKeyFile ----
                    btnKeyFile.setText("choose key file");
                    btnKeyFile.addActionListener(e -> btnKeyFile(e));
                    pnKey.add(btnKeyFile);

                    //---- btnResetKeyToPlainText ----
                    btnResetKeyToPlainText.setText("reset key to plain text");
                    btnResetKeyToPlainText.addActionListener(e -> btnResetKeyToPlainText(e));
                    pnKey.add(btnResetKeyToPlainText);
                }
                pnCenter.add(pnKey);

                //======== pnEn_De ========
                {
                    pnEn_De.setLayout(new FlowLayout());

                    //---- btnEncrypt ----
                    btnEncrypt.setText("encrypt");
                    btnEncrypt.addActionListener(e -> btnEncrypt(e));
                    pnEn_De.add(btnEncrypt);

                    //---- btnDecrypt ----
                    btnDecrypt.setText("decrypt");
                    btnDecrypt.addActionListener(e -> btnDecrypt(e));
                    pnEn_De.add(btnDecrypt);
                }
                pnCenter.add(pnEn_De);

                //======== pnResult ========
                {
                    pnResult.setLayout(new BorderLayout());

                    //======== scrollPane2 ========
                    {

                        //---- jTAResult ----
                        jTAResult.setRows(5);
                        jTAResult.setBorder(new TitledBorder(BorderFactory.createEmptyBorder(), "Result"));
                        jTAResult.setToolTipText("result encode as Base64");
                        scrollPane2.setViewportView(jTAResult);
                    }
                    pnResult.add(scrollPane2, BorderLayout.CENTER);
                }
                pnCenter.add(pnResult);
            }
            pnMain.add(pnCenter, BorderLayout.CENTER);

            //======== scrollPane3 ========
            {

                //---- jTAStatus ----
                jTAStatus.setBorder(new TitledBorder("status"));
                jTAStatus.setRows(8);
                scrollPane3.setViewportView(jTAStatus);
            }
            pnMain.add(scrollPane3, BorderLayout.SOUTH);
        }
        add(pnMain);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - hoanghai
    JPanel pnMain;
    private JPanel pnTop;
    private JLabel label3;
    private JComboBox<String> jCBAlgorithm;
    private JLabel label4;
    private JComboBox<String> jCBMode;
    private JLabel label5;
    private JComboBox<String> jCBStandard;
    private JButton btnResetInputToPlainText;
    private JPanel pnCenter;
    private JPanel pnInput;
    private JScrollPane scrollPane1;
    private JTextArea jTAInput;
    private JButton btnChooseFileInput;
    private JPanel pnKey;
    private JLabel label8;
    private JTextField jTFKey;
    private JButton btnKeyFile;
    private JButton btnResetKeyToPlainText;
    private JPanel pnEn_De;
    private JButton btnEncrypt;
    private JButton btnDecrypt;
    private JPanel pnResult;
    private JScrollPane scrollPane2;
    private JTextArea jTAResult;
    private JScrollPane scrollPane3;
    private JTextArea jTAStatus;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
