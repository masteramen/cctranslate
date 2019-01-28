package cc.translate;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


//https://stackoverflow.com/questions/12513436/jtextfield-with-rounded-corner-preserving-shadows


public class FrameTestBase2   {
    public static void main(String args[]) {
		System.setProperty("apple.awt.UIElement", "true");
		//System.setProperty("java.awt.headless", "true");
		System.out.println("No docker");
		// System.setProperty("java.awt.headless", "true");
		System.setProperty("apple.awt.UIElement", "true");

		UIManager.put("swing.boldMetal", Boolean.FALSE);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JDialog t = new JDialog();
		        
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
		});


    }
}