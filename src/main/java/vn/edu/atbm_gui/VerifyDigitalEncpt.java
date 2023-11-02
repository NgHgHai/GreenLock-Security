/*
 * Created by JFormDesigner on Thu Oct 19 18:58:28 ICT 2023
 */

package vn.edu.atbm_gui;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author hoang hai
 */
public class VerifyDigitalEncpt extends JPanel {
    public VerifyDigitalEncpt() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - hoanghai
        pnMain = new JPanel();
        pnTop = new JPanel();
        pnCenter = new JPanel();
        pnInput = new JPanel();
        scrollPane1 = new JScrollPane();
        textArea2 = new JTextArea();
        button4 = new JButton();
        pnEn_De = new JPanel();
        btnEncrypt = new JButton();
        scrollPane3 = new JScrollPane();
        jTAStatus = new JTextArea();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border.
        EmptyBorder( 0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing. border. TitledBorder. CENTER, javax. swing
        . border. TitledBorder. BOTTOM, new java .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ),
        java. awt. Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( )
        { @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062order" .equals (e .getPropertyName () ))
        throw new RuntimeException( ); }} );
        setLayout(new VerticalLayout());

        //======== pnMain ========
        {
            pnMain.setLayout(new BorderLayout());

            //======== pnTop ========
            {
                pnTop.setLayout(new FlowLayout());
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

                        //---- textArea2 ----
                        textArea2.setRows(5);
                        textArea2.setBorder(new TitledBorder(BorderFactory.createEmptyBorder(), "signed file", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION));
                        textArea2.setPreferredSize(new Dimension(500, 113));
                        scrollPane1.setViewportView(textArea2);
                    }
                    pnInput.add(scrollPane1);

                    //---- button4 ----
                    button4.setText("choose file");
                    pnInput.add(button4);
                }
                pnCenter.add(pnInput);

                //======== pnEn_De ========
                {
                    pnEn_De.setLayout(new FlowLayout());

                    //---- btnEncrypt ----
                    btnEncrypt.setText("verify");
                    pnEn_De.add(btnEncrypt);
                }
                pnCenter.add(pnEn_De);
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
    private JPanel pnCenter;
    private JPanel pnInput;
    private JScrollPane scrollPane1;
    private JTextArea textArea2;
    private JButton button4;
    private JPanel pnEn_De;
    private JButton btnEncrypt;
    private JScrollPane scrollPane3;
    private JTextArea jTAStatus;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
