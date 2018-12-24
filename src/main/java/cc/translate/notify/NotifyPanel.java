package cc.translate.notify;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.management.Notification;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NotifyPanel extends JLayeredPane {

	private JPanel topPanel;
	private JButton closeBtn;
	private JProgressBar progressBar;
	private JLabel lblTitle;
	private Notify notification;
	private JEditorPane contentEditor;
public NotifyPanel() {
	this("test","test");
}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JWindow frame = new JWindow();
					frame.setBounds(100, 100, 200, 200);

					frame.setContentPane(new NotifyPanel("test","test content"));
					frame.setVisible(true);
					frame.setAlwaysOnTop(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public NotifyPanel(String title,String content) {
        init(title, content);

		
	}

	private void init(String title, String content) {
		/*addComponentListener(new ComponentAdapter() {
            @Override
             public void componentResized(ComponentEvent e) {
                 setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
             }
         });*/
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(new BorderLayout(0, 0));
		
		topPanel = new JPanel();
		topPanel.setBackground(Color.BLACK);
		add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new BorderLayout(0, 0));
		
		lblTitle = new JLabel(title);
		lblTitle.setBackground(Color.BLACK);
		lblTitle.setForeground(Color.WHITE);
		topPanel.add(lblTitle);
		
		closeBtn = new JButton("x");
		closeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		closeBtn.setForeground(Color.WHITE);
		closeBtn.setBackground(Color.BLACK);
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
		contentEditor.setContentType("text/html");
		contentEditor.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentEditor.setBackground(Color.BLACK);
		contentEditor.setForeground(new Color(255, 255, 255));
		contentEditor.setText(content);

		add(contentEditor, BorderLayout.CENTER);
		
		progressBar = new JProgressBar();
		progressBar.setSize(new Dimension(0, 1));
		progressBar.setOpaque(true);
		progressBar.setPreferredSize(new Dimension(1, 1));
		progressBar.setMaximumSize(new Dimension(32767, 1));
		progressBar.setMinimumSize(new Dimension(1, 1));
		progressBar.setBorder(new EmptyBorder(0, 0, 0, 0));
		progressBar.setBorderPainted(false);
		add(progressBar, BorderLayout.SOUTH);
		

	}

	public NotifyPanel(AsDesktop asDesktop, Notify notification, ImageIcon image, Theme theme) {
		this.notification = notification;
		init(this.notification.title, this.notification.text);
	}

	public Dimension getNotifySize() {

		String html=String.format("<div style=\"color:white;background-color:black;\"><div>%s</div></div>", this.notification.text);

        JEditorPane dummyEditorPane=new JEditorPane();
        dummyEditorPane.setContentType("text/html");
        dummyEditorPane.setSize(NotifyCanvas.WIDTH,Short.MAX_VALUE);
        dummyEditorPane.setText(html);
        contentEditor.setContentType("text/html");

        contentEditor.setText(html);
        contentEditor.setSize(NotifyCanvas.WIDTH,Short.MAX_VALUE);
       

		setSize(NotifyCanvas.WIDTH,(int) (dummyEditorPane.getPreferredSize().height+lblTitle.getPreferredSize().getHeight()+progressBar.getPreferredSize().height
				+8));
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
