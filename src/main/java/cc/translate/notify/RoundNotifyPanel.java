package cc.translate.notify;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.UIManager;

class BarUI extends BasicProgressBarUI {

	private Rectangle r = new Rectangle();

	@Override
	protected void paintIndeterminate(Graphics g, JComponent c) {
		Graphics2D G2D = (Graphics2D) g;
		G2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		r = getBox(r);
		g.setColor(c.getForeground());
		g.fillRect(r.x + 10, r.y, r.width - 10, r.height);
	}
}

public class RoundNotifyPanel extends NotifyPanel {

	private JPanel topPanel;
	private JLabel closeBtn;
	private JProgressBar progressBar;
	private JLabel lblTitle;
	private Notify notification;
	private JEditorPane contentEditor;
	// private JPanel contentPanel;
	private BufferedImage bufferedImage;
	private int width;
	private int height;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel loadingLabel;
	private JScrollPane scrollBar;

	public RoundNotifyPanel() {
		this("test", "<div style=\"color:white;\"><div>Hello My Friends , CC translate is ready now.</div></div>");
	}

	public void paintComponent(Graphics g) {
		// g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
		if (bufferedImage == null || bufferedImage.getHeight() != height || bufferedImage.getWidth() != width) {
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
			//g2.setComposite(AlphaComposite.Clear);
			g2.setColor(new Color(50, 50, 50));
			g2.fillRect(0, 0, w, h);
			g2.dispose();
			System.out.println(String.format("paint height:%d", h));
		}
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
		g2.drawImage(bufferedImage, 0, 0, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		// g2.drawImage(image, 0, 0, null);
		// g2.dispose();
		// g.setColor(Color.RED);
		// g.fillRect(0, 0, getWidth(), getHeight());
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JDialog frame = new JDialog();
					frame.setUndecorated(true);
					// frame.setBounds(100, 100, 200, 200);
					//frame.setBackground(new Color(50, 50, 50, 0));
					frame.requestFocusInWindow();
					//frame.setOpacity(0);
					 frame.setBackground(new Color(50,50,50,0));
					String str = "";
					for (int i = 0; i < 1; i++)
						str += "坚持改革开放的初心和使命 I am trying to paint a rounded rectangle around a JScrollPane. For the life of me I can't figure out how to do this! No matter what I try, the border is not visible. I have figured out that it is drawing BEHIND the contents and not over them. The only thing inside the scroll pane is a JPanel with some graphics painted onto it. Does anyone know how to fix this?";

					frame.setContentPane(new RoundNotifyPanel("test",
							"<div style=\"color:white;white-space:pre-wrap;\"><div>" + str + "</div></div>"));

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
	public RoundNotifyPanel(String title, String content) {
		init(title, content);

	}

	private void init(String title, String content) {
		setLayout(null);
		// contentPanel = new JPanel();
		// contentPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		// contentPanel
		// setLayout(new BorderLayout());

		lblTitle = new JLabel("");
		// lblTitle.setPreferredSize(new Dimension(300, 20));
		lblTitle.setForeground(Color.WHITE);
		
		closeBtn = new JLabel("x");
		closeBtn.setBorder(new EmptyBorder(0, 0, 0, 0));

		setupCloseButton();

		// setContentPane(contentPane);

		contentEditor = new JEditorPane();
		// contentEditor.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentEditor.setOpaque(false);
		contentEditor.setContentType("text/html");
		contentEditor.setForeground(new Color(255, 255, 255));
		contentEditor.setText(content);

		// contentPanel.add(contentEditor);

		panel = new JPanel();
		panel.setOpaque(false);
		// contentPanel.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		progressBar = new JProgressBar();
		progressBar.setOpaque(false);
		//progressBar.setForeground(Color.LIGHT_GRAY);
		//progressBar.setBackground(Color.LIGHT_GRAY);

		progressBar.setBorder(null);
		progressBar.setBorderPainted(false);

		lblNewLabel = new JLabel("");
		lblNewLabel.setPreferredSize(new Dimension(5, 1));
		panel.add(lblNewLabel, BorderLayout.WEST);

		lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setPreferredSize(new Dimension(5, 1));
		panel.add(lblNewLabel_1, BorderLayout.EAST);

		loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
		// loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		// loadingLabel.setHorizontalTextPosition(JLabel.CENTER);
		panel.add(loadingLabel, BorderLayout.NORTH);


		scrollBar = new JScrollPane(contentEditor, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// contentEditor.setBounds(0,0,getWidth(),getHeight());
		scrollBar.setWheelScrollingEnabled(true);
		scrollBar.setBorder(new EmptyBorder(20, 5, 5, 5));
		scrollBar.setOpaque(false);
		scrollBar.getViewport().setOpaque(false);

		scrollBar.getVerticalScrollBar().setOpaque(false);

		scrollBar.getVerticalScrollBar().setPreferredSize(new Dimension(3, 0));

		scrollBar.getVerticalScrollBar().setUI(new MyScrollBarUI());
		add(progressBar, JLayeredPane.PALETTE_LAYER, 3);
		add(scrollBar, JLayeredPane.DEFAULT_LAYER, 3);
		add(closeBtn, JLayeredPane.PALETTE_LAYER, 3);
		updateSize(content);

		this.updateNotify();

	}

	private void setupCloseButton() {
		//closeBtn.setContentAreaFilled(false);
		//closeBtn.setBorderPainted(false);
		closeBtn.setPreferredSize(new Dimension(16, 16));
		closeBtn.setAlignmentY(Component.TOP_ALIGNMENT);
		closeBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
		closeBtn.setOpaque(false);
		closeBtn.setForeground(Color.GRAY);
		closeBtn.setBounds(5, 2, 16, 16);
		closeBtn.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseEntered(e);
				closeBtn.setForeground(Color.RED);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseExited(e);
				closeBtn.setForeground(Color.GRAY);
			}

		});
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
		String html = String.format("<div style=\"color:#FFFFFF;\"><div>%s</div></div>", text);

		JEditorPane dummyEditorPane = new JEditorPane();
		
		dummyEditorPane.setContentType("text/html");
		dummyEditorPane.setSize(NotifyCanvas.WIDTH, Short.MAX_VALUE);
		dummyEditorPane.setText(html);
		contentEditor.setContentType("text/html");

		contentEditor.setText(html);
		//contentEditor.setOpaque(true);
		// loadingLabel.setText(this.notification.title);

		double adjHeight = dummyEditorPane.getPreferredSize().height + lblTitle.getPreferredSize().getHeight()
				+ progressBar.getPreferredSize().height + loadingLabel.getPreferredSize().getHeight() + 8;
		Dimension dim = new Dimension(NotifyCanvas.WIDTH, (int) adjHeight + 20);

		setPreferredSize(dim);
		setSize(dim);
		setMinimumSize(dim);
		width = (int) dim.getWidth();
		height = (int) dim.getHeight();
		scrollBar.setBounds(0, 0, width, height > 600 ? 600 : height);
		scrollBar.getViewport().setOpaque(false);
		progressBar.setBounds(4, 1, width - 8, 1);

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

	public JComponent getCloseBtn() {
		return closeBtn;
	}

}

class MyScrollBarUI extends BasicScrollBarUI {
	private final Dimension d = new Dimension();

	@Override
	protected JButton createDecreaseButton(int orientation) {
		return new JButton() {
			@Override
			public Dimension getPreferredSize() {
				return d;
			}
		};
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {
		return new JButton() {
			@Override
			public Dimension getPreferredSize() {
				return d;
			}
		};
	}

	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
	}

	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color color = null;
		JScrollBar sb = (JScrollBar) c;
		if (!sb.isEnabled() || r.width > r.height) {
			return;
		} else if (isDragging) {
			color = Color.DARK_GRAY;
		} else if (isThumbRollover()) {
			color = Color.LIGHT_GRAY;
		} else {
			color = Color.GRAY;
		}
		g2.setPaint(color);
		g2.fillRoundRect(r.x, r.y, 10, r.height, r.width, r.width);
		g2.setPaint(Color.WHITE);
		g2.drawRoundRect(r.x, r.y, 10, r.height, r.width, r.width);
		g2.dispose();
	}

	@Override
	protected void setThumbBounds(int x, int y, int width, int height) {
		super.setThumbBounds(x, y, width, height);
		scrollbar.repaint();
	}
}