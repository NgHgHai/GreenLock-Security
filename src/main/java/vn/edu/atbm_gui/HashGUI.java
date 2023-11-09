/*
 * Created by JFormDesigner on Thu Oct 19 19:05:39 ICT 2023
 */

package vn.edu.atbm_gui;


import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;

import org.jdesktop.swingx.*;
import org.jdesktop.swingx.VerticalLayout;
import vn.edu.atbmmodel.hash.Hash;
import vn.edu.atbmmodel.tool.ChooseFile;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author hoang hai
 */
public class HashGUI extends JPanel {
    public boolean inputIsFile = false;

    public HashGUI() {
        initComponents();
    }

    private void btnCopyHash(ActionEvent e) {
        String textToCopy = jTAResult.getText();
        // Đưa nội dung vào Clipboard
        StringSelection stringSelection = new StringSelection(textToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private void btnChooseFileInput(ActionEvent e) {
        jTAInput.setEditable(false);
        valueStatus.setText("hash on file");
        inputIsFile = true;
        String path = ChooseFile.chooseFile("choose file input");
        jTAInput.setText(path);

    }

    private void jCheckUseTextInField(ActionEvent e) {
        if (jCheckUseTextInField.isSelected()) {
            jTAInput.setEditable(true);
            inputIsFile = false;
            valueStatus.setText("hash on text");
        } else {
            jTAInput.setEditable(false);
            inputIsFile = true;
            valueStatus.setText("hash on file");
        }
    }

    private void btnHash(ActionEvent e) {
        String algorithm = jCBAlgorithm.getSelectedItem().toString();
        String input = jTAInput.getText();
        String result = "";
        try {
            Hash hash = new Hash(algorithm);
            if (inputIsFile) {
                result = hash.hashFile(input);
                System.out.println(result);
            } else {
                result = hash.hash(input);
            }
            jTAResult.setText(result);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - hoanghai
        pnMain = new JPanel();
        pnTop = new JPanel();
        label3 = new JLabel();
        jCBAlgorithm = new JComboBox<>();
        label1 = new JLabel();
        label2 = new JLabel();
        valueStatus = new JLabel();
        pnCenter = new JPanel();
        pnInput = new JPanel();
        scrollPane1 = new JScrollPane();
        jTAInput = new JTextArea();
        btnChooseFileInput = new JButton();
        jCheckUseTextInField = new JCheckBox();
        pnExecute = new JPanel();
        btnHash = new JButton();
        pnResult = new JPanel();
        scrollPane2 = new JScrollPane();
        jTAResult = new JTextArea();
        btnCopyHash = new JButton();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (
        new javax. swing. border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn"
        , javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM
        , new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,12 )
        , java. awt. Color. red) , getBorder( )) );  addPropertyChangeListener (
        new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
        ) {if ("\u0062ord\u0065r" .equals (e .getPropertyName () )) throw new RuntimeException( )
        ; }} );
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
                jCBAlgorithm.setPreferredSize(new Dimension(110, 30));
                jCBAlgorithm.setModel(new DefaultComboBoxModel<>(new String[] {
                    "SHA-1",
                    "SHA-224",
                    "SHA-256",
                    "SHA-384",
                    "SHA-512",
                    "SHA-512(224)",
                    "SHA-512(256)",
                    "SHA3-224",
                    "SHA3-256",
                    "SHA3-384",
                    "SHA3-512",
                    "SHAKE128",
                    "SHAKE256",
                    "cSHAKE128",
                    "cSHAKE256",
                    "GOST3411",
                    "RIPEMD128",
                    "RIPEMD160",
                    "RIPEMD256",
                    "RIPEMD320",
                    "Tiger",
                    "Whirlpool"
                }));
                pnTop.add(jCBAlgorithm);

                //---- label1 ----
                label1.setText("status: ");
                pnTop.add(label1);
                pnTop.add(label2);

                //---- valueStatus ----
                valueStatus.setText("ready !!!");
                pnTop.add(valueStatus);
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
                        jTAInput.setPreferredSize(new Dimension(500, 113));
                        jTAInput.setEditable(false);
                        scrollPane1.setViewportView(jTAInput);
                    }
                    pnInput.add(scrollPane1);

                    //---- btnChooseFileInput ----
                    btnChooseFileInput.setText("choose file");
                    btnChooseFileInput.addActionListener(e -> btnChooseFileInput(e));
                    pnInput.add(btnChooseFileInput);

                    //---- jCheckUseTextInField ----
                    jCheckUseTextInField.setText("use text in field");
                    jCheckUseTextInField.setFont(new Font("Arial", Font.PLAIN, 12));
                    jCheckUseTextInField.addActionListener(e -> jCheckUseTextInField(e));
                    pnInput.add(jCheckUseTextInField);
                }
                pnCenter.add(pnInput);

                //======== pnExecute ========
                {
                    pnExecute.setLayout(new FlowLayout());

                    //---- btnHash ----
                    btnHash.setText("hash");
                    btnHash.addActionListener(e -> btnHash(e));
                    pnExecute.add(btnHash);
                }
                pnCenter.add(pnExecute);

                //======== pnResult ========
                {
                    pnResult.setLayout(new BoxLayout(pnResult, BoxLayout.X_AXIS));

                    //======== scrollPane2 ========
                    {

                        //---- jTAResult ----
                        jTAResult.setRows(5);
                        jTAResult.setBorder(new TitledBorder(BorderFactory.createEmptyBorder(), "Result"));
                        scrollPane2.setViewportView(jTAResult);
                    }
                    pnResult.add(scrollPane2);

                    //---- btnCopyHash ----
                    btnCopyHash.setText("copy");
                    btnCopyHash.addActionListener(e -> btnCopyHash(e));
                    pnResult.add(btnCopyHash);
                }
                pnCenter.add(pnResult);
            }
            pnMain.add(pnCenter, BorderLayout.CENTER);
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
    private JLabel label1;
    private JLabel label2;
    private JLabel valueStatus;
    private JPanel pnCenter;
    private JPanel pnInput;
    private JScrollPane scrollPane1;
    private JTextArea jTAInput;
    private JButton btnChooseFileInput;
    private JCheckBox jCheckUseTextInField;
    private JPanel pnExecute;
    private JButton btnHash;
    private JPanel pnResult;
    private JScrollPane scrollPane2;
    private JTextArea jTAResult;
    private JButton btnCopyHash;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setContentPane(new HashGUI());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
