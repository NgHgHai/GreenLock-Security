/*
 * Created by JFormDesigner on Thu Oct 19 18:43:59 ICT 2023
 */

package vn.edu.atbm_gui;

import java.awt.event.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jdesktop.swingx.*;
import vn.edu.atbmmodel.digitalsignature.SignInData;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @author hoang hai
 */
public class DigitalSignature extends JPanel {
    boolean inputIsFile = false;
    LocalDateTime time = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    String now = time.format(formatter);

    public DigitalSignature() {
        initComponents();
    }

    private void jRadioKeyStore(ActionEvent e) {
        JTFCertificate.setEnabled(false);
        btnCertificateFile.setEnabled(false);
        JTFPrivateKey.setEnabled(false);
        btnPrivateKeyFile.setEnabled(false);
        JTFKeyStore.setEnabled(true);
        btnKeyStoreFile.setEnabled(true);
        jTAStatus.append(now + ": key store has certificate and private key, return a file Digital Certificate .(CA organization will authenticate)\n");
    }

    private void jRadioOnlyPrivateKey(ActionEvent e) {
        JTFCertificate.setEnabled(false);
        btnCertificateFile.setEnabled(false);
        JTFPrivateKey.setEnabled(true);
        btnPrivateKeyFile.setEnabled(true);
        JTFKeyStore.setEnabled(false);
        btnKeyStoreFile.setEnabled(false);
        jTAStatus.append(now + ": will return a String (base64 encode)- Digital Signature -(CA organization will NOT authenticate)\n");
    }

    private void jRadioWithCertificate(ActionEvent e) {
        JTFCertificate.setEnabled(true);
        btnCertificateFile.setEnabled(true);
        JTFPrivateKey.setEnabled(true);
        btnPrivateKeyFile.setEnabled(true);
        JTFKeyStore.setEnabled(false);
        btnKeyStoreFile.setEnabled(false);
        jTAStatus.append(now + ": return a file Digital Certificate .(CA organization will authenticate)\n");
    }

    private void jCheckUseTextInField(ActionEvent e) {
        if (jCheckUseTextInField.isSelected()) {
            jTAInput.setEnabled(true);
            btnChooseFileInput.setEnabled(false);
            inputIsFile = false;
            jTAStatus.append(now + ": use text in field\n");
            jTAInput.setText("");
        } else {
            jTAInput.setEnabled(false);
            btnChooseFileInput.setEnabled(true);
            inputIsFile = true;
            jTAStatus.append(now + ": sign on file\n");
        }
    }

