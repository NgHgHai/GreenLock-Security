/*
 * Created by JFormDesigner on Fri Nov 10 14:35:32 ICT 2023
 */

package vn.edu.atbm_gui;

import org.jdesktop.swingx.VerticalLayout;
import vn.edu.atbmmodel.key.KeyGen;
import vn.edu.atbmmodel.tool.ChooseFile;
import vn.edu.atbmmodel.tool.ReadKeyFormFile;

import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.File;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Random;

/**
 * @author hoang hai
 */
public class GenKeyGUI extends JPanel {
    KeyGen keyGen = KeyGen.getInstance();

    public GenKeyGUI() {
        initComponents();
    }

    private void btnSymMode(ActionEvent e) {
        pnSymmetric.setVisible(true);
        pnPublicKey.setVisible(false);
        pnCert.setVisible(false);
        pnKeyStore.setVisible(false);

    }

    private void btnPubMode(ActionEvent e) {
        pnSymmetric.setVisible(false);
        pnPublicKey.setVisible(true);
        pnCert.setVisible(false);
        pnKeyStore.setVisible(false);
    }

    private void btnCertMode(ActionEvent e) {
        pnSymmetric.setVisible(false);
        pnPublicKey.setVisible(false);
        pnCert.setVisible(true);
        pnKeyStore.setVisible(false);
    }

    private void btnKeyStoreMode(ActionEvent e) {
        pnSymmetric.setVisible(false);
        pnPublicKey.setVisible(false);
        pnCert.setVisible(false);
        pnKeyStore.setVisible(true);
    }

    private void btnSymGen(ActionEvent e) {
        if (jCBSymAlgo.getSelectedItem().toString().equals("Hill")) {
            int[] size = new int[4];
            try {
                Random random = new Random();
                size[0] = random.nextInt(256);
                size[1] = random.nextInt(256);
                size[2] = random.nextInt(256);
                size[3] = random.nextInt(256);
            } catch (Exception ex) {
                return;
            }
            jTFSymKey.setText(String.valueOf(size[0]) + " " + String.valueOf(size[1]) + " " + String.valueOf(size[2]) + " " + String.valueOf(size[3]));
            return;
        }
        if (jCBSymAlgo.getSelectedItem().toString().equals("Vigener")) {
            int[] size = new int[4];
            try {
                Random random = new Random();
                size[0] = random.nextInt(26);
                size[1] = random.nextInt(26);
                size[2] = random.nextInt(26);
                size[3] = random.nextInt(26);
            } catch (Exception ex) {
                return;
            }
            jTFSymKey.setText(size[0] + " " + size[1] + " " + size[2] + " " + size[3]);
            return;
        }
        try {
            int size = Integer.parseInt(jCBSymSize.getSelectedItem().toString());
            SecretKey key = keyGen.getKeySymmetric(jCBSymAlgo.getSelectedItem().toString(), size);
            String keyBase64 = Base64.getEncoder().encodeToString(key.getEncoded());
            jTFSymKey.setText(keyBase64);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error : " + ex.getMessage());
        }
    }


    private void jCBSymAlgo(ActionEvent e) {
        if (jCBSymAlgo.getSelectedItem().toString().equals("AES")) {
            jCBSymSize.setModel(new DefaultComboBoxModel(new String[]{"128", "192", "256"}));
        } else if (jCBSymAlgo.getSelectedItem().toString().equals("DESede")) {
            jCBSymSize.setModel(new DefaultComboBoxModel(new String[]{"112", "168"}));
        } else if (jCBSymAlgo.getSelectedItem().toString().equals("Blowfish")) {
            jCBSymSize.setModel(new DefaultComboBoxModel(new String[]{"128", "192", "256"}));
        } else if (jCBSymAlgo.getSelectedItem().toString().equals("Camellia")) {
            jCBSymSize.setModel(new DefaultComboBoxModel(new String[]{"128", "192", "256"}));
        } else if (jCBSymAlgo.getSelectedItem().toString().equals("DES")) {
            jCBSymSize.setModel(new DefaultComboBoxModel(new String[]{"56"}));
        } else if (jCBSymAlgo.getSelectedItem().toString().equals("Twofish")) {
            jCBSymSize.setModel(new DefaultComboBoxModel(new String[]{"128", "192", "256"}));
        } else if (jCBSymAlgo.getSelectedItem().toString().equals("CAST5")) {
            jCBSymSize.setModel(new DefaultComboBoxModel(new String[]{"128", "192", "256"}));
        }
    }

