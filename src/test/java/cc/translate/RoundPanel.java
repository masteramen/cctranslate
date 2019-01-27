package cc.translate;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JDialog;

public class RoundPanel extends JPanel implements ActionListener {
	
	private boolean mouseOver = false;
	protected int screenX;
	protected int screenY;
	protected int myX;
	protected int myY;
	private JDialog parentDialog;
	private Timer timer;
	private int seconds;
	public RoundPanel(JDialog t) {
		this.parentDialog = t;
		JButton btnNewButton = new CircleButton("New button");
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setOpaque(false);

		btnNewButton.setBorderPainted(false);
		add(btnNewButton);
		setPreferredSize(new Dimension(60, 60));
		addMouseListener(new MouseAdapter() {
			@Override
		      public void mousePressed(MouseEvent e) {
		        screenX = e.getXOnScreen();
		        screenY = e.getYOnScreen();

		        myX = parentDialog.getX();
		        myY = parentDialog.getY();
		      }
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseEntered(e);
				RoundPanel.this.mouseOver = true;
				System.out.println("enter");
				RoundPanel.this.repaint();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseExited(e);
				RoundPanel.this.mouseOver = false;
				RoundPanel.this.repaint();
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {

		      @Override
		      public void mouseDragged(MouseEvent e) {
		        int deltaX = e.getXOnScreen() - screenX;
		        int deltaY = e.getYOnScreen() - screenY;

		        parentDialog.setLocation(myX + deltaX, myY + deltaY);
		      }

		      @Override
		      public void mouseMoved(MouseEvent e) { }

		    });
		
		this.timer = new Timer(1000,RoundPanel.this);
		timer.start();
		
	}

	//Image bg = new ImageIcon("water.jpg").getImage();
    @Override
    public void paintComponent(Graphics g) {
        //g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    	Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        float rate;
		if(mouseOver) rate = 0.8f;
        else rate = 0.2f;
        Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, rate);
        g2.setComposite(alpha);

        //g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
        g2.fillOval(0, 0, getSize().width, getSize().height);

        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
       // g2.setColor(new Color(0,0,0,0));
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2.setColor(Color.green);
       // g2.drawString(String.format("%d", seconds), 15, 35);
        g2.fillArc(0, 0, getWidth(), getHeight(), 180, - seconds);
        g2.setColor(Color.red);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        String text = String.format("%d", seconds);
        int textWith = g2.getFontMetrics().stringWidth(text);
        int textHeight = g2.getFontMetrics().getHeight();
        g2.drawString(text, (getWidth()-textWith)/2, (getHeight()-textHeight)/2+g2.getFontMetrics().getAscent());
        System.out.println(getWidth());
        System.out.println((getHeight()-textHeight)/2);
        //g2.drawImage(image, 0, 0, null);

       // g2.dispose();
    	//g.setColor(Color.RED);
    	//g.fillRect(0, 0, getWidth(), getHeight());
    }
    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param g The Graphics instance.
     * @param text The String to draw.
     * @param rect The Rectangle to center the text in.
     */
    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		
		this.seconds+=1;
		this.repaint();
		
	}
    
    
}
