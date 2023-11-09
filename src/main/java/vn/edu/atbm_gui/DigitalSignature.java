/*
 * Created by JFormDesigner on Thu Oct 19 18:43:59 ICT 2023
 */

package vn.edu.atbm_gui;

import com.itextpdf.signatures.PdfSigner;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jdesktop.swingx.VerticalLayout;
import vn.edu.atbmmodel.digitalsignature.SignInData;
import vn.edu.atbmmodel.digitalsignature.SignInPdf;
import vn.edu.atbmmodel.key.KeyGen;
import vn.edu.atbmmodel.tool.ChooseFile;
import vn.edu.atbmmodel.tool.ReadKeyFormFile;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.StringTokenizer;


/**
 * @author hoang hai
 */
public class DigitalSignature extends JPanel {
    boolean inputIsFile = false;
    LocalDateTime time = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    String now = time.format(formatter);
    KeyGen keyGen = KeyGen.getInstance();

    public DigitalSignature() {
        initComponents();
    }

    private void jRadioKeyStore(ActionEvent e) {
        jTFCertificate.setEnabled(false);
        btnCertificateFile.setEnabled(false);
        jTFPrivateKey.setEnabled(false);
        btnPrivateKeyFile.setEnabled(false);
        jTFKeyStore.setEnabled(true);
        btnKeyStoreFile.setEnabled(true);
        jTAStatus.append(now + ": key store has certificate and private key, return a file Digital Certificate .(CA organization will authenticate)\n");
    }

    private void jRadioOnlyPrivateKey(ActionEvent e) {
        jTFCertificate.setEnabled(false);
        btnCertificateFile.setEnabled(false);
        jTFPrivateKey.setEnabled(true);
        btnPrivateKeyFile.setEnabled(true);
        jTFKeyStore.setEnabled(false);
        btnKeyStoreFile.setEnabled(false);
        jTAStatus.append(now + ": will return a - Digital Signature -(CA organization will NOT authenticate)\n");
    }

    private void jRadioWithCertificate(ActionEvent e) {
        jTFCertificate.setEnabled(true);
        btnCertificateFile.setEnabled(true);
        jTFPrivateKey.setEnabled(true);
        btnPrivateKeyFile.setEnabled(true);
        jTFKeyStore.setEnabled(false);
        btnKeyStoreFile.setEnabled(false);
        jTAStatus.append(now + ": return a file Digital Certificate .(CA organization will authenticate)\n");
    }

    private void jCheckUseTextInField(ActionEvent e) {
        if (jCheckUseTextInField.isSelected()) {
            jTAInput.setEditable(true);
            inputIsFile = false;
            jTAStatus.append(now + ": use text in field\n");
            jTAInput.setText("");
        } else {
            jTAInput.setEditable(false);
            inputIsFile = true;
            jTAStatus.append(now + ": sign on file\n");
        }
    }