    private void btnChooseFileInput(ActionEvent e) {
        inputIsFile = true;
        jTAInput.setEnabled(false);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose file input");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            jTAInput.setText(path);
        }
        jTAStatus.append(now + ": sign on file\n");
    }

    private void btnSign(ActionEvent e) {
        Security.addProvider(new BouncyCastleProvider());
        String algorithm = jCBAlgorithm.getSelectedItem().toString();
        if (inputIsFile){

        }
        String input = jTAInput.getText();

        try {
            if (jRadioKeyStore.isSelected()) {
                String keyStore = JTFKeyStore.getText();
                FileInputStream fis = new FileInputStream(new File(keyStore));
                KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
                ks.load(fis, jPassKeyStore.getPassword());
                String alias = ks.aliases().nextElement();
                PrivateKey privateKey = (PrivateKey) ks.getKey(alias, jPassKeyStore.getPassword());
                Certificate certificate = ks.getCertificate(alias);
                jTAStatus.append(now + ": Key Store : " + privateKey.toString() + "\n");
                jTAStatus.append(now + ": Certificate : " + certificate.toString() + "\n");
                System.out.println("aaaaaaaaa");
//                result = SignInData.createDigitalSignature(input, hashAlgorithm, keyEntry.getPrivateKey());
            } else if (jRadioOnlyPrivateKey.isSelected()) {

            } else if (jRadioWithCertificate.isSelected()) {

            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnKeyStoreFile(ActionEvent e) {
        // TODO add your code here
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose key store file");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            JTFKeyStore.setText(path);
        }
        jTAStatus.append(now + ": Key Store : " + JTFKeyStore.getText() + "\n");
    }

    private void btnPrivateKeyFile(ActionEvent e) {
        // TODO add your code here
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose private key file");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            JTFPrivateKey.setText(path);
        }
        jTAStatus.append(now + ": Private Key : " + JTFPrivateKey.getText() + "\n");
    }

    private void btnCertificateFile(ActionEvent e) {
        // TODO add your code here
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose certificate file");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            JTFCertificate.setText(path);
        }
        jTAStatus.append(now + ": Certificate : " + JTFCertificate.getText() + "\n");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - hoanghai
        pnMain = new JPanel();
        pnTop = new JPanel();
        label3 = new JLabel();
        jCBAlgorithm = new JComboBox<>();
        pnCenter = new JPanel();
        pnInput = new JPanel();
        scrollPane1 = new JScrollPane();
        jTAInput = new JTextArea();
        btnChooseFileInput = new JButton();
        jCheckUseTextInField = new JCheckBox();
        pnKey = new JPanel();
        panel1 = new JPanel();
        label8 = new JLabel();
        JTFKeyStore = new JTextField();
        btnKeyStoreFile = new JButton();
        label2 = new JLabel();
        jPassKeyStore = new JPasswordField();
        jRadioKeyStore = new JRadioButton();
        panel2 = new JPanel();
        label9 = new JLabel();
        JTFPrivateKey = new JTextField();
        btnPrivateKeyFile = new JButton();
        jRadioOnlyPrivateKey = new JRadioButton();
        panel3 = new JPanel();
        label10 = new JLabel();
        JTFCertificate = new JTextField();
        btnCertificateFile = new JButton();
        jRadioWithCertificate = new JRadioButton();
        pnExecute = new JPanel();
        btnSign = new JButton();
        label1 = new JLabel();
        pnResult = new JPanel();
        scrollPane2 = new JScrollPane();
        jTAResult = new JTextArea();
        scrollPane3 = new JScrollPane();
        jTAStatus = new JTextArea();

        //======== this ========
        setFont(new Font("Arial", Font.PLAIN, 12));
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
            pnMain.setFont(new Font("Arial", Font.PLAIN, 12));
            pnMain.setLayout(new VerticalLayout());

            //======== pnTop ========
            {
                pnTop.setFont(new Font("Arial", Font.PLAIN, 12));
                pnTop.setLayout(new FlowLayout());

                //---- label3 ----
                label3.setText("Algorithm");
                label3.setFont(new Font("Arial", Font.PLAIN, 12));
                pnTop.add(label3);

                //---- jCBAlgorithm ----
                jCBAlgorithm.setPreferredSize(new Dimension(86, 30));
                jCBAlgorithm.setModel(new DefaultComboBoxModel<>(new String[] {
                    "SHA1withRSA",
                    "SHA224withRSA",
                    "SHA256withRSA",
                    "SHA384withRSA",
                    "SHA512withRSA",
                    "SHA512(224)withRSA",
                    "SHA512(256)withRSA",
                    "SHA3-224withRSA",
                    "SHA3-256withRSA",
                    "SHA3-384withRSA",
                    "SHA3-512withRSA"
                }));
                jCBAlgorithm.setFont(new Font("Arial", Font.PLAIN, 12));
                pnTop.add(jCBAlgorithm);
            }
            pnMain.add(pnTop);

            //======== pnCenter ========
            {
                pnCenter.setFont(new Font("Arial", Font.PLAIN, 12));
                pnCenter.setLayout(new BorderLayout());

                //======== pnInput ========
                {
                    pnInput.setFont(new Font("Arial", Font.PLAIN, 12));
                    pnInput.setLayout(new BoxLayout(pnInput, BoxLayout.X_AXIS));

                    //======== scrollPane1 ========
                    {
                        scrollPane1.setFont(new Font("Arial", Font.PLAIN, 12));

                        //---- jTAInput ----
                        jTAInput.setRows(5);
                        jTAInput.setBorder(new TitledBorder(BorderFactory.createEmptyBorder(), "Input", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION));
                        jTAInput.setFont(new Font("Arial", Font.PLAIN, 12));
                        scrollPane1.setViewportView(jTAInput);
                    }
                    pnInput.add(scrollPane1);

                    //---- btnChooseFileInput ----
                    btnChooseFileInput.setText("choose file");
                    btnChooseFileInput.setFont(new Font("Arial", Font.PLAIN, 12));
                    btnChooseFileInput.addActionListener(e -> btnChooseFileInput(e));
                    pnInput.add(btnChooseFileInput);

                    //---- jCheckUseTextInField ----
                    jCheckUseTextInField.setText("use text in field");
                    jCheckUseTextInField.setFont(new Font("Arial", Font.PLAIN, 12));
                    jCheckUseTextInField.addActionListener(e -> jCheckUseTextInField(e));
                    pnInput.add(jCheckUseTextInField);
                }
                pnCenter.add(pnInput, BorderLayout.CENTER);

                //======== pnKey ========
                {
                    pnKey.setFont(new Font("Arial", Font.PLAIN, 12));
                    pnKey.setLayout(new VerticalLayout());

                    //======== panel1 ========
                    {
                        panel1.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel1.setLayout(new FlowLayout(FlowLayout.LEFT));

                        //---- label8 ----
                        label8.setText("Key Store          ");
                        label8.setMaximumSize(new Dimension(110, 17));
                        label8.setMinimumSize(new Dimension(110, 17));
                        label8.setPreferredSize(new Dimension(110, 17));
                        label8.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel1.add(label8);

                        //---- JTFKeyStore ----
                        JTFKeyStore.setPreferredSize(new Dimension(500, 30));
                        JTFKeyStore.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel1.add(JTFKeyStore);

                        //---- btnKeyStoreFile ----
                        btnKeyStoreFile.setText("choose key-store file");
                        btnKeyStoreFile.setPreferredSize(new Dimension(170, 30));
                        btnKeyStoreFile.setFont(new Font("Arial", Font.PLAIN, 12));
                        btnKeyStoreFile.addActionListener(e -> btnKeyStoreFile(e));
                        panel1.add(btnKeyStoreFile);

                        //---- label2 ----
                        label2.setText("pass");
                        label2.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel1.add(label2);

                        //---- jPassKeyStore ----
                        jPassKeyStore.setMinimumSize(new Dimension(100, 26));
                        jPassKeyStore.setPreferredSize(new Dimension(100, 26));
                        jPassKeyStore.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel1.add(jPassKeyStore);

                        //---- jRadioKeyStore ----
                        jRadioKeyStore.setText("use key store");
                        jRadioKeyStore.setToolTipText("key store has certificate and private key, return a file Digital Certificate .(CA organization will authenticate)");
                        jRadioKeyStore.setSelected(true);
                        jRadioKeyStore.setFont(new Font("Arial", Font.PLAIN, 12));
                        jRadioKeyStore.addActionListener(e -> jRadioKeyStore(e));
                        panel1.add(jRadioKeyStore);
                    }
                    pnKey.add(panel1);

                    //======== panel2 ========
                    {
                        panel2.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel2.setLayout(new FlowLayout(FlowLayout.LEFT));

                        //---- label9 ----
                        label9.setText("Key (private key)");
                        label9.setMaximumSize(new Dimension(110, 17));
                        label9.setMinimumSize(new Dimension(110, 17));
                        label9.setPreferredSize(new Dimension(110, 17));
                        label9.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel2.add(label9);

                        //---- JTFPrivateKey ----
                        JTFPrivateKey.setPreferredSize(new Dimension(500, 30));
                        JTFPrivateKey.setEnabled(false);
                        JTFPrivateKey.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel2.add(JTFPrivateKey);

                        //---- btnPrivateKeyFile ----
                        btnPrivateKeyFile.setText("choose key file");
                        btnPrivateKeyFile.setPreferredSize(new Dimension(170, 30));
                        btnPrivateKeyFile.setEnabled(false);
                        btnPrivateKeyFile.setFont(new Font("Arial", Font.PLAIN, 12));
                        btnPrivateKeyFile.addActionListener(e -> btnPrivateKeyFile(e));
                        panel2.add(btnPrivateKeyFile);

                        //---- jRadioOnlyPrivateKey ----
                        jRadioOnlyPrivateKey.setText("only private key (no certificate)");
                        jRadioOnlyPrivateKey.setToolTipText("will return a String (base64 encode)- Digital Signature -(CA organization will NOT authenticate)");
                        jRadioOnlyPrivateKey.setFont(new Font("Arial", Font.PLAIN, 12));
                        jRadioOnlyPrivateKey.addActionListener(e -> jRadioOnlyPrivateKey(e));
                        panel2.add(jRadioOnlyPrivateKey);
                    }
                    pnKey.add(panel2);

                    //======== panel3 ========
                    {
                        panel3.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel3.setLayout(new FlowLayout(FlowLayout.LEFT));

                        //---- label10 ----
                        label10.setText("Certificate");
                        label10.setMaximumSize(new Dimension(110, 17));
                        label10.setMinimumSize(new Dimension(110, 17));
                        label10.setPreferredSize(new Dimension(110, 17));
                        label10.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel3.add(label10);

                        //---- JTFCertificate ----
                        JTFCertificate.setPreferredSize(new Dimension(500, 30));
                        JTFCertificate.setEnabled(false);
                        JTFCertificate.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel3.add(JTFCertificate);

                        //---- btnCertificateFile ----
                        btnCertificateFile.setText("choose certificate file");
                        btnCertificateFile.setEnabled(false);
                        btnCertificateFile.setPreferredSize(new Dimension(170, 26));
                        btnCertificateFile.setFont(new Font("Arial", Font.PLAIN, 12));
                        btnCertificateFile.addActionListener(e -> btnCertificateFile(e));
                        panel3.add(btnCertificateFile);

                        //---- jRadioWithCertificate ----
                        jRadioWithCertificate.setText("private key with certificate");
                        jRadioWithCertificate.setToolTipText(" return a file Digital Certificate .(CA organization will authenticate)");
                        jRadioWithCertificate.setFont(new Font("Arial", Font.PLAIN, 12));
                        jRadioWithCertificate.addActionListener(e -> jRadioWithCertificate(e));
                        panel3.add(jRadioWithCertificate);
                    }
                    pnKey.add(panel3);
                }
                pnCenter.add(pnKey, BorderLayout.NORTH);

                //======== pnExecute ========
                {
                    pnExecute.setFont(new Font("Arial", Font.PLAIN, 12));
                    pnExecute.setLayout(new FlowLayout());

                    //---- btnSign ----
                    btnSign.setText("sign");
                    btnSign.setFont(new Font("Arial", Font.PLAIN, 12));
                    btnSign.addActionListener(e -> btnSign(e));
                    pnExecute.add(btnSign);

                    //---- label1 ----
                    label1.setText("p/s: this tool signed with detached-signature, it mean signed data dose NOT containt original data");
                    label1.setFont(new Font("Arial", Font.PLAIN, 12));
                    pnExecute.add(label1);
                }
                pnCenter.add(pnExecute, BorderLayout.SOUTH);
            }
            pnMain.add(pnCenter);

            //======== pnResult ========
            {
                pnResult.setFont(new Font("Arial", Font.PLAIN, 12));
                pnResult.setLayout(new VerticalLayout());

                //======== scrollPane2 ========
                {
                    scrollPane2.setFont(new Font("Arial", Font.PLAIN, 12));

                    //---- jTAResult ----
                    jTAResult.setRows(5);
                    jTAResult.setBorder(new TitledBorder(BorderFactory.createEmptyBorder(), "Result"));
                    jTAResult.setFont(new Font("Arial", Font.PLAIN, 12));
                    scrollPane2.setViewportView(jTAResult);
                }
                pnResult.add(scrollPane2);
            }
            pnMain.add(pnResult);

            //======== scrollPane3 ========
            {
                scrollPane3.setFont(new Font("Arial", Font.PLAIN, 12));

                //---- jTAStatus ----
                jTAStatus.setBorder(new TitledBorder("status"));
                jTAStatus.setRows(20);
                jTAStatus.setFont(new Font("Arial", Font.PLAIN, 12));
                scrollPane3.setViewportView(jTAStatus);
            }
            pnMain.add(scrollPane3);
        }
        add(pnMain);

        //---- buttonGroup1 ----
        var buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(jRadioKeyStore);
        buttonGroup1.add(jRadioOnlyPrivateKey);
        buttonGroup1.add(jRadioWithCertificate);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - hoanghai
    private JPanel pnMain;
    private JPanel pnTop;
    private JLabel label3;
    private JComboBox<String> jCBAlgorithm;
    private JPanel pnCenter;
    private JPanel pnInput;
    private JScrollPane scrollPane1;
    private JTextArea jTAInput;
    private JButton btnChooseFileInput;
    private JCheckBox jCheckUseTextInField;
    private JPanel pnKey;
    private JPanel panel1;
    private JLabel label8;
    private JTextField JTFKeyStore;
    private JButton btnKeyStoreFile;
    private JLabel label2;
    private JPasswordField jPassKeyStore;
    private JRadioButton jRadioKeyStore;
    private JPanel panel2;
    private JLabel label9;
    private JTextField JTFPrivateKey;
    private JButton btnPrivateKeyFile;
    private JRadioButton jRadioOnlyPrivateKey;
    private JPanel panel3;
    private JLabel label10;
    private JTextField JTFCertificate;
    private JButton btnCertificateFile;
    private JRadioButton jRadioWithCertificate;
    private JPanel pnExecute;
    private JButton btnSign;
    private JLabel label1;
    private JPanel pnResult;
    private JScrollPane scrollPane2;
    private JTextArea jTAResult;
    private JScrollPane scrollPane3;
    private JTextArea jTAStatus;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        JFrame frame = new JFrame("app");
        frame.setContentPane(new DigitalSignature().pnMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
