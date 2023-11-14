/*
 * Created by JFormDesigner on Thu Oct 19 18:42:28 ICT 2023
 */

package vn.edu.atbm_gui;


import org.jdesktop.swingx.VerticalLayout;
import vn.edu.atbmmodel.key.KeyGen;
import vn.edu.atbmmodel.publicKey.RSA;
import vn.edu.atbmmodel.tool.ChooseFile;
import vn.edu.atbmmodel.tool.ReadKeyFormFile;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * @author hoang hai
 */
public class PublicKeyEncpt extends JPanel {

    KeyGen keyGen = KeyGen.getInstance();

    public PublicKeyEncpt() {
        initComponents();
    }


    private void btnKeyFile(ActionEvent e) {
        // choose key file
        String key = ChooseFile.chooseFile("choose key file");
        jTFKey.setText(key);
        jTAStatus.append("key field: " + key + "\n");
    }

    private void btnChooseFileInput(ActionEvent e) {
        // choose input file
        String input = ChooseFile.chooseFile("choose input file");
        jTAInput.setText(input);
        jTAStatus.append("input field: " + input + "\n");
    }

    private void btnEncrypt(ActionEvent e) {
        jTAStatus.append("encrypting...\n");
        try {
            RSA rsa = RSA.getInstance();
            byte[] enc;
            byte[] key = null;
            String des = null;

            String path = jTFKey.getText();
            File file = new File(path);
            //get key
            if (file.isFile()) {
                key = ReadKeyFormFile.readKeyFromFile(jTFKey.getText());
                jTAStatus.append("key: read from file\n");
            } else {
                key = Base64.getDecoder().decode(jTFKey.getText());
                jTAStatus.append("key: read from text field\n");
            }
            // init
            rsa.init(jCBAlgorithm.getSelectedItem().toString(), jCBMode.getSelectedItem().toString(), jCBPadding.getSelectedItem().toString());
            jTAStatus.append("algorithm: " + jCBAlgorithm.getSelectedItem().toString() + "mode: " + jCBMode.getSelectedItem().toString() + "padding: " + jCBPadding.getSelectedItem().toString() + "\n");
            file = new File(jTAInput.getText());
            if (file.isFile()) {
                des = ChooseFile.chooseFile("choose output file");
                boolean b = rsa.encryptFile(keyGen.getPublicKeyformBytes(key), jTAInput.getText(), des);
                if (b) {
                    jTAStatus.append("encrypt file success : " + des + "\n");
                } else {
                    jTAStatus.append("encrypt file fail\n");
                }
            } else {
                enc = rsa.encrypt(keyGen.getPublicKeyformBytes(key), jTAInput.getText().getBytes());
                jTAResult.setText(Base64.getEncoder().encodeToString(enc));
                jTAStatus.append("encrypt success\n");
            }
        } catch (Exception ex) {
            jTAStatus.append("ERROR : " + ex.getMessage() + "\n");
        }

    }

    private void btnDecrypt(ActionEvent e) {
        jTAStatus.append("decrypting...\n");
        try {
            RSA rsa = RSA.getInstance();
            byte[] dec;
            byte[] key = null;
            String des = null;
            //getkey
            File file = new File(jTFKey.getText());
            if (file.isFile()) {
                key = ReadKeyFormFile.readKeyFromFile(jTFKey.getText());
                jTAStatus.append("key: read from file\n");
            } else {
                key = Base64.getDecoder().decode(jTFKey.getText());
                jTAStatus.append("key: read from text field\n");
            }
            //init
            rsa.init(jCBAlgorithm.getSelectedItem().toString(), jCBMode.getSelectedItem().toString(), jCBPadding.getSelectedItem().toString());
            jTAStatus.append("algorithm: " + jCBAlgorithm.getSelectedItem().toString() + "mode: " + jCBMode.getSelectedItem().toString() + "padding: " + jCBPadding.getSelectedItem().toString() + "\n");
            file = new File(jTAInput.getText());
            if (file.isFile()) {
                des = ChooseFile.chooseFile("choose output file");
                boolean b = rsa.decryptFile(keyGen.getPrivateKeyformBytes(key), jTAInput.getText(), des);
                if (b) {
                    jTAStatus.append("decrypt file success : " + des + "\n");
                } else {
                    jTAStatus.append("decrypt file fail\n");
                }
            } else {
                dec = rsa.decrypt(keyGen.getPrivateKeyformBytes(key), Base64.getDecoder().decode(jTAInput.getText()));
                jTAResult.setText(new String(dec));
                jTAStatus.append("decrypt success\n");
            }
        } catch (Exception ex) {
            jTAStatus.append("ERROR : " + ex.getMessage() + "\n");
        }
    }

    private void jTFKeyFocusGained(FocusEvent e) {
        if (jTFKey.getText().equals("choose publicKey for encode, privateKey for decode")) {
            jTFKey.setText("");
            jTFKey.setForeground(Color.BLACK); // Đặt màu chữ khi nhập
        }
    }

    private void jTFKeyFocusLost(FocusEvent e) {
        if (jTFKey.getText().isEmpty()) {
            jTFKey.setForeground(Color.GRAY); // Đặt màu chữ mờ
            jTFKey.setText("choose publicKey for encode, privateKey for decode");
        }
    }

