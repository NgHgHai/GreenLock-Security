/*
 * Created by JFormDesigner on Thu Oct 19 19:05:39 ICT 2023
 */

package vn.edu.atbm_gui;


import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import org.jdesktop.swingx.VerticalLayout;
import vn.edu.atbmmodel.hash.Hash;

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
        valueStatus.setText("hash on file");
        inputIsFile = true;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose file input");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            jTAInput.setText(path);
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
            } else {
                result = hash.hash(input);
            }
            jTAResult.setText(result);
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void btnResetInputToPlainText(ActionEvent e) {
        valueStatus.setText("ready !!!");
        inputIsFile = false;
        jTAInput.setText("");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - hoanghai
        pnMain = new JPanel();
        pnTop = new JPanel();
        label3 = new JLabel();
        jCBAlgorithm = new JComboBox<>();
        btnResetInputToPlainText = new JButton();
        label1 = new JLabel();
        label2 = new JLabel();
        valueStatus = new JLabel();
        pnCenter = new JPanel();
        pnInput = new JPanel();
        scrollPane1 = new JScrollPane();
        jTAInput = new JTextArea();
        btnChooseFileInput = new JButton();
        pnExecute = new JPanel();
        btnHash = new JButton();
        pnResult = new JPanel();
        scrollPane2 = new JScrollPane();
        jTAResult = new JTextArea();
        btnCopyHash = new JButton();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border
        . EmptyBorder( 0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing. border. TitledBorder. CENTER, javax
        . swing. border. TitledBorder. BOTTOM, new java .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,
        12 ), java. awt. Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans
        . PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062order" .equals (e .
        getPropertyName () )) throw new RuntimeException( ); }} );
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

                //---- btnResetInputToPlainText ----
                btnResetInputToPlainText.setText("reset input to plain text");
                btnResetInputToPlainText.addActionListener(e -> btnResetInputToPlainText(e));
                pnTop.add(btnResetInputToPlainText);

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
                        scrollPane1.setViewportView(jTAInput);
                    }
                    pnInput.add(scrollPane1);

                    //---- btnChooseFileInput ----
                    btnChooseFileInput.setText("choose file");
                    btnChooseFileInput.addActionListener(e -> btnChooseFileInput(e));
                    pnInput.add(btnChooseFileInput);
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
                    pnResult.setLayout(new VerticalLayout());

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
    private JButton btnResetInputToPlainText;
    private JLabel label1;
    private JLabel label2;
    private JLabel valueStatus;
    private JPanel pnCenter;
    private JPanel pnInput;
    private JScrollPane scrollPane1;
    private JTextArea jTAInput;
    private JButton btnChooseFileInput;
    private JPanel pnExecute;
    private JButton btnHash;
    private JPanel pnResult;
    private JScrollPane scrollPane2;
    private JTextArea jTAResult;
    private JButton btnCopyHash;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
