/*
 * Created by JFormDesigner on Thu Oct 19 18:58:28 ICT 2023
 */

package vn.edu.atbm_gui;

import java.awt.event.*;

import org.jdesktop.swingx.VerticalLayout;
import vn.edu.atbmmodel.digitalsignature.SignInData;
import vn.edu.atbmmodel.key.KeyGen;
import vn.edu.atbmmodel.tool.ChooseFile;
import vn.edu.atbmmodel.tool.ReadKeyFormFile;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.security.PublicKey;

/**
 * @author hoang hai
 */
public class VerifyDigitalSignature extends JPanel {

    boolean useTextInField = false;

    public VerifyDigitalSignature() {
        initComponents();
    }


    private void jCheckUseTextInField(ActionEvent e) {
        if (jCheckUseTextInField.isSelected()) {
            useTextInField = true;
            jTFData.setEditable(true);
        } else {
            useTextInField = false;
            jTFData.setEditable(false);
        }
    }

    private void btnPublicKeyFile(ActionEvent e) {
        String publicKeyFile = ChooseFile.chooseFile("Choose public key file");
        jTFPublicKey.setText(publicKeyFile);
    }


    private void btnSigFile(ActionEvent e) {
        String signedFile = ChooseFile.chooseFile("Choose signed file");
        jTFSignedFile.setText(signedFile);
    }

    private void btndataFile(ActionEvent e) {
        jCheckUseTextInField.setSelected(false);
        jTFData.setEditable(false);
        String dataFile = ChooseFile.chooseFile("Choose data file");
        jTFData.setText(dataFile);
    }