    private void jTFKeyCaretUpdate(CaretEvent e) {
        try {
            byte[] key = null;
            String path = jTFKey.getText();
            File file = new File(path);
            PublicKey publicKey = null;
            PrivateKey privateKey = null;

            //get key
            if (file.isFile()) {
                key = ReadKeyFormFile.readKeyFromFile(jTFKey.getText());
            } else {
                key = Base64.getDecoder().decode(jTFKey.getText());
            }
            publicKey = keyGen.getPublicKeyformBytes(key);
            privateKey = keyGen.getPrivateKeyformBytes(key);
            if (publicKey != null) {
                jLBStatus.setForeground(new Color(51,153,0));
                jLBStatus.setText("public key");
            } else if (privateKey != null) {
                jLBStatus.setForeground(new Color(51,153,0));
                jLBStatus.setText("private key");
            } else {
                jLBStatus.setForeground(Color.red);
                jLBStatus.setText("key invalid");
            }
        }catch (Exception ex) {
            jLBStatus.setForeground(Color.red);
            jLBStatus.setText("key invalid");
        }

    }



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - hoanghai
        pnMain = new JPanel();
        pnTop = new JPanel();
        label3 = new JLabel();
        jCBAlgorithm = new JComboBox<>();
        label4 = new JLabel();
        jCBMode = new JComboBox<>();
        label5 = new JLabel();
        jCBPadding = new JComboBox<>();
        pnCenter = new JPanel();
        pnInput = new JPanel();
        scrollPane1 = new JScrollPane();
        jTAInput = new JTextArea();
        btnChooseFileInput = new JButton();
        pnKey = new JPanel();
        label8 = new JLabel();
        jTFKey = new JTextField();
        btnKeyFile = new JButton();
        jLBStatus = new JLabel();
        pnExecute = new JPanel();
        btnEncrypt = new JButton();
        btnDecrypt = new JButton();
        pnResult = new JPanel();
        scrollPane2 = new JScrollPane();
        jTAResult = new JTextArea();
        scrollPane3 = new JScrollPane();
        jTAStatus = new JTextArea();

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(
        new javax.swing.border.EmptyBorder(0,0,0,0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn"
        ,javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder.BOTTOM
        ,new java.awt.Font("Dia\u006cog",java.awt.Font.BOLD,12)
        ,java.awt.Color.red), getBorder())); addPropertyChangeListener(
        new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e
        ){if("\u0062ord\u0065r".equals(e.getPropertyName()))throw new RuntimeException()
        ;}});
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
                    "RSA"
                }));
                pnTop.add(jCBAlgorithm);

                //---- label4 ----
                label4.setText("Mode");
                pnTop.add(label4);

                //---- jCBMode ----
                jCBMode.setPreferredSize(new Dimension(86, 26));
                jCBMode.setModel(new DefaultComboBoxModel<>(new String[] {
                    "ECB"
                }));
                pnTop.add(jCBMode);

                //---- label5 ----
                label5.setText("padding");
                pnTop.add(label5);

                //---- jCBPadding ----
                jCBPadding.setPreferredSize(new Dimension(86, 26));
                jCBPadding.setModel(new DefaultComboBoxModel<>(new String[] {
                    "NoPadding",
                    "PKCS1Padding",
                    "OAEPPadding"
                }));
                pnTop.add(jCBPadding);
            }
            pnMain.add(pnTop, BorderLayout.PAGE_START);

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
                        jTAInput.setPreferredSize(new Dimension(700, 113));
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
                    jTFKey.setToolTipText("choose publicKey for encode, privateKey for  decode");
                    jTFKey.setText("choose publicKey for encode, privateKey for decode");
                    jTFKey.setForeground(Color.gray);
                    jTFKey.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            jTFKeyFocusGained(e);
                        }
                        @Override
                        public void focusLost(FocusEvent e) {
                            jTFKeyFocusLost(e);
                        }
                    });
                    jTFKey.addCaretListener(e -> jTFKeyCaretUpdate(e));
                    pnKey.add(jTFKey);

                    //---- btnKeyFile ----
                    btnKeyFile.setText("choose key file");
                    btnKeyFile.addActionListener(e -> btnKeyFile(e));
                    pnKey.add(btnKeyFile);

                    //---- jLBStatus ----
                    jLBStatus.setText("status");
                    pnKey.add(jLBStatus);
                }
                pnCenter.add(pnKey);

                //======== pnExecute ========
                {
                    pnExecute.setLayout(new FlowLayout());

                    //---- btnEncrypt ----
                    btnEncrypt.setText("encrypt");
                    btnEncrypt.addActionListener(e -> btnEncrypt(e));
                    pnExecute.add(btnEncrypt);

                    //---- btnDecrypt ----
                    btnDecrypt.setText("decrypt");
                    btnDecrypt.addActionListener(e -> btnDecrypt(e));
                    pnExecute.add(btnDecrypt);
                }
                pnCenter.add(pnExecute);

                //======== pnResult ========
                {
                    pnResult.setLayout(new VerticalLayout());

                    //======== scrollPane2 ========
                    {

                        //---- jTAResult ----
                        jTAResult.setRows(5);
                        jTAResult.setBorder(new TitledBorder(BorderFactory.createEmptyBorder(), "Result"));
                        scrollPane2.setViewportView(jTAResult);
                    }
                    pnResult.add(scrollPane2);
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
            pnMain.add(scrollPane3, BorderLayout.PAGE_END);
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
    private JComboBox<String> jCBPadding;
    private JPanel pnCenter;
    private JPanel pnInput;
    private JScrollPane scrollPane1;
    private JTextArea jTAInput;
    private JButton btnChooseFileInput;
    private JPanel pnKey;
    private JLabel label8;
    private JTextField jTFKey;
    private JButton btnKeyFile;
    private JLabel jLBStatus;
    private JPanel pnExecute;
    private JButton btnEncrypt;
    private JButton btnDecrypt;
    private JPanel pnResult;
    private JScrollPane scrollPane2;
    private JTextArea jTAResult;
    private JScrollPane scrollPane3;
    private JTextArea jTAStatus;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
