package dorkbox.notify;

import java.awt.event.*; 
import java.awt.*; 
import javax.swing.*; 
  
  
public class solveit extends JWindow implements ActionListener { 
  
    // frame 
    static JWindow f; 
  
    // main class 
    public static void main(String[] args) 
    { 
        // create a new frame 
        f = new JWindow(); 
  
        // create a object 
        solveit s = new solveit(); 
  
        // create a panel 
        JPanel p = new JPanel(); 
  
        JButton b = new JButton("click"); 
  
        // add actionlistener to button 
        b.addActionListener(s); 
  
        // add button to panel 
        p.add(b); 
  
        f.add(p); 
  
        // set the size of frame 
        f.setSize(100, 100); 
        f.setAlwaysOnTop(true);

        f.setVisible(true);
        
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i=0;i<10;i++)
					{
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					f.setSize(f.getWidth(), f.getHeight()+20);
					}
				
				
			}
		}).start();
    } 
  
    // if button is pressed 
    public void actionPerformed(ActionEvent e) 
    { 
        String s = e.getActionCommand(); 
        if (s.equals("click")) { 
            // create a window 
            JWindow w = new JWindow(f); 
  
            // set panel 
            JPanel p = new JPanel(); 
  
            // create a label 
            JLabel l = new JLabel("this is a window"); 
  
            // set border 
            p.setBorder(BorderFactory.createLineBorder(Color.black)); 
  
            p.add(l); 
            w.add(p); 
  
            // set background 
            p.setBackground(Color.red); 
  
            // setsize of window 
            w.setSize(200, 100); 
  
            // set visibility of window 
            w.setVisible(true); 
  
            // set location of window 
            w.setLocation(100, 100); 
        } 
    } 
} 
