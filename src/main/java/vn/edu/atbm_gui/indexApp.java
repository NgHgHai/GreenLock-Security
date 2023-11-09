/*
 * Created by JFormDesigner on Thu Oct 19 18:15:43 ICT 2023
 */

package vn.edu.atbm_gui;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

/**
 * @author hoang hai
 */
public class indexApp {

    public indexApp() {
        initComponents();
    }

    private void btnSKE(ActionEvent e) {
        // chuyen den frame SymmetricKeyEncpt
        JFrame frame = new JFrame("SymmetricKeyEncpt");
        SwingUtilities.updateComponentTreeUI(frame);
        JPanel pnMain = new SymmetricKeyEncpt().pnMain;
        frame.setContentPane(pnMain);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void btnPKE(ActionEvent e) {
        // chuyen den frame PublicKeyEncpt
        JFrame frame = new JFrame("PublicKeyEncpt");
        SwingUtilities.updateComponentTreeUI(frame);
        JPanel pnMain = new PublicKeyEncpt().pnMain;
        frame.setContentPane(pnMain);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void btnHash(ActionEvent e) {
        // chuyen den frame Hash
        JFrame frame = new JFrame("Hash");
        SwingUtilities.updateComponentTreeUI(frame);
        JPanel pnMain = new HashGUI().pnMain;
        frame.setContentPane(pnMain);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void btnCheckSums(ActionEvent e) {
        // chuyen den frame CheckSum
        JFrame frame = new JFrame("CheckSum");
        SwingUtilities.updateComponentTreeUI(frame);
        JPanel pnMain = new CheckSum().pnMain;
        frame.setContentPane(pnMain);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void btnDS(ActionEvent e) {
        // chuyen den frame DigitalSignature
        JFrame frame = new JFrame("DigitalSignature");
        SwingUtilities.updateComponentTreeUI(frame);
        JPanel pnMain = new DigitalSignature().pnMain;
        frame.setContentPane(pnMain);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void btnVDS(ActionEvent e) {
        // chuyen den frame VerifyDigitalSignature
        JFrame frame = new JFrame("VerifyDigitalSignature");
        SwingUtilities.updateComponentTreeUI(frame);
        JPanel pnMain = new VerifyDigitalSignature().pnMain;
        frame.setContentPane(pnMain);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - hoanghai
        pnMain = new JPanel();
        panel2 = new JPanel();
        label1 = new JLabel();
        panel4 = new JPanel();
        panel5 = new JPanel();
        label2 = new JLabel();
        pnBtn = new JPanel();
        btnSKE = new JButton();
        btnPKE = new JButton();
        btnDS = new JButton();
        btnVDS = new JButton();
        btnHash = new JButton();
        btnCheckSums = new JButton();
        btnGetKey = new JButton();

        //======== pnMain ========
        {
            pnMain.setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .EmptyBorder (
            0, 0 ,0 , 0) ,  "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e" , javax. swing .border . TitledBorder. CENTER ,javax . swing. border .TitledBorder
            . BOTTOM, new java. awt .Font ( "D\u0069al\u006fg", java .awt . Font. BOLD ,12 ) ,java . awt. Color .
            red ) ,pnMain. getBorder () ) ); pnMain. addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java .
            beans. PropertyChangeEvent e) { if( "\u0062or\u0064er" .equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } );
            pnMain.setLayout(new BorderLayout());

            //======== panel2 ========
            {
                panel2.setLayout(new FlowLayout());

                //---- label1 ----
                label1.setText("Green Tool To Lock");
                label1.setFont(new Font("Inter", Font.PLAIN, 50));
                label1.setForeground(new Color(0x339900));
                panel2.add(label1);
            }
            pnMain.add(panel2, BorderLayout.PAGE_START);

            //======== panel4 ========
            {
                panel4.setLayout(new BorderLayout());

                //======== panel5 ========
                {
                    panel5.setLayout(new FlowLayout());

                    //---- label2 ----
                    label2.setText("Choose mode :");
                    label2.setFont(new Font("Inter", Font.PLAIN, 25));
                    panel5.add(label2);
                }
                panel4.add(panel5, BorderLayout.NORTH);

                //======== pnBtn ========
                {
                    pnBtn.setLayout(new FlowLayout());

                    //---- btnSKE ----
                    btnSKE.setText("Symmetric Key Encryption");
                    btnSKE.setFont(new Font("Inter", Font.PLAIN, 15));
                    btnSKE.addActionListener(e -> btnSKE(e));
                    pnBtn.add(btnSKE);

                    //---- btnPKE ----
                    btnPKE.setText("Public Key Encryption");
                    btnPKE.setFont(new Font("Inter", Font.PLAIN, 15));
                    btnPKE.addActionListener(e -> btnPKE(e));
                    pnBtn.add(btnPKE);

                    //---- btnDS ----
                    btnDS.setText("Digital Signature");
                    btnDS.setFont(new Font("Inter", Font.PLAIN, 15));
                    btnDS.addActionListener(e -> btnDS(e));
                    pnBtn.add(btnDS);

                    //---- btnVDS ----
                    btnVDS.setText("Verify Digital Signature");
                    btnVDS.setFont(new Font("Inter", Font.PLAIN, 15));
                    btnVDS.addActionListener(e -> btnVDS(e));
                    pnBtn.add(btnVDS);

                    //---- btnHash ----
                    btnHash.setText("Hash");
                    btnHash.setFont(new Font("Inter", Font.PLAIN, 15));
                    btnHash.addActionListener(e -> btnHash(e));
                    pnBtn.add(btnHash);

                    //---- btnCheckSums ----
                    btnCheckSums.setText("CheckSums");
                    btnCheckSums.setFont(new Font("Inter", Font.PLAIN, 15));
                    btnCheckSums.addActionListener(e -> btnCheckSums(e));
                    pnBtn.add(btnCheckSums);

                    //---- btnGetKey ----
                    btnGetKey.setText("Get key");
                    btnGetKey.setFont(new Font("Inter", Font.PLAIN, 15));
                    pnBtn.add(btnGetKey);
                }
                panel4.add(pnBtn, BorderLayout.CENTER);
            }
            pnMain.add(panel4, BorderLayout.CENTER);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - hoanghai
    JPanel pnMain;
    private JPanel panel2;
    private JLabel label1;
    private JPanel panel4;
    private JPanel panel5;
    private JLabel label2;
    private JPanel pnBtn;
    private JButton btnSKE;
    private JButton btnPKE;
    private JButton btnDS;
    private JButton btnVDS;
    private JButton btnHash;
    private JButton btnCheckSums;
    private JButton btnGetKey;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