    private void btnChooseFileInput(ActionEvent e) {
        inputIsFile = true;
        jCheckUseTextInField.setSelected(false);
        jTAInput.setEditable(false);

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Tệp PDF", "pdf");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Choose file input");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            jTAInput.setText(path);
        }
        jTAStatus.append(now + ": sign on file\n");
    }

    private void btnSign(ActionEvent e) {
        Provider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        String algorithm = jCBAlgorithm.getSelectedItem().toString();


        try {
            if (jCheckPdf.isSelected()) {
                String pdfPath = jTAInput.getText();
                if (!pdfPath.endsWith("pdf")) {
                    JOptionPane.showMessageDialog(this, "choose a pdf file !!!");
                    return;
                }
                PrivateKey privateKey = null;
                X509Certificate certificate = null;
                if (jRadioKeyStore.isSelected()) {
                    String keyStore = jTFKeyStore.getText();

                    FileInputStream fis = new FileInputStream(new File(keyStore));
                    KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
                    ks.load(fis, jPassKeyStore.getPassword());
                    String alias = ks.aliases().nextElement();

                    privateKey = (PrivateKey) ks.getKey(alias, jPassKeyStore.getPassword());
                    certificate = (X509Certificate) ks.getCertificate(alias);
                    jTAStatus.append(now + ": certificate form keyStore : " + "\n");
                }
                if (jRadioWithCertificate.isSelected()) {
                    privateKey = keyGen.getPrivateKeyformBytes(ReadKeyFormFile.readKeyFromFile(jTFPrivateKey.getText()));
                    byte[] certificateByte = Files.readAllBytes(new File(jTFCertificate.getText()).toPath());
                    certificate = (X509Certificate) KeyGen.getInstance().getCertificateFormBytes(certificateByte);
                    jTAStatus.append(now + ": certificate form private key and certificate : " + "\n");
                }
                
                jTAStatus.append(now + ": private key : " + privateKey.getAlgorithm() + "\n");
                jTAStatus.append(now + ": Certificate : " + certificate.toString() + "\n");
                String des = ChooseFile.chooseFile("Choose file to save signature");
                Certificate[] chain ={certificate};
//                StringTokenizer st = new StringTokenizer(algorithm, "with");
//                String digestAlgorithm = st.nextToken();
                SignInPdf.sign(pdfPath,des,chain,privateKey, "SHA256",provider.getName(), PdfSigner.CryptoStandard.CMS,"sign by GreenLock","vietnamese");
                jTAStatus.append("=====================================================================================\n");
                jTAStatus.append("=====================================================================================\n");
                return;
            }
            if (jRadioKeyStore.isSelected()) {
                String keyStore = jTFKeyStore.getText();

                FileInputStream fis = new FileInputStream(new File(keyStore));
                KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
                ks.load(fis, jPassKeyStore.getPassword());
                String alias = ks.aliases().nextElement();

                PrivateKey privateKey = (PrivateKey) ks.getKey(alias, jPassKeyStore.getPassword());
                X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);

                jTAStatus.append(now + ": private key : " + privateKey.getAlgorithm() + "\n");
                jTAStatus.append(now + ": Certificate : " + certificate.toString() + "\n");
                jTAStatus.append(now + ": certificate form keyStore : " + "\n");
                String des = ChooseFile.chooseFile("Choose file to save signature");

                if (inputIsFile) {
                    SignInData.createDetachedSignatureWithCertFile(jTAInput.getText(), des, certificate, algorithm, privateKey);
                    jTAStatus.append(now + ": Signature form file \n");
                    jTAStatus.append(now + ": Signature : " + des + "\n");
                } else {
                    byte[] data = jTAInput.getText().getBytes();
                    byte[] signed = SignInData.createDetachedSignatureWithCert(data, certificate, algorithm, privateKey);
                    try {
                        FileOutputStream fos = new FileOutputStream(des);
                        fos.write(signed);
                    } catch (Exception ex) {

                    }

                    jTAStatus.append(now + ": " + Base64.getEncoder().encodeToString(signed) + "\n");
                    jTAStatus.append(now + ": Signature form text in field \n");
                    jTAStatus.append(now + ": Signature : " + des + "\n");
                }
            } else if (jRadioOnlyPrivateKey.isSelected()) {
                PrivateKey privateKey = keyGen.getPrivateKeyformBytes(ReadKeyFormFile.readKeyFromFile(jTFPrivateKey.getText()));
                jTAStatus.append(now + ": Private Key : " + privateKey.getAlgorithm() + "\n");
                jTAStatus.append(now + ": Certificate : " + "null" + "\n");
                String des = ChooseFile.chooseFile("Choose file to save signature");
                FileOutputStream fos = new FileOutputStream(des);
                jTAStatus.append(now + ": certificate form private key : " + "\n");
                if (inputIsFile) {
                    SignInData.createDigitalSignatureFile(jTAInput.getText(), des, algorithm, privateKey);
                    jTAStatus.append(now + ": Signature form file \n");
                    jTAStatus.append(now + ": Signature : " + des + "\n");
                } else {
                    byte[] data = jTAInput.getText().getBytes();
                    byte[] signed = SignInData.createDigitalSignature(data, algorithm, privateKey);
                    fos.write(signed);
                    jTAStatus.append(now + ": " + Base64.getEncoder().encodeToString(signed) + "\n");
                    jTAStatus.append(now + ": Signature form text in field \n");
                    jTAStatus.append(now + ": Signature : " + des + "\n");
                }

            } else if (jRadioWithCertificate.isSelected()) {
                PrivateKey privateKey = keyGen.getPrivateKeyformBytes(ReadKeyFormFile.readKeyFromFile(jTFPrivateKey.getText()));
                byte[] certificateByte = Files.readAllBytes(new File(jTFCertificate.getText()).toPath());
                X509Certificate certificate = (X509Certificate) KeyGen.getInstance().getCertificateFormBytes(certificateByte);
                jTAStatus.append(now + ": Private Key : " + privateKey.getAlgorithm() + "\n");
                jTAStatus.append(now + ": Certificate : " + certificate.toString() + "\n");
                jTAStatus.append(now + ": certificate form private key and certificate : " + "\n");
                String des = ChooseFile.chooseFile("Choose file to save signature");
                FileOutputStream fos = new FileOutputStream(des);
                if (inputIsFile) {
                    SignInData.createDetachedSignatureWithCertFile(jTAInput.getText(), des, certificate, algorithm, privateKey);
                    jTAStatus.append(now + ": Signature form file \n");
                    jTAStatus.append(now + ": Signature : " + des + "\n");
                } else {
                    byte[] data = jTAInput.getText().getBytes();
                    byte[] signed = SignInData.createDetachedSignatureWithCert(data, certificate, algorithm, privateKey);
                    fos.write(signed);
                    jTAStatus.append(now + ": " + Base64.getEncoder().encodeToString(signed) + "\n");
                    jTAStatus.append(now + ": Signature form text in field \n");
                    jTAStatus.append(now + ": Signature : " + des + "\n");
                }
            }


            jTAStatus.append("=====================================================================================\n");
            jTAStatus.append("=====================================================================================\n");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnKeyStoreFile(ActionEvent e) {
        jTFKeyStore.setText(ChooseFile.chooseFile("Choose key store file"));
        jTAStatus.append(now + ": Key Store : " + jTFKeyStore.getText() + "\n");
    }

    private void btnPrivateKeyFile(ActionEvent e) {
        jTFPrivateKey.setText(ChooseFile.chooseFile("Choose private key file"));
        jTAStatus.append(now + ": Private Key : " + jTFPrivateKey.getText() + "\n");
    }

    private void btnCertificateFile(ActionEvent e) {
        jTFCertificate.setText(ChooseFile.chooseFile("Choose certificate file"));
        jTAStatus.append(now + ": Certificate : " + jTFCertificate.getText() + "\n");
    }

    private void checkPdf(ActionEvent e) {
        if (jCheckPdf.isSelected()) {
            jCheckUseTextInField.setEnabled(false);
            jRadioOnlyPrivateKey.setSelected(false);
            jRadioOnlyPrivateKey.setEnabled(false);
            jRadioOnlyPrivateKey.setVisible(true);
        } else {
            jRadioOnlyPrivateKey.setEnabled(true);
            jRadioOnlyPrivateKey.setVisible(true);
        }
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
        jTFKeyStore = new JTextField();
        btnKeyStoreFile = new JButton();
        label2 = new JLabel();
        jPassKeyStore = new JPasswordField();
        jRadioKeyStore = new JRadioButton();
        panel2 = new JPanel();
        label9 = new JLabel();
        jTFPrivateKey = new JTextField();
        btnPrivateKeyFile = new JButton();
        jRadioOnlyPrivateKey = new JRadioButton();
        panel3 = new JPanel();
        label10 = new JLabel();
        jTFCertificate = new JTextField();
        btnCertificateFile = new JButton();
        jRadioWithCertificate = new JRadioButton();
        pnExecute = new JPanel();
        jCheckPdf = new JCheckBox();
        btnSign = new JButton();
        label1 = new JLabel();
        scrollPane3 = new JScrollPane();
        jTAStatus = new JTextArea();

        //======== this ========
        setFont(new Font("Arial", Font.PLAIN, 12));
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border
        . EmptyBorder( 0, 0, 0, 0) , "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax. swing. border. TitledBorder. CENTER, javax
        . swing. border. TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,
        12 ), java. awt. Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans
        . PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("bord\u0065r" .equals (e .
        getPropertyName () )) throw new RuntimeException( ); }} );
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
                jCBAlgorithm.setPreferredSize(new Dimension(130, 30));
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
                        jTAInput.setEditable(false);
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

                        //---- jTFKeyStore ----
                        jTFKeyStore.setPreferredSize(new Dimension(500, 30));
                        jTFKeyStore.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel1.add(jTFKeyStore);

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

                        //---- jTFPrivateKey ----
                        jTFPrivateKey.setPreferredSize(new Dimension(500, 30));
                        jTFPrivateKey.setEnabled(false);
                        jTFPrivateKey.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel2.add(jTFPrivateKey);

                        //---- btnPrivateKeyFile ----
                        btnPrivateKeyFile.setText("choose key file");
                        btnPrivateKeyFile.setPreferredSize(new Dimension(170, 30));
                        btnPrivateKeyFile.setEnabled(false);
                        btnPrivateKeyFile.setFont(new Font("Arial", Font.PLAIN, 12));
                        btnPrivateKeyFile.addActionListener(e -> btnPrivateKeyFile(e));
                        panel2.add(btnPrivateKeyFile);

                        //---- jRadioOnlyPrivateKey ----
                        jRadioOnlyPrivateKey.setText("only private key (no certificate)");
                        jRadioOnlyPrivateKey.setToolTipText("will return a - Digital Signature -(CA organization will NOT authenticate)");
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

                        //---- jTFCertificate ----
                        jTFCertificate.setPreferredSize(new Dimension(500, 30));
                        jTFCertificate.setEnabled(false);
                        jTFCertificate.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel3.add(jTFCertificate);

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

                    //---- jCheckPdf ----
                    jCheckPdf.setText("sign in pdf");
                    jCheckPdf.addActionListener(e -> checkPdf(e));
                    pnExecute.add(jCheckPdf);

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
    JPanel pnMain;
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
    private JTextField jTFKeyStore;
    private JButton btnKeyStoreFile;
    private JLabel label2;
    private JPasswordField jPassKeyStore;
    private JRadioButton jRadioKeyStore;
    private JPanel panel2;
    private JLabel label9;
    private JTextField jTFPrivateKey;
    private JButton btnPrivateKeyFile;
    private JRadioButton jRadioOnlyPrivateKey;
    private JPanel panel3;
    private JLabel label10;
    private JTextField jTFCertificate;
    private JButton btnCertificateFile;
    private JRadioButton jRadioWithCertificate;
    private JPanel pnExecute;
    private JCheckBox jCheckPdf;
    private JButton btnSign;
    private JLabel label1;
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
