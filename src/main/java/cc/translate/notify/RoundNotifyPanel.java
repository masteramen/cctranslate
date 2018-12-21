package cc.translate.notify;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.management.Notification;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RoundNotifyPanel extends JPanel implements INotify {

	private JPanel topPanel;
	private JButton closeBtn;
	private JProgressBar progressBar;
	private JLabel lblTitle;
	private Notify notification;
	private JEditorPane contentEditor;
	private JPanel contentPanel;
public RoundNotifyPanel() {
	this("test","test");
}
public void paintComponent(Graphics g) {
    //g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
	Graphics2D g2 = (Graphics2D) g;
    g2.setComposite(AlphaComposite.Src);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(Color.WHITE);
    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));

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

					frame.setContentPane(new RoundNotifyPanel("test","test contentdfdddddd ddddsdd dfdfddd  d df d fd"));

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

		contentPanel = new JPanel();
        setLayout(new BorderLayout());
		add(contentPanel,BorderLayout.CENTER);
		contentPanel.setOpaque(false);
		contentPanel.setLayout(new BorderLayout(0, 0));
		topPanel = new JPanel();
		contentPanel.add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new BorderLayout(0, 0));
		topPanel.setOpaque(false);
		lblTitle = new JLabel(title);
		lblTitle.setForeground(Color.WHITE);
		topPanel.add(lblTitle);
		
		closeBtn = new JButton("x");
		closeBtn.setOpaque(false);
		closeBtn.setContentAreaFilled(false);
		closeBtn.setBorderPainted(false);

		closeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		closeBtn.setForeground(Color.WHITE);
		closeBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				closeBtn.setForeground(Color.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				closeBtn.setForeground(Color.BLACK);

			}
		});
		closeBtn.setBorder(new EmptyBorder(0, 0, 0, 0));
		closeBtn.setPreferredSize(new Dimension(20, 20));
		topPanel.add(closeBtn, BorderLayout.EAST);
		//setContentPane(contentPane);
		
		contentEditor = new JEditorPane();
		contentEditor.setOpaque(false);
		contentEditor.setContentType("text/html");
		contentEditor.setForeground(new Color(255, 255, 255));
		contentEditor.setText(content);

		contentPanel.add(contentEditor);
		
		progressBar = new JProgressBar();
		progressBar.setOpaque(true);
		progressBar.setPreferredSize(new Dimension(1, 1));
		progressBar.setMaximumSize(new Dimension(32767, 1));
		progressBar.setMinimumSize(new Dimension(1, 1));
		progressBar.setBorder(new EmptyBorder(0, 0, 0, 0));
		progressBar.setBorderPainted(false);
		contentPanel.add(progressBar, BorderLayout.SOUTH);
		updateSize(content);
		this.updateNotify();

	}

	public RoundNotifyPanel(AsDesktop asDesktop, Notify notification, ImageIcon image, Theme theme) {
		this.notification = notification;
		init(this.notification.title, this.notification.text);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
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
       

        contentPanel.setPreferredSize(new Dimension(NotifyCanvas.WIDTH,(int) (dummyEditorPane.getPreferredSize().height+lblTitle.getPreferredSize().getHeight()+progressBar.getPreferredSize().height
				+8)));
		return getSize();
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
}
