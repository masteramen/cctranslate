package cc.translate;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JPanel;


//https://stackoverflow.com/questions/12513436/jtextfield-with-rounded-corner-preserving-shadows


public class FrameTestBase2 extends JDialog  {
    public static void main(String args[]) {

        FrameTestBase2 t = new FrameTestBase2();
        
        JPanel bgPanel = new  RoundPanel(t);
        bgPanel.setLayout(new BorderLayout());
        bgPanel.setOpaque(false);
        t.setContentPane(bgPanel);
       // t.setDefaultCloseOperation(EXIT_ON_CLOSE);
        t.setUndecorated(true);
        t.setBackground(new Color(50,50,50,0));
        t.setSize(250, 100);
        t.pack();
        t.setVisible(true);
        t.setLocation(300,300);
       t.setAlwaysOnTop(true);
    }
}