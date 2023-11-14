/*
 * Created by JFormDesigner on Thu Oct 19 19:20:35 ICT 2023
 */

package vn.edu.atbm_gui;


import org.jdesktop.swingx.VerticalLayout;
import vn.edu.atbmmodel.hash.Hash;
import vn.edu.atbmmodel.tool.ChooseFile;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author hoang hai
 */
public class CheckSum extends JPanel {
    public boolean inputIsFile = false;

    public CheckSum() {
        initComponents();
    }

    private void btnChooseFileInput(ActionEvent e) {
        jTAInput.setEditable(false);
        jCheckUseTextInField.setSelected(false);
        inputIsFile = true;
        String path = ChooseFile.chooseFile("choose file input");
        jTAInput.setText(path);
    }

    private void jCheckUseTextInField(ActionEvent e) {
        // TODO add your code here
        if (jCheckUseTextInField.isSelected()) {
            jTAInput.setEditable(true);
            inputIsFile = false;
        } else {
            jTAInput.setEditable(false);
            inputIsFile = true;
        }
    }

    private void btnCheck(ActionEvent e) {
        // TODO add your code here
        try {
            String algorithm = jCBAlgorithm.getSelectedItem().toString();
            String input = jTAInput.getText();
            String hashValueTocheck = jTFhashValue.getText();
            String hashValue;
            Hash hash = new Hash(algorithm);
            if (inputIsFile) {
                hashValue = hash.hashFile(input);
            } else {
                hashValue = hash.hash(input);
            }
            System.out.println(hashValue);
            if (hashValue.equals(hashValueTocheck)) {
                JOptionPane.showMessageDialog(this, "hash value is correct");
            } else {
                JOptionPane.showMessageDialog(this, "hash value is NOT correct");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

        String result = "";

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
        label8 = new JLabel();
        jTFhashValue = new JTextField();
        pnExecute = new JPanel();
        btnCheck = new JButton();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing
        . border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing. border. TitledBorder
        . CENTER, javax. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("D\u0069alog" ,java .
        awt .Font .BOLD ,12 ), java. awt. Color. red) , getBorder( )) )
        ;  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
        ) {if ("\u0062order" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} )
        ;
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

                //======== pnKey ========
                {
                    pnKey.setLayout(new FlowLayout());

                    //---- label8 ----
                    label8.setText("checksums");
                    pnKey.add(label8);

                    //---- jTFhashValue ----
                    jTFhashValue.setPreferredSize(new Dimension(500, 30));
                    pnKey.add(jTFhashValue);
                }
                pnCenter.add(pnKey);

                //======== pnExecute ========
                {
                    pnExecute.setLayout(new FlowLayout());

                    //---- btnCheck ----
                    btnCheck.setText("check");
                    btnCheck.addActionListener(e -> btnCheck(e));
                    pnExecute.add(btnCheck);
                }
                pnCenter.add(pnExecute);
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
    private JPanel pnCenter;
    private JPanel pnInput;
    private JScrollPane scrollPane1;
    private JTextArea jTAInput;
    private JButton btnChooseFileInput;
    private JCheckBox jCheckUseTextInField;
    private JPanel pnKey;
    private JLabel label8;
    private JTextField jTFhashValue;
    private JPanel pnExecute;
    private JButton btnCheck;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new CheckSum());
        frame.pack();
        frame.setVisible(true);
    }
}