    private void jSymCopy(ActionEvent e) {
        String textToCopy = jTFSymKey.getText();
        // Đưa nội dung vào Clipboard
        StringSelection stringSelection = new StringSelection(textToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private void jSymExport(ActionEvent e) {
        String path = ChooseFile.chooseFile("choose file to export");
        byte[] keyByte = Base64.getDecoder().decode(jTFSymKey.getText());
        ChooseFile.writeFile(path + ".secKey", keyByte);
    }

    private void btnPubGen(ActionEvent e) {

        try {
            KeyPair keyPair = keyGen.getKeyPair(Integer.parseInt(jCBPubSize.getSelectedItem().toString()));
            String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            jTFPubPublicKey.setText(publicKeyBase64);
            jTFPubPrivateKey.setText(privateKeyBase64);
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            JOptionPane.showMessageDialog(this, "Error : " + ex.getMessage());
        }

    }

    private void jPubPublicKeyCopy(ActionEvent e) {
        String textToCopy = jTFPubPublicKey.getText();
        // Đưa nội dung vào Clipboard
        StringSelection stringSelection = new StringSelection(textToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private void jPubPrivateKeyCopy(ActionEvent e) {
        String textToCopy = jTFPubPrivateKey.getText();
        // Đưa nội dung vào Clipboard
        StringSelection stringSelection = new StringSelection(textToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private void jPubPublicKeyExport(ActionEvent e) {
        String path = ChooseFile.chooseFile("choose file to export");
        byte[] keyByte = Base64.getDecoder().decode(jTFPubPublicKey.getText());
        ChooseFile.writeFile(path + ".pubKey", keyByte);
    }

    private void jPubPrivateKeyExport(ActionEvent e) {
        String path = ChooseFile.chooseFile("choose file to export");
        byte[] keyByte = Base64.getDecoder().decode(jTFPubPrivateKey.getText());
        ChooseFile.writeFile(path + ".priKey", keyByte);
    }

    private void jChooseCertPublicKey(ActionEvent e) {
        String path = ChooseFile.chooseFile("choose file input");
        jTFCertPublicKey.setText(path);
    }

    private void btnCertGen(ActionEvent e) {
        try {
            KeyGen keyGen = KeyGen.getInstance();
            String name = jTFCertName.getText();
            byte[] keyByte;

            String path = jTFCertPublicKey.getText();
            File file = new File(path);

            if (file.exists()) {
                keyByte = ReadKeyFormFile.readKeyFromFile(path);
            } else {
                keyByte = Base64.getDecoder().decode(path);
            }

            PublicKey publicKey = keyGen.getPublicKeyformBytes(keyByte);
            X509Certificate x509Certificate = keyGen.genCertificate(publicKey, name, new BigInteger(64, new SecureRandom()));

//            String pathOut = ChooseFile.chooseFile("choose file to export");
//            ChooseFile.writeFile(pathOut + ".cer", x509Certificate.getEncoded());

            jTFCertResult.setText(Base64.getEncoder().encodeToString(x509Certificate.getEncoded()));
            JOptionPane.showMessageDialog(this, "success");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error : " + ex.getMessage());
        }
    }

    private void jCertCopy(ActionEvent e) {
        String textToCopy = jTFCertResult.getText();
        // Đưa nội dung vào Clipboard
        StringSelection stringSelection = new StringSelection(textToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private void jCertExport(ActionEvent e) {
        String pathOut = ChooseFile.chooseFile("choose file to export");
        byte[] certByte = Base64.getDecoder().decode(jTFCertResult.getText());
        ChooseFile.writeFile(pathOut + ".cer", certByte);
    }

    private void jTFCertPublicKeyCaretUpdate(CaretEvent e) {
        try {
            byte[] keyByte;
            String path = jTFCertPublicKey.getText();
            File file = new File(path);
            PublicKey publicKey;
            if (file.exists()) {
                keyByte = ReadKeyFormFile.readKeyFromFile(path);
                publicKey = KeyGen.getInstance().getPublicKeyformBytes(keyByte);
            } else {
                keyByte = Base64.getDecoder().decode(path);
                publicKey = KeyGen.getInstance().getPublicKeyformBytes(keyByte);
            }
            jLBCertStatus.setForeground(new Color(51, 153, 0));
            jLBCertStatus.setText(publicKey.getAlgorithm());
        } catch (Exception ex) {
            jLBCertStatus.setForeground(Color.RED);
            jLBCertStatus.setText("invalid public key");
        }
    }

    private void jTFKeyStorePrivateKeyCaretUpdate(CaretEvent e) {
        try {
            byte[] keyByte;
            String path = jTFKeyStorePrivateKey.getText();
            File file = new File(path);
            PrivateKey privateKey;
            if (file.exists()) {
                keyByte = ReadKeyFormFile.readKeyFromFile(path);
                privateKey = KeyGen.getInstance().getPrivateKeyformBytes(keyByte);
            } else {
                keyByte = Base64.getDecoder().decode(path);
                privateKey = KeyGen.getInstance().getPrivateKeyformBytes(keyByte);
            }
            jLBKeyStorePrivateKeyStatus.setForeground(new Color(51, 153, 0));
            jLBKeyStorePrivateKeyStatus.setText(privateKey.getAlgorithm());
        } catch (Exception ex) {
            jLBKeyStorePrivateKeyStatus.setForeground(Color.RED);
            jLBKeyStorePrivateKeyStatus.setText("invalid private key");
        }
    }

    private void jTFKeyStoreCertCaretUpdate(CaretEvent e) {
        try {
            byte[] keyByte;
            String path = jTFKeyStoreCert.getText();
            File file = new File(path);
            X509Certificate x509Certificate;
            if (file.exists()) {
                keyByte = ReadKeyFormFile.readKeyFromFile(path);
                x509Certificate = KeyGen.getInstance().getCertificateFormBytes(keyByte);
            } else {
                keyByte = Base64.getDecoder().decode(path);
                x509Certificate = KeyGen.getInstance().getCertificateFormBytes(keyByte);
            }
            jLBKeyStoreCertStatus.setForeground(new Color(51, 153, 0));
            jLBKeyStoreCertStatus.setText(x509Certificate.getSigAlgName());
        } catch (Exception ex) {
            jLBKeyStoreCertStatus.setForeground(Color.RED);
            jLBKeyStoreCertStatus.setText("invalid certificate");
        }
    }

    private void jChooseKeyStorePrivateKey(ActionEvent e) {
        String path = ChooseFile.chooseFile("choose file input");
        jTFKeyStorePrivateKey.setText(path);
    }

    private void jChooseKeyStoreCert(ActionEvent e) {
        String path = ChooseFile.chooseFile("choose file input");
        jTFKeyStoreCert.setText(path);
    }

    private void btnKeyStoreGen(ActionEvent e) {
        try {
            byte[] keyStore;
            byte[] priKeyByte;
            byte[] certByte;
            KeyGen keyGen = KeyGen.getInstance();
            String privateKeyPath = jTFKeyStorePrivateKey.getText();
            String certPath = jTFKeyStoreCert.getText();
            char[] passChar = jPassKeyStore.getPassword();
            //private-key
            File file = new File(privateKeyPath);
            if (file.exists()) {
                priKeyByte = ReadKeyFormFile.readKeyFromFile(privateKeyPath);
            } else {
                priKeyByte = Base64.getDecoder().decode(privateKeyPath);
            }
            //cert
            file = new File(certPath);
            if (file.isFile()) {
                certByte = ReadKeyFormFile.readKeyFromFile(certPath);
            } else {
                certByte = Base64.getDecoder().decode(certPath);
            }

            PrivateKey privateKey = keyGen.getPrivateKeyformBytes(priKeyByte);
            X509Certificate x509Certificate = keyGen.getCertificateFormBytes(certByte);
            keyStore = keyGen.genKeyStore(privateKey, x509Certificate, passChar);
            jTFKeyStoreResult.setText(Base64.getEncoder().encodeToString(keyStore));

            JOptionPane.showMessageDialog(this, "success");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error : " + ex.getMessage());
        }

    }

    private void jKeyStoreCopy(ActionEvent e) {
        String textToCopy = jTFKeyStoreResult.getText();
        // Đưa nội dung vào Clipboard
        StringSelection stringSelection = new StringSelection(textToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private void jKeyStoreExport(ActionEvent e) {
        String pathOut = ChooseFile.chooseFile("choose file to export");
        byte[] keyStoreByte = Base64.getDecoder().decode(jTFKeyStoreResult.getText());
        ChooseFile.writeFile(pathOut + ".p12", keyStoreByte);
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - hoanghai
        pnMain = new JPanel();
        panel1 = new JPanel();
        btnSymMode = new JButton();
        btnPubMode = new JButton();
        btnCertMode = new JButton();
        btnKeyStoreMode = new JButton();
        pnRoot2nd = new JPanel();
        pnSymmetric = new JPanel();
        panel6 = new JPanel();
        label1 = new JLabel();
        jCBSymAlgo = new JComboBox<>();
        label2 = new JLabel();
        jCBSymSize = new JComboBox<>();
        panel7 = new JPanel();
        btnSymGen = new JButton();
        panel8 = new JPanel();
        label7 = new JLabel();
        jTFSymKey = new JTextField();
        jSymCopy = new JButton();
        jSymExport = new JButton();
        pnPublicKey = new JPanel();
        panel9 = new JPanel();
        label4 = new JLabel();
        jCBPubAlgo = new JComboBox<>();
        label5 = new JLabel();
        jCBPubSize = new JComboBox<>();
        panel10 = new JPanel();
        btnPubGen = new JButton();
        panel11 = new JPanel();
        label8 = new JLabel();
        jTFPubPublicKey = new JTextField();
        jPubPublicKeyCopy = new JButton();
        jPubPublicKeyExport = new JButton();
        panel12 = new JPanel();
        label9 = new JLabel();
        jTFPubPrivateKey = new JTextField();
        jPubPrivateKeyCopy = new JButton();
        jPubPrivateKeyExport = new JButton();
        pnCert = new JPanel();
        panel13 = new JPanel();
        label10 = new JLabel();
        panel14 = new JPanel();
        label11 = new JLabel();
        jTFCertPublicKey = new JTextField();
        jChooseCertPublicKey = new JButton();
        jLBCertStatus = new JLabel();
        panel15 = new JPanel();
        label12 = new JLabel();
        jTFCertName = new JTextField();
        panel19 = new JPanel();
        btnCertGen = new JButton();
        panel22 = new JPanel();
        label16 = new JLabel();
        jTFCertResult = new JTextField();
        jCertCopy = new JButton();
        jCertExport = new JButton();
        pnKeyStore = new JPanel();
        panel16 = new JPanel();
        label13 = new JLabel();
        panel17 = new JPanel();
        label14 = new JLabel();
        jTFKeyStorePrivateKey = new JTextField();
        jChooseKeyStorePrivateKey = new JButton();
        jLBKeyStorePrivateKeyStatus = new JLabel();
        panel18 = new JPanel();
        label15 = new JLabel();
        jTFKeyStoreCert = new JTextField();
        jChooseKeyStoreCert = new JButton();
        jLBKeyStoreCertStatus = new JLabel();
        panel20 = new JPanel();
        label3 = new JLabel();
        jPassKeyStore = new JPasswordField();
        btnKeyStoreGen = new JButton();
        panel23 = new JPanel();
        label17 = new JLabel();
        jTFKeyStoreResult = new JTextField();
        jKeyStoreCopy = new JButton();
        jKeyStoreExport = new JButton();

        //======== this ========
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing
        . border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JFor\u006dDesi\u0067ner \u0045valu\u0061tion" , javax. swing .border . TitledBorder
        . CENTER ,javax . swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "Dia\u006cog", java .
        awt . Font. BOLD ,12 ) ,java . awt. Color .red ) , getBorder () ) )
        ;  addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java . beans. PropertyChangeEvent e
        ) { if( "bord\u0065r" .equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } )
        ;
        setLayout(new VerticalLayout());

        //======== pnMain ========
        {
            pnMain.setLayout(new BorderLayout());

            //======== panel1 ========
            {
                panel1.setLayout(new VerticalLayout());

                //---- btnSymMode ----
                btnSymMode.setText("Symmetric algo");
                btnSymMode.setPreferredSize(new Dimension(100, 100));
                btnSymMode.addActionListener(e -> btnSymMode(e));
                panel1.add(btnSymMode);

                //---- btnPubMode ----
                btnPubMode.setText("Publickey algo");
                btnPubMode.setPreferredSize(new Dimension(100, 100));
                btnPubMode.addActionListener(e -> btnPubMode(e));
                panel1.add(btnPubMode);

                //---- btnCertMode ----
                btnCertMode.setText("Certificate");
                btnCertMode.setPreferredSize(new Dimension(100, 100));
                btnCertMode.addActionListener(e -> btnCertMode(e));
                panel1.add(btnCertMode);

                //---- btnKeyStoreMode ----
                btnKeyStoreMode.setText("Key Store");
                btnKeyStoreMode.setPreferredSize(new Dimension(100, 100));
                btnKeyStoreMode.addActionListener(e -> btnKeyStoreMode(e));
                panel1.add(btnKeyStoreMode);
            }
            pnMain.add(panel1, BorderLayout.WEST);

            //======== pnRoot2nd ========
            {
                pnRoot2nd.setLayout(new VerticalLayout());

                //======== pnSymmetric ========
                {
                    pnSymmetric.setLayout(new VerticalLayout());

                    //======== panel6 ========
                    {
                        panel6.setLayout(new FlowLayout());

                        //---- label1 ----
                        label1.setText("Symmetric algo :");
                        panel6.add(label1);

                        //---- jCBSymAlgo ----
                        jCBSymAlgo.setPreferredSize(new Dimension(130, 26));
                        jCBSymAlgo.setModel(new DefaultComboBoxModel<>(new String[] {
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
                        jCBSymAlgo.addActionListener(e -> jCBSymAlgo(e));
                        panel6.add(jCBSymAlgo);

                        //---- label2 ----
                        label2.setText("size");
                        panel6.add(label2);

                        //---- jCBSymSize ----
                        jCBSymSize.setModel(new DefaultComboBoxModel<>(new String[] {
                            "128",
                            "192",
                            "256"
                        }));
                        panel6.add(jCBSymSize);
                    }
                    pnSymmetric.add(panel6);

                    //======== panel7 ========
                    {
                        panel7.setLayout(new FlowLayout());

                        //---- btnSymGen ----
                        btnSymGen.setText("Gen");
                        btnSymGen.addActionListener(e -> btnSymGen(e));
                        panel7.add(btnSymGen);
                    }
                    pnSymmetric.add(panel7);

                    //======== panel8 ========
                    {
                        panel8.setLayout(new FlowLayout(FlowLayout.LEFT));

                        //---- label7 ----
                        label7.setText("key :            ");
                        panel8.add(label7);

                        //---- jTFSymKey ----
                        jTFSymKey.setPreferredSize(new Dimension(400, 26));
                        panel8.add(jTFSymKey);

                        //---- jSymCopy ----
                        jSymCopy.setText("copy");
                        jSymCopy.addActionListener(e -> jSymCopy(e));
                        panel8.add(jSymCopy);

                        //---- jSymExport ----
                        jSymExport.setText("export");
                        jSymExport.addActionListener(e -> jSymExport(e));
                        panel8.add(jSymExport);
                    }
                    pnSymmetric.add(panel8);
                }
                pnRoot2nd.add(pnSymmetric);

                //======== pnPublicKey ========
                {
                    pnPublicKey.setLayout(new VerticalLayout());

                    //======== panel9 ========
                    {
                        panel9.setLayout(new FlowLayout());

                        //---- label4 ----
                        label4.setText("Publickey algo :");
                        panel9.add(label4);

                        //---- jCBPubAlgo ----
                        jCBPubAlgo.setPreferredSize(new Dimension(130, 26));
                        jCBPubAlgo.setModel(new DefaultComboBoxModel<>(new String[] {
                            "RSA"
                        }));
                        panel9.add(jCBPubAlgo);

                        //---- label5 ----
                        label5.setText("size");
                        panel9.add(label5);

                        //---- jCBPubSize ----
                        jCBPubSize.setModel(new DefaultComboBoxModel<>(new String[] {
                            "2048",
                            "1024",
                            "4096"
                        }));
                        panel9.add(jCBPubSize);
                    }
                    pnPublicKey.add(panel9);

                    //======== panel10 ========
                    {
                        panel10.setLayout(new FlowLayout());

                        //---- btnPubGen ----
                        btnPubGen.setText("Gen");
                        btnPubGen.addActionListener(e -> btnPubGen(e));
                        panel10.add(btnPubGen);
                    }
                    pnPublicKey.add(panel10);

                    //======== panel11 ========
                    {
                        panel11.setLayout(new FlowLayout(FlowLayout.LEFT));

                        //---- label8 ----
                        label8.setText("public key : ");
                        panel11.add(label8);

                        //---- jTFPubPublicKey ----
                        jTFPubPublicKey.setPreferredSize(new Dimension(400, 26));
                        panel11.add(jTFPubPublicKey);

                        //---- jPubPublicKeyCopy ----
                        jPubPublicKeyCopy.setText("copy");
                        jPubPublicKeyCopy.addActionListener(e -> jPubPublicKeyCopy(e));
                        panel11.add(jPubPublicKeyCopy);

                        //---- jPubPublicKeyExport ----
                        jPubPublicKeyExport.setText("export");
                        jPubPublicKeyExport.addActionListener(e -> jPubPublicKeyExport(e));
                        panel11.add(jPubPublicKeyExport);
                    }
                    pnPublicKey.add(panel11);

                    //======== panel12 ========
                    {
                        panel12.setLayout(new FlowLayout(FlowLayout.LEFT));

                        //---- label9 ----
                        label9.setText("private key :");
                        panel12.add(label9);

                        //---- jTFPubPrivateKey ----
                        jTFPubPrivateKey.setPreferredSize(new Dimension(400, 26));
                        panel12.add(jTFPubPrivateKey);

                        //---- jPubPrivateKeyCopy ----
                        jPubPrivateKeyCopy.setText("copy");
                        jPubPrivateKeyCopy.addActionListener(e -> jPubPrivateKeyCopy(e));
                        panel12.add(jPubPrivateKeyCopy);

                        //---- jPubPrivateKeyExport ----
                        jPubPrivateKeyExport.setText("export");
                        jPubPrivateKeyExport.addActionListener(e -> jPubPrivateKeyExport(e));
                        panel12.add(jPubPrivateKeyExport);
                    }
                    pnPublicKey.add(panel12);
                }
                pnRoot2nd.add(pnPublicKey);

                //======== pnCert ========
                {
                    pnCert.setLayout(new VerticalLayout());

                    //======== panel13 ========
                    {
                        panel13.setLayout(new FlowLayout());

                        //---- label10 ----
                        label10.setText("Certificate : (with GreenLockCA)");
                        label10.setToolTipText("signed with privatekey of GreenLockCA");
                        panel13.add(label10);
                    }
                    pnCert.add(panel13);

                    //======== panel14 ========
                    {
                        panel14.setLayout(new FlowLayout(FlowLayout.LEFT));

                        //---- label11 ----
                        label11.setText("public key : ");
                        panel14.add(label11);

                        //---- jTFCertPublicKey ----
                        jTFCertPublicKey.setPreferredSize(new Dimension(400, 26));
                        jTFCertPublicKey.addCaretListener(e -> jTFCertPublicKeyCaretUpdate(e));
                        panel14.add(jTFCertPublicKey);

                        //---- jChooseCertPublicKey ----
                        jChooseCertPublicKey.setText("choose");
                        jChooseCertPublicKey.addActionListener(e -> jChooseCertPublicKey(e));
                        panel14.add(jChooseCertPublicKey);

                        //---- jLBCertStatus ----
                        jLBCertStatus.setText("status");
                        jLBCertStatus.setPreferredSize(new Dimension(120, 16));
                        panel14.add(jLBCertStatus);
                    }
                    pnCert.add(panel14);

                    //======== panel15 ========
                    {
                        panel15.setLayout(new FlowLayout(FlowLayout.LEFT));

                        //---- label12 ----
                        label12.setText("name :         ");
                        panel15.add(label12);

                        //---- jTFCertName ----
                        jTFCertName.setPreferredSize(new Dimension(400, 26));
                        panel15.add(jTFCertName);
                    }
                    pnCert.add(panel15);

                    //======== panel19 ========
                    {
                        panel19.setLayout(new FlowLayout());

                        //---- btnCertGen ----
                        btnCertGen.setText("Gen");
                        btnCertGen.addActionListener(e -> btnCertGen(e));
                        panel19.add(btnCertGen);
                    }
                    pnCert.add(panel19);

                    //======== panel22 ========
                    {
                        panel22.setLayout(new FlowLayout());

                        //---- label16 ----
                        label16.setText("certificate :");
                        panel22.add(label16);

                        //---- jTFCertResult ----
                        jTFCertResult.setPreferredSize(new Dimension(400, 30));
                        panel22.add(jTFCertResult);

                        //---- jCertCopy ----
                        jCertCopy.setText("copy");
                        jCertCopy.addActionListener(e -> jCertCopy(e));
                        panel22.add(jCertCopy);

                        //---- jCertExport ----
                        jCertExport.setText("export");
                        jCertExport.addActionListener(e -> jCertExport(e));
                        panel22.add(jCertExport);
                    }
                    pnCert.add(panel22);
                }
                pnRoot2nd.add(pnCert);

                //======== pnKeyStore ========
                {
                    pnKeyStore.setLayout(new VerticalLayout());

                    //======== panel16 ========
                    {
                        panel16.setLayout(new FlowLayout());

                        //---- label13 ----
                        label13.setText("Key Store :");
                        panel16.add(label13);
                    }
                    pnKeyStore.add(panel16);

                    //======== panel17 ========
                    {
                        panel17.setLayout(new FlowLayout(FlowLayout.LEFT));

                        //---- label14 ----
                        label14.setText("private key : ");
                        panel17.add(label14);

                        //---- jTFKeyStorePrivateKey ----
                        jTFKeyStorePrivateKey.setPreferredSize(new Dimension(400, 26));
                        jTFKeyStorePrivateKey.addCaretListener(e -> jTFKeyStorePrivateKeyCaretUpdate(e));
                        panel17.add(jTFKeyStorePrivateKey);

                        //---- jChooseKeyStorePrivateKey ----
                        jChooseKeyStorePrivateKey.setText("choose");
                        jChooseKeyStorePrivateKey.addActionListener(e -> jChooseKeyStorePrivateKey(e));
                        panel17.add(jChooseKeyStorePrivateKey);

                        //---- jLBKeyStorePrivateKeyStatus ----
                        jLBKeyStorePrivateKeyStatus.setText("status");
                        jLBKeyStorePrivateKeyStatus.setPreferredSize(new Dimension(120, 16));
                        panel17.add(jLBKeyStorePrivateKeyStatus);
                    }
                    pnKeyStore.add(panel17);

                    //======== panel18 ========
                    {
                        panel18.setLayout(new FlowLayout(FlowLayout.LEFT));

                        //---- label15 ----
                        label15.setText("certificate :   ");
                        panel18.add(label15);

                        //---- jTFKeyStoreCert ----
                        jTFKeyStoreCert.setPreferredSize(new Dimension(400, 26));
                        jTFKeyStoreCert.addCaretListener(e -> jTFKeyStoreCertCaretUpdate(e));
                        panel18.add(jTFKeyStoreCert);

                        //---- jChooseKeyStoreCert ----
                        jChooseKeyStoreCert.setText("choose");
                        jChooseKeyStoreCert.addActionListener(e -> jChooseKeyStoreCert(e));
                        panel18.add(jChooseKeyStoreCert);

                        //---- jLBKeyStoreCertStatus ----
                        jLBKeyStoreCertStatus.setText("status");
                        jLBKeyStoreCertStatus.setPreferredSize(new Dimension(120, 16));
                        panel18.add(jLBKeyStoreCertStatus);
                    }
                    pnKeyStore.add(panel18);

                    //======== panel20 ========
                    {
                        panel20.setLayout(new FlowLayout());

                        //---- label3 ----
                        label3.setText("pass :");
                        panel20.add(label3);

                        //---- jPassKeyStore ----
                        jPassKeyStore.setPreferredSize(new Dimension(100, 30));
                        panel20.add(jPassKeyStore);

                        //---- btnKeyStoreGen ----
                        btnKeyStoreGen.setText("Gen");
                        btnKeyStoreGen.addActionListener(e -> btnKeyStoreGen(e));
                        panel20.add(btnKeyStoreGen);
                    }
                    pnKeyStore.add(panel20);

                    //======== panel23 ========
                    {
                        panel23.setLayout(new FlowLayout());

                        //---- label17 ----
                        label17.setText("key store");
                        panel23.add(label17);

                        //---- jTFKeyStoreResult ----
                        jTFKeyStoreResult.setPreferredSize(new Dimension(400, 30));
                        panel23.add(jTFKeyStoreResult);

                        //---- jKeyStoreCopy ----
                        jKeyStoreCopy.setText("copy");
                        jKeyStoreCopy.addActionListener(e -> jKeyStoreCopy(e));
                        panel23.add(jKeyStoreCopy);

                        //---- jKeyStoreExport ----
                        jKeyStoreExport.setText("export");
                        jKeyStoreExport.addActionListener(e -> jKeyStoreExport(e));
                        panel23.add(jKeyStoreExport);
                    }
                    pnKeyStore.add(panel23);
                }
                pnRoot2nd.add(pnKeyStore);
            }
            pnMain.add(pnRoot2nd, BorderLayout.CENTER);
        }
        add(pnMain);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - hoanghai
    JPanel pnMain;
    private JPanel panel1;
    private JButton btnSymMode;
    private JButton btnPubMode;
    private JButton btnCertMode;
    private JButton btnKeyStoreMode;
    private JPanel pnRoot2nd;
    private JPanel pnSymmetric;
    private JPanel panel6;
    private JLabel label1;
    private JComboBox<String> jCBSymAlgo;
    private JLabel label2;
    private JComboBox<String> jCBSymSize;
    private JPanel panel7;
    private JButton btnSymGen;
    private JPanel panel8;
    private JLabel label7;
    private JTextField jTFSymKey;
    private JButton jSymCopy;
    private JButton jSymExport;
    private JPanel pnPublicKey;
    private JPanel panel9;
    private JLabel label4;
    private JComboBox<String> jCBPubAlgo;
    private JLabel label5;
    private JComboBox<String> jCBPubSize;
    private JPanel panel10;
    private JButton btnPubGen;
    private JPanel panel11;
    private JLabel label8;
    private JTextField jTFPubPublicKey;
    private JButton jPubPublicKeyCopy;
    private JButton jPubPublicKeyExport;
    private JPanel panel12;
    private JLabel label9;
    private JTextField jTFPubPrivateKey;
    private JButton jPubPrivateKeyCopy;
    private JButton jPubPrivateKeyExport;
    private JPanel pnCert;
    private JPanel panel13;
    private JLabel label10;
    private JPanel panel14;
    private JLabel label11;
    private JTextField jTFCertPublicKey;
    private JButton jChooseCertPublicKey;
    private JLabel jLBCertStatus;
    private JPanel panel15;
    private JLabel label12;
    private JTextField jTFCertName;
    private JPanel panel19;
    private JButton btnCertGen;
    private JPanel panel22;
    private JLabel label16;
    private JTextField jTFCertResult;
    private JButton jCertCopy;
    private JButton jCertExport;
    private JPanel pnKeyStore;
    private JPanel panel16;
    private JLabel label13;
    private JPanel panel17;
    private JLabel label14;
    private JTextField jTFKeyStorePrivateKey;
    private JButton jChooseKeyStorePrivateKey;
    private JLabel jLBKeyStorePrivateKeyStatus;
    private JPanel panel18;
    private JLabel label15;
    private JTextField jTFKeyStoreCert;
    private JButton jChooseKeyStoreCert;
    private JLabel jLBKeyStoreCertStatus;
    private JPanel panel20;
    private JLabel label3;
    private JPasswordField jPassKeyStore;
    private JButton btnKeyStoreGen;
    private JPanel panel23;
    private JLabel label17;
    private JTextField jTFKeyStoreResult;
    private JButton jKeyStoreCopy;
    private JButton jKeyStoreExport;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        JFrame frame = new JFrame();
        frame.setContentPane(new GenKeyGUI().pnMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
