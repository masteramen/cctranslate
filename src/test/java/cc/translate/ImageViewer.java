package cc.translate;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
public class ImageViewer extends Component{
    BufferedImage bi = null;
    
    @Override
    public void paint(Graphics g){
        Dimension dim = new Dimension();
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = ((int) dim.getWidth() / 2) - (bi.getWidth() / 2);
        int h = ((int) dim.getHeight() / 2) - (bi.getHeight() / 2);
        g.drawImage(bi, w, h, null);
    }
    
    @SuppressWarnings("LeakingThisInConstructor")
    public ImageViewer() {
        try {
            bi = ImageIO.read(new File("D:\\pwpis\\pwpis-reports\\src\\main\\java\\8.png"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }//end catch()
    }//end ImageViewer()
    
    public static void main(String[] args){
        JFrame f = new JFrame();
        f.add(new ImageViewer());
        f.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setUndecorated(true);
        f.setBackground(new Color(50, 50, 50, 0));
        f.getContentPane().setBackground(Color.BLACK);
        f.setVisible(true);
        
        ImageViewer iv = new ImageViewer();
        //f.getContentPane().add(iv);
        
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.setOpaque(false);
        p.add(new JButton("OKKK"));
        f.getContentPane().add(p, BorderLayout.SOUTH);
        f.setVisible(true);
    }//end main()
    
    public class ContentPane extends JPanel {
        
        public ContentPane() {
            setOpaque(false);
            setLayout(new BorderLayout());
        }//end ContentPane
    
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.setColor(getBackground());
            g2d.fill(getBounds());
            g2d.dispose();
        }
    
    }

    private static class ActionListenerImpl implements ActionListener {

        public ActionListenerImpl() {
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            System.out.println("click");
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    
}//end ImageViewer.class
