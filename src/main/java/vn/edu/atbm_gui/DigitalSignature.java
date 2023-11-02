/*
 * Created by JFormDesigner on Thu Oct 19 18:43:59 ICT 2023
 */

package vn.edu.atbm_gui;


import org.jdesktop.swingx.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author hoang hai
 */
public class DigitalSignature extends JPanel {
    public DigitalSignature() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - hoanghai
        pnMain = new JPanel();
        pnTop = new JPanel();
        label3 = new JLabel();
        jCBAlgorithm = new JComboBox<>();
        label4 = new JLabel();
        jCBMode = new JComboBox();
        label5 = new JLabel();
        jCBStandard = new JComboBox();
        pnCenter = new JPanel();
        pnInput = new JPanel();
        scrollPane1 = new JScrollPane();
        jTAInput = new JTextArea();
        btnChooseFileInput = new JButton();
        pnKey = new JPanel();
        label8 = new JLabel();
        jCBExtend = new JComboBox<>();
        JTFKey = new JTextField();
        btnKeyFile = new JButton();
        pnExecute = new JPanel();
        btnSign = new JButton();
        scrollPane3 = new JScrollPane();
        jTAStatus = new JTextArea();

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.
        EmptyBorder(0,0,0,0), "JF\u006frmDes\u0069gner \u0045valua\u0074ion",javax.swing.border.TitledBorder.CENTER,javax.swing
        .border.TitledBorder.BOTTOM,new java.awt.Font("D\u0069alog",java.awt.Font.BOLD,12),
        java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {@Override public void propertyChange(java.beans.PropertyChangeEvent e){if("\u0062order".equals(e.getPropertyName()))
        throw new RuntimeException();}});
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
                pnTop.add(jCBMode);

                //---- label5 ----
                label5.setText("Standard");
                pnTop.add(label5);

                //---- jCBStandard ----
                jCBStandard.setPreferredSize(new Dimension(86, 26));
                pnTop.add(jCBStandard);
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
                    pnInput.add(btnChooseFileInput);
                }
                pnCenter.add(pnInput);

                //======== pnKey ========
                {
                    pnKey.setLayout(new FlowLayout());

                    //---- label8 ----
                    label8.setText("Portable Key Store");
                    pnKey.add(label8);

                    //---- jCBExtend ----
                    jCBExtend.setPreferredSize(new Dimension(86, 26));
                    jCBExtend.setModel(new DefaultComboBoxModel<>(new String[] {
                        ".pfx",
                        ".p12"
                    }));
                    pnKey.add(jCBExtend);

                    //---- JTFKey ----
                    JTFKey.setPreferredSize(new Dimension(500, 30));
                    pnKey.add(JTFKey);

                    //---- btnKeyFile ----
                    btnKeyFile.setText("choose key file");
                    pnKey.add(btnKeyFile);
                }
                pnCenter.add(pnKey);

                //======== pnExecute ========
                {
                    pnExecute.setLayout(new FlowLayout());

                    //---- btnSign ----
                    btnSign.setText("sign");
                    pnExecute.add(btnSign);
                }
                pnCenter.add(pnExecute);
            }
            pnMain.add(pnCenter, BorderLayout.CENTER);
        }
        add(pnMain);

        //======== scrollPane3 ========
        {

            //---- jTAStatus ----
            jTAStatus.setBorder(new TitledBorder("status"));
            jTAStatus.setRows(3);
            scrollPane3.setViewportView(jTAStatus);
        }
        add(scrollPane3);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - hoanghai
    private JPanel pnMain;
    private JPanel pnTop;
    private JLabel label3;
    private JComboBox<String> jCBAlgorithm;
    private JLabel label4;
    private JComboBox jCBMode;
    private JLabel label5;
    private JComboBox jCBStandard;
    private JPanel pnCenter;
    private JPanel pnInput;
    private JScrollPane scrollPane1;
    private JTextArea jTAInput;
    private JButton btnChooseFileInput;
    private JPanel pnKey;
    private JLabel label8;
    private JComboBox<String> jCBExtend;
    private JTextField JTFKey;
    private JButton btnKeyFile;
    private JPanel pnExecute;
    private JButton btnSign;
    private JScrollPane scrollPane3;
    private JTextArea jTAStatus;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