    private void btnVerify(ActionEvent e) {
        String algorithm = jCBAlgorithm.getSelectedItem().toString();
        try {
            if (useTextInField) {
                String data = jTFData.getText();
                byte[] publicKeyBytes = ReadKeyFormFile.readKeyFromFile(jTFPublicKey.getText());
                byte[] signedBytes = ReadKeyFormFile.readKeyFromFile(jTFSignedFile.getText());
                byte[] dataBytes = jTFData.getText().getBytes();
                if (publicKeyBytes == null || signedBytes == null) {
                    JOptionPane.showMessageDialog(null, "File not found");
                    return;
                }
                PublicKey publicKey = KeyGen.getInstance().getPublicKeyformBytes(publicKeyBytes);
                boolean v1 = SignInData.verifyDigitalSignature(data, signedBytes, algorithm, publicKey);
                System.out.println(v1);
                boolean v2 = SignInData.verifyDetachedData(dataBytes, signedBytes);
                System.out.println(v2);
                boolean verify = v1 || v2;
                if (verify) {
                    jTAStatus.setText("Verify success");
                } else {
                    jTAStatus.setText("Verify fail");
                }
                return;
            } else {
                byte[] publicKeyBytes = ReadKeyFormFile.readKeyFromFile(jTFPublicKey.getText());
                byte[] signedBytes = ReadKeyFormFile.readKeyFromFile(jTFSignedFile.getText());
                byte[] dataBytes = ReadKeyFormFile.readKeyFromFile(jTFData.getText());
                if (publicKeyBytes == null || signedBytes == null || dataBytes == null) {
                    JOptionPane.showMessageDialog(null, "File not found");
                    return;
                }
                PublicKey publicKey = KeyGen.getInstance().getPublicKeyformBytes(publicKeyBytes);

                boolean v1 = SignInData.verifyDetachedDataFile(jTFData.getText(), jTFSignedFile.getText());
                boolean v2 = SignInData.verifyDigitalSignatureFile(jTFData.getText(), jTFSignedFile.getText(), algorithm, publicKey);
                boolean verify = v1 || v2;
                if (verify) {
                    jTAStatus.setText("Verify success");
                } else {
                    jTAStatus.setText("Verify fail");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Verify fail");
        }
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - hoanghai
        pnMain = new JPanel();
        pnTop = new JPanel();
        pnTop2 = new JPanel();
        label3 = new JLabel();
        jCBAlgorithm = new JComboBox<>();
        pnCenter = new JPanel();
        pnInput = new JPanel();
        panel2 = new JPanel();
        label9 = new JLabel();
        jTFPublicKey = new JTextField();
        btnPublicKeyFile = new JButton();
        panel3 = new JPanel();
        label10 = new JLabel();
        jTFSignedFile = new JTextField();
        btnSigFile = new JButton();
        panel4 = new JPanel();
        label11 = new JLabel();
        jTFData = new JTextField();
        btndataFile = new JButton();
        jCheckUseTextInField = new JCheckBox();
        pnEn_De = new JPanel();
        btnVerify = new JButton();
        scrollPane3 = new JScrollPane();
        jTAStatus = new JTextArea();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new
        javax. swing. border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax
        . swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM, new java
        .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ), java. awt
        . Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans.
        PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062order" .
        equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
        setLayout(new VerticalLayout());

        //======== pnMain ========
        {
            pnMain.setLayout(new VerticalLayout());

            //======== pnTop ========
            {
                pnTop.setLayout(new FlowLayout());

                //======== pnTop2 ========
                {
                    pnTop2.setFont(new Font("Arial", Font.PLAIN, 12));
                    pnTop2.setLayout(new FlowLayout());

                    //---- label3 ----
                    label3.setText("Algorithm");
                    label3.setFont(new Font("Arial", Font.PLAIN, 12));
                    pnTop2.add(label3);

                    //---- jCBAlgorithm ----
                    jCBAlgorithm.setPreferredSize(new Dimension(115, 30));
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
                    pnTop2.add(jCBAlgorithm);
                }
                pnTop.add(pnTop2);
            }
            pnMain.add(pnTop);

            //======== pnCenter ========
            {
                pnCenter.setLayout(new VerticalLayout());

                //======== pnInput ========
                {
                    pnInput.setLayout(new VerticalLayout());

                    //======== panel2 ========
                    {
                        panel2.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel2.setLayout(new FlowLayout(FlowLayout.LEFT));

                        //---- label9 ----
                        label9.setText("Key (public key)");
                        label9.setMaximumSize(new Dimension(110, 17));
                        label9.setMinimumSize(new Dimension(110, 17));
                        label9.setPreferredSize(new Dimension(110, 17));
                        label9.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel2.add(label9);

                        //---- jTFPublicKey ----
                        jTFPublicKey.setPreferredSize(new Dimension(500, 30));
                        jTFPublicKey.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel2.add(jTFPublicKey);

                        //---- btnPublicKeyFile ----
                        btnPublicKeyFile.setText("choose key file");
                        btnPublicKeyFile.setPreferredSize(new Dimension(170, 30));
                        btnPublicKeyFile.setFont(new Font("Arial", Font.PLAIN, 12));
                        btnPublicKeyFile.addActionListener(e -> btnPublicKeyFile(e));
                        panel2.add(btnPublicKeyFile);
                    }
                    pnInput.add(panel2);

                    //======== panel3 ========
                    {
                        panel3.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel3.setLayout(new FlowLayout(FlowLayout.LEFT));

                        //---- label10 ----
                        label10.setText("Signed file (.sig)");
                        label10.setMaximumSize(new Dimension(110, 17));
                        label10.setMinimumSize(new Dimension(110, 17));
                        label10.setPreferredSize(new Dimension(110, 17));
                        label10.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel3.add(label10);

                        //---- jTFSignedFile ----
                        jTFSignedFile.setPreferredSize(new Dimension(500, 30));
                        jTFSignedFile.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel3.add(jTFSignedFile);

                        //---- btnSigFile ----
                        btnSigFile.setText("choose sig file");
                        btnSigFile.setPreferredSize(new Dimension(170, 26));
                        btnSigFile.setFont(new Font("Arial", Font.PLAIN, 12));
                        btnSigFile.addActionListener(e -> btnSigFile(e));
                        panel3.add(btnSigFile);
                    }
                    pnInput.add(panel3);

                    //======== panel4 ========
                    {
                        panel4.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel4.setLayout(new FlowLayout(FlowLayout.LEFT));

                        //---- label11 ----
                        label11.setText("data");
                        label11.setMaximumSize(new Dimension(110, 17));
                        label11.setMinimumSize(new Dimension(110, 17));
                        label11.setPreferredSize(new Dimension(110, 17));
                        label11.setFont(new Font("Arial", Font.PLAIN, 12));
                        panel4.add(label11);

                        //---- jTFData ----
                        jTFData.setPreferredSize(new Dimension(500, 30));
                        jTFData.setFont(new Font("Arial", Font.PLAIN, 12));
                        jTFData.setEditable(false);
                        panel4.add(jTFData);

                        //---- btndataFile ----
                        btndataFile.setText("choose data file");
                        btndataFile.setPreferredSize(new Dimension(170, 26));
                        btndataFile.setFont(new Font("Arial", Font.PLAIN, 12));
                        btndataFile.addActionListener(e -> btndataFile(e));
                        panel4.add(btndataFile);

                        //---- jCheckUseTextInField ----
                        jCheckUseTextInField.setText("use text in field");
                        jCheckUseTextInField.setFont(new Font("Arial", Font.PLAIN, 12));
                        jCheckUseTextInField.setToolTipText("if this checkbox is selected, it mean the system will use the data in field");
                        jCheckUseTextInField.addActionListener(e -> jCheckUseTextInField(e));
                        panel4.add(jCheckUseTextInField);
                    }
                    pnInput.add(panel4);
                }
                pnCenter.add(pnInput);

                //======== pnEn_De ========
                {
                    pnEn_De.setLayout(new FlowLayout());

                    //---- btnVerify ----
                    btnVerify.setText("verify");
                    btnVerify.addActionListener(e -> btnVerify(e));
                    pnEn_De.add(btnVerify);
                }
                pnCenter.add(pnEn_De);

                //======== scrollPane3 ========
                {

                    //---- jTAStatus ----
                    jTAStatus.setBorder(new TitledBorder("status"));
                    jTAStatus.setRows(10);
                    scrollPane3.setViewportView(jTAStatus);
                }
                pnCenter.add(scrollPane3);
            }
            pnMain.add(pnCenter);
        }
        add(pnMain);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - hoanghai
    JPanel pnMain;
    private JPanel pnTop;
    private JPanel pnTop2;
    private JLabel label3;
    private JComboBox<String> jCBAlgorithm;
    private JPanel pnCenter;
    private JPanel pnInput;
    private JPanel panel2;
    private JLabel label9;
    private JTextField jTFPublicKey;
    private JButton btnPublicKeyFile;
    private JPanel panel3;
    private JLabel label10;
    private JTextField jTFSignedFile;
    private JButton btnSigFile;
    private JPanel panel4;
    private JLabel label11;
    private JTextField jTFData;
    private JButton btndataFile;
    private JCheckBox jCheckUseTextInField;
    private JPanel pnEn_De;
    private JButton btnVerify;
    private JScrollPane scrollPane3;
    private JTextArea jTAStatus;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        JFrame frame = new JFrame();

        frame.setContentPane(new VerifyDigitalSignature());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
