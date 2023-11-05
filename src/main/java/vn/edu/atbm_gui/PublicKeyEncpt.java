/*
 * Created by JFormDesigner on Thu Oct 19 18:42:28 ICT 2023
 */

package vn.edu.atbm_gui;


import java.awt.event.*;

import org.jdesktop.swingx.VerticalLayout;
import vn.edu.atbmmodel.key.KeyGen;
import vn.edu.atbmmodel.publicKey.RSA;
import vn.edu.atbmmodel.symmetric.Symmetric;
import vn.edu.atbmmodel.tool.ReadKeyFormFile;

import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

/**
 * @author hoang hai
 */
public class PublicKeyEncpt extends JPanel {
    public boolean keyIsFile = false;
    public boolean inputIsFile = false;
    File dataSource;
    File key;

    public PublicKeyEncpt() {
        initComponents();
    }

    private void btnResetKeyToPlainText(ActionEvent e) {
        keyIsFile = false;
        jTAStatus.append("key field: " + "reset to plain text" + "\n");
    }

    private void btnResetInputToPlainText(ActionEvent e) {
        inputIsFile = false;
        jTAStatus.append("input field: " + "reset to plain text" + "\n");
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

    private void btnEncrypt(ActionEvent e) {
        jTAStatus.append("encrypting...\n");
        RSA rsa = RSA.getInstance();
        byte[] enc;
        byte[] key = null;
        String des = null;
        if (keyIsFile) {
            try {
                key = ReadKeyFormFile.readKeyFromFile(jTFKey.getText());
                jTAStatus.append("key: read from file\n");
            } catch (IOException ex) {
                jTAStatus.append("ERROR : " + ex.getMessage() + "\n");
            }
        } else {
            key = jTFKey.getText().getBytes();
            jTAStatus.append("key: read from text field\n");
        }
        try {
            rsa.init(jCBAlgorithm.getSelectedItem().toString(), jCBMode.getSelectedItem().toString(), jCBPadding.getSelectedItem().toString());
            jTAStatus.append("algorithm: " + jCBAlgorithm.getSelectedItem().toString() + "mode: " + jCBMode.getSelectedItem().toString() + "padding: " + jCBPadding.getSelectedItem().toString() + "\n");
            if (inputIsFile) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(jTAInput.getText()));
                fileChooser.setSelectedFile(new File(jTAInput.getText() + "-encrypted" + ".enc"));
                int returnValue = fileChooser.showSaveDialog(this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    des = fileChooser.getSelectedFile().getAbsolutePath();
                }
                boolean b = rsa.encryptFile(KeyGen.getPublicKeyformBytes(key), jTAInput.getText(), des);
                if (b) {
                    jTAStatus.append("encrypt file success : " + des + "\n");
                } else {
                    jTAStatus.append("encrypt file fail\n");
                }
            } else {
                enc = rsa.encrypt(KeyGen.getPublicKeyformBytes(key), jTAInput.getText().getBytes());
                jTAResult.setText(Base64.getEncoder().encodeToString(enc));
                jTAStatus.append("encrypt success\n");
            }
        } catch (Exception ex) {
            jTAStatus.append("ERROR : " + ex.getMessage() + "\n");
        }
    }

    private void btnDecrypt(ActionEvent e) {
        jTAStatus.append("decrypting...\n");
        RSA rsa = RSA.getInstance();
        byte[] dec;
        byte[] key = null;
        String des = null;
        if (keyIsFile) {
            try {
                key = ReadKeyFormFile.readKeyFromFile(jTFKey.getText());
                jTAStatus.append("key: read from file\n");
            } catch (IOException ex) {
                jTAStatus.append("ERROR : " + ex.getMessage() + "\n");
            }
        } else {
            key = jTFKey.getText().getBytes();
            jTAStatus.append("key: read from text field\n");
        }
        try {
            rsa.init(jCBAlgorithm.getSelectedItem().toString(), jCBMode.getSelectedItem().toString(), jCBPadding.getSelectedItem().toString());
            jTAStatus.append("algorithm: " + jCBAlgorithm.getSelectedItem().toString() + "mode: " + jCBMode.getSelectedItem().toString() + "padding: " + jCBPadding.getSelectedItem().toString() + "\n");
            if (inputIsFile) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(jTAInput.getText()));
                fileChooser.setSelectedFile(new File(jTAInput.getText() + "-decrypted" + ".dec"));
                int returnValue = fileChooser.showSaveDialog(this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    des = fileChooser.getSelectedFile().getAbsolutePath();
                }
                boolean b = rsa.decryptFile(KeyGen.getPrivateKeyformBytes(key), jTAInput.getText(), des);
                if (b) {
                    jTAStatus.append("decrypt file success : " + des + "\n");
                } else {
                    jTAStatus.append("decrypt file fail\n");
                }
            } else {
                dec = rsa.decrypt(KeyGen.getPrivateKeyformBytes(key), Base64.getDecoder().decode(jTAInput.getText()));
                jTAResult.setText(new String(dec));
                jTAStatus.append("decrypt success\n");
            }
        } catch (Exception ex) {
            jTAStatus.append("ERROR : " + ex.getMessage() + "\n");
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
        pnExecute = new JPanel();
        btnEncrypt = new JButton();
        btnDecrypt = new JButton();
        pnResult = new JPanel();
        scrollPane2 = new JScrollPane();
        jTAResult = new JTextArea();
        scrollPane3 = new JScrollPane();
        jTAStatus = new JTextArea();

        //======== this ========
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing.
        border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JFor\u006dDesi\u0067ner \u0045valu\u0061tion" , javax. swing .border . TitledBorder. CENTER
        ,javax . swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "Dia\u006cog", java .awt . Font
        . BOLD ,12 ) ,java . awt. Color .red ) , getBorder () ) );  addPropertyChangeListener(
        new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java . beans. PropertyChangeEvent e) { if( "bord\u0065r"
        .equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } );
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

                //---- btnResetInputToPlainText ----
                btnResetInputToPlainText.setText("reset input to plain text");
                btnResetInputToPlainText.addActionListener(e -> btnResetInputToPlainText(e));
                pnTop.add(btnResetInputToPlainText);
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
