/*
 * Created by JFormDesigner on Thu Oct 19 12:31:54 ICT 2023
 */

package vn.edu.atbm_gui;


import org.jdesktop.swingx.VerticalLayout;
import vn.edu.atbmmodel.symmetric.Hill;
import vn.edu.atbmmodel.symmetric.Symmetric;
import vn.edu.atbmmodel.symmetric.Vigener;
import vn.edu.atbmmodel.tool.ChooseFile;
import vn.edu.atbmmodel.tool.ReadKeyFormFile;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Pattern;


/**
 * @author hoang hai
 */
public class SymmetricKeyEncpt extends JPanel {
    String dataSource;
    String key;
    byte[] keyByte;

    public SymmetricKeyEncpt() {
        initComponents();
    }

    private void btnKeyFile(ActionEvent e) {
        // choose key file
        key = ChooseFile.chooseFile("Choose key file");
        jTFKey.setText(key);
        jTAStatus.append("key field: " + key + "\n");
    }

    private void btnChooseFileInput(ActionEvent e) {
        // choose input file
        dataSource = ChooseFile.chooseFile("Choose file input");
        jTAInput.setText(dataSource);
        jTAStatus.append("input field: " + dataSource + "\n");
    }

    private void btnEncrypt(ActionEvent e) {
        jTAStatus.append("encrypting...\n");
        try {
//            Hill
            if (jCBAlgorithm.getSelectedItem().equals("Hill")) {
                Hill hill = new Hill();
                String pattern = "^(0?\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-6]) (0?\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-6]) (0?\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-6]) (0?\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-6])$";
                boolean isMatch = Pattern.matches(pattern, jTFKey.getText());
                if (!isMatch) {
                    jTAStatus.append("ERROR : key is not match\n");
                    return;
                }
                int[][] key = hill.getKey(jTFKey.getText());
                jTAResult.setText(hill.encrypt(key, jTAInput.getText()));
                System.out.println("ma hoa thanh cong");
                return;
            }
//            Vigener
            if (jCBAlgorithm.getSelectedItem().equals("Vigener")) {
                int[] key;
                key = Arrays.stream(jTFKey.getText().split(" ")).mapToInt(Integer::parseInt).toArray();
                for (int i = 0; i < key.length; i++) {
                    if (key[i] < 0 || key[i] > 26) {
                        jTAStatus.append("ERROR : key is not match\n");
                        return;
                    }
                }
                Vigener vigener = new Vigener(key);
                jTAResult.setText(vigener.encrypt(jTAInput.getText()));
                return;
            }
            //Symmetric
            Symmetric symmetric = Symmetric.getInstance();
            byte[] enc;
            byte[] key = null;
            String des = null;
            File file = new File(jTFKey.getText());
            // read key
            System.out.println("readkey");
            if (file.isFile()) {
                key = ReadKeyFormFile.readKeyFromFile(jTFKey.getText());
                jTAStatus.append("key: read from file\n");
            } else {
                key = Base64.getDecoder().decode(jTFKey.getText());
                jTAStatus.append("key: read from text field\n");
            }
            // init algorithm
            symmetric.init(jCBAlgorithm.getSelectedItem().toString(), jCBMode.getSelectedItem().toString(), jCBStandard.getSelectedItem().toString(), symmetric.getIvLength(jCBAlgorithm.getSelectedItem().toString()));
            jTAStatus.append("init algorithm: " + jCBAlgorithm.getSelectedItem().toString() + "/" + jCBMode.getSelectedItem().toString() + "/" + jCBStandard.getSelectedItem().toString() + "\n");
            file = new File(jTAInput.getText());
            if (file.isFile()) {
                des = ChooseFile.chooseFile("Choose file output");
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
            jTAStatus.append("ERROR : " + ex.getMessage() + "\n");
        }
    }

    private void btnDecrypt(ActionEvent e) {
        try {
            jTAStatus.append("decrypting...\n");
            //hill
            if (jCBAlgorithm.getSelectedItem().equals("Hill")) {
                Hill hill = new Hill();
                String pattern = "^(0?\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-6]) (0?\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-6]) (0?\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-6]) (0?\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-6])$";
                boolean isMatch = Pattern.matches(pattern, jTFKey.getText());
                if (!isMatch) {
                    jTAStatus.append("ERROR : key is not match\n");
                    return;
                }
                int[][] key = hill.getKey(jTFKey.getText());
//            System.out.println(jTAInput.getText());
                jTAResult.setText(hill.decrypt(key, jTAInput.getText()));
                return;
            }
            //vigener
            if (jCBAlgorithm.getSelectedItem().equals("Vigener")) {
                int[] key;
                key = Arrays.stream(jTFKey.getText().split(" ")).mapToInt(Integer::parseInt).toArray();
                for (int i = 0; i < key.length; i++) {
                    if (key[i] < 0 || key[i] > 26) {
                        jTAStatus.append("ERROR : key is not match\n");
                        return;
                    }
                }
                Vigener vigener = new Vigener(key);
                jTAResult.setText(vigener.decrypt(jTAInput.getText()));
                return;
            }
            //symmetric
            Symmetric symmetric = Symmetric.getInstance();
            byte[] dec;
            byte[] key = null;
            String des = null;
            String path = jTFKey.getText();
            File file = new File(path);

            if (file.isFile()) {
                key = ReadKeyFormFile.readKeyFromFile(jTFKey.getText());
                jTAStatus.append("key: read from file\n");
            } else {
                key = Base64.getDecoder().decode(jTFKey.getText());
                jTAStatus.append("key: read from text field\n");

            }

            symmetric.init(jCBAlgorithm.getSelectedItem().toString(), jCBMode.getSelectedItem().toString(), jCBStandard.getSelectedItem().toString(), symmetric.getIvLength(jCBAlgorithm.getSelectedItem().toString()));
            jTAStatus.append("init algorithm: " + jCBAlgorithm.getSelectedItem().toString() + "/" + jCBMode.getSelectedItem().toString() + "/" + jCBStandard.getSelectedItem().toString() + "\n");
            file = new File(jTAInput.getText());
            if (file.isFile()) {
                des = ChooseFile.chooseFile("Choose file output");
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
            jTAStatus.append("ERROR : " + ex.getMessage() + "\n");
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
            jTAStatus.append("key là 1 mảng gồm những con số từ 0 -> 26\n");
            jTAStatus.append("Chỉ mã hóa bảng chữ cái tiếng anh\n");
        }

    }

    private void btnResetKeyToPlainText(ActionEvent e) {
        // TODO add your code here
    }

    private void jTFKeyCaretUpdate(CaretEvent e) {
        try {
            jLBStatus.setForeground(Color.GREEN);
            String keyString = jTFKey.getText();
            File file = new File(keyString);
            if (file.isFile()) {
                keyByte = ReadKeyFormFile.readKeyFromFile(keyString);
                SecretKey secretKey = new SecretKeySpec(keyByte, jCBAlgorithm.getSelectedItem().toString());
                jLBStatus.setText("length" + String.valueOf(secretKey.getEncoded().length * 8) + " bit");
            } else {
                keyByte = Base64.getDecoder().decode(keyString);
                SecretKey secretKey = new SecretKeySpec(keyByte, jCBAlgorithm.getSelectedItem().toString());
                jLBStatus.setText("length" + String.valueOf(secretKey.getEncoded().length * 8)+ " bit");
            }
        } catch (Exception ex) {
            jLBStatus.setForeground(Color.RED);
            jLBStatus.setText("error");
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
        pnEn_De = new JPanel();
        btnEncrypt = new JButton();
        btnDecrypt = new JButton();
        pnResult = new JPanel();
        scrollPane2 = new JScrollPane();
        jTAResult = new JTextArea();
        scrollPane3 = new JScrollPane();
        jTAStatus = new JTextArea();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder(
        0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder
        . BOTTOM, new java .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ), java. awt. Color.
        red) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .
        beans .PropertyChangeEvent e) {if ("\u0062order" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
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
                    "PKCS7Padding",
                    "NoPadding"
                }));
                pnTop.add(jCBStandard);
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
