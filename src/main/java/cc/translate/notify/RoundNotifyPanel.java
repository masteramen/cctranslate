package cc.translate.notify;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.management.Notification;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;

class BarUI extends BasicProgressBarUI { 

    private Rectangle r = new Rectangle(); 

    @Override 
    protected void paintIndeterminate(Graphics g, JComponent c) { 
     Graphics2D G2D = (Graphics2D) g; 
     G2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
               RenderingHints.VALUE_ANTIALIAS_ON); 
     r = getBox(r); 
     g.setColor(c.getForeground()); 
     g.fillRect(r.x+10,r.y,r.width-10,r.height); 
    } 
   } 

public class RoundNotifyPanel extends NotifyPanel {

	private JPanel topPanel;
	private JButton closeBtn;
	private JProgressBar progressBar;
	private JLabel lblTitle;
	private Notify notification;
	private JEditorPane contentEditor;
	//private JPanel contentPanel;
	private BufferedImage bufferedImage;
	private int width;
	private int height;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel loadingLabel;
	protected boolean showClose = false;
	private MouseListener mouseCloseListener;
public RoundNotifyPanel() {
	this("test","<div style=\"color:white;\"><div>Hello My Friends , CC translate is ready now.</div></div>");
}
public void paintComponent(Graphics g) {
    //g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
	if(bufferedImage==null  || bufferedImage.getHeight()!=height || bufferedImage.getWidth()!=width) {
	    int w = width;
	    int h = height;
	    bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2 = bufferedImage.createGraphics();
	    
	    g2.setComposite(AlphaComposite.Src);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setColor(Color.WHITE);
	    g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 20, 20));

