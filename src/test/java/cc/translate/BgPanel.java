package cc.translate;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

public class BgPanel extends JPanel {
    //Image bg = new ImageIcon("water.jpg").getImage();
    @Override
    public void paintComponent(Graphics g) {
        //g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    	Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        //g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getWidth()/2, getHeight()/2));
        g2.drawOval(0, 0, getSize().width-1, getSize().height-1);
        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());
        //g2.drawImage(image, 0, 0, null);

       // g2.dispose();
    	//g.setColor(Color.RED);
    	//g.fillRect(0, 0, getWidth(), getHeight());
    }
}