	    // ... then compositing the image on top,
	    // using the white shape from above as alpha source
	    g2.setComposite(AlphaComposite.SrcAtop);
	    g2.setColor(new Color(50,50,50));
	    g2.fillRect(0, 0, w, h);
	    g2.dispose();
	    System.out.println(String.format("paint height:%d",h));
	}
	g.drawImage(bufferedImage, 0, 0, null);


	
    //g2.drawImage(image, 0, 0, null);
   // g2.dispose();
	//g.setColor(Color.RED);
	//g.fillRect(0, 0, getWidth(), getHeight());
}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JWindow frame = new JWindow();
					//frame.setBounds(100, 100, 200, 200);
					frame.setBackground(new Color(50,50,50,0));

					
					frame.setContentPane(new RoundNotifyPanel("test","<div style=\"color:white;\"><div>Hello My Friends , CC translate is ready now.</div></div>"));

					frame.pack();
					frame.setVisible(true);
					frame.setAlwaysOnTop(true);
					frame.setLocationRelativeTo(null);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RoundNotifyPanel(String title,String content) {
        init(title, content);

		
	}

	private void init(String title, String content) {
		setLayout(null);
		//contentPanel = new JPanel();
		//contentPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		//contentPanel
        //setLayout(new BorderLayout());

		lblTitle = new JLabel("");
		lblTitle.setPreferredSize(new Dimension(300, 20));
		lblTitle.setForeground(Color.WHITE);
		
		closeBtn = new JButton("x");
		closeBtn.setBounds(0,0,20,20);
		
		setupCloseButton();
		
		//setContentPane(contentPane);
		
		contentEditor = new JEditorPane();
		contentEditor.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentEditor.setOpaque(false);
		contentEditor.setContentType("text/html");
		contentEditor.setForeground(new Color(255, 255, 255));
		contentEditor.setText(content);

		//contentPanel.add(contentEditor);
		
		panel = new JPanel();
		panel.setOpaque(false);
		//contentPanel.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		progressBar = new JProgressBar();
		progressBar.setBackground(Color.BLACK);

		progressBar.setPreferredSize(new Dimension(1, 1));
		progressBar.setMaximumSize(new Dimension(32767, 1));
		progressBar.setMinimumSize(new Dimension(1, 1));
		progressBar.setBorder(null);
		progressBar.setBorderPainted(false);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setPreferredSize(new Dimension(5, 1));
		panel.add(lblNewLabel, BorderLayout.WEST);
		
		lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setPreferredSize(new Dimension(5, 1));
		panel.add(lblNewLabel_1, BorderLayout.EAST);
		
		loadingLabel = new JLabel("Loading...",SwingConstants.CENTER);
		//loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		//loadingLabel.setHorizontalTextPosition(JLabel.CENTER);
		panel.add(loadingLabel, BorderLayout.NORTH);
		updateSize(content);
		contentEditor.setBounds(0,0,getWidth(),getHeight());
		
		panel.add(progressBar, BorderLayout.CENTER);
		progressBar.setBounds(8,1,getWidth()-16,1);
		add(progressBar, JLayeredPane.DEFAULT_LAYER,3);
		add(closeBtn, JLayeredPane.DEFAULT_LAYER,3);

		add(contentEditor, JLayeredPane.DEFAULT_LAYER,3);

		contentEditor.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				if(e.getX()<20&&e.getY()<20){
					closeBtn.setForeground(Color.RED);
				}else {
					closeBtn.setForeground(Color.WHITE);
				}
				
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		this.updateNotify();

	}
	private void setupCloseButton() {
		closeBtn.setContentAreaFilled(false);
		closeBtn.setBorderPainted(false);
		closeBtn.setPreferredSize(new Dimension(16, 16));
		closeBtn.setAlignmentY(Component.TOP_ALIGNMENT);
		closeBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
		closeBtn.setOpaque(false);
		closeBtn.setForeground(Color.WHITE);
		closeBtn.setBorder(null);
	}

	public RoundNotifyPanel(AsDesktop asDesktop, Notify notification, ImageIcon image, Theme theme) {
		this.notification = notification;
		init(this.notification.title, this.notification.text);
	}

	public void updateNotify() {

		repaint();
	}

	public Dimension getNotifySize() {

		String text = this.notification.text;
		return updateSize(text);
	
	}

	private Dimension updateSize(String text) {
		String html=String.format("<div style=\"color:white;\"><div>%s</div></div>",text );

        JEditorPane dummyEditorPane=new JEditorPane();
        dummyEditorPane.setContentType("text/html");
        dummyEditorPane.setSize(NotifyCanvas.WIDTH,Short.MAX_VALUE);
        dummyEditorPane.setText(html);
        contentEditor.setContentType("text/html");

        contentEditor.setText(html);
       // loadingLabel.setText(this.notification.title);
       
        double adjHeight = dummyEditorPane.getPreferredSize().height+lblTitle.getPreferredSize().getHeight()+progressBar.getPreferredSize().height+loadingLabel.getPreferredSize().getHeight()
				+8;
        Dimension dim = new Dimension(NotifyCanvas.WIDTH, (int)adjHeight);;

		setPreferredSize(dim);
		setSize(dim);
		setMinimumSize(dim);
		width = (int) dim.getWidth();
		height = (int) dim.getHeight();
        
        System.out.println(String.format("text height:%f", adjHeight));
        System.out.println(String.format("panel height:%d", getHeight()));
        bufferedImage = null;
		return dim;
	}

	public void setProgress(int progress) {
		progressBar.setValue(progress);
		
	}

	public int getProgress() {
		// TODO Auto-generated method stub
		return progressBar.getValue();
	}

	public void resetCacheImage() {
		this.updateNotify();
		
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}
	public JEditorPane getContentEditor() {
		return contentEditor;
	}
	public JButton getCloseBtn() {
		return closeBtn;
	}

	public void addContentPanelMouseListener(MouseListener mouseListener) {
		contentEditor.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getX()<20 && e.getY()< 20){
					if(mouseCloseListener!=null)mouseCloseListener.mouseReleased(e);
				}
				
			}
			

		});
		this.contentEditor.addMouseListener(mouseListener);
	}
	public void setCloseMouseListener(MouseListener mouseListener) {
		this.mouseCloseListener = mouseListener;
		
	}
}
