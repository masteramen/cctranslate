package cc.translate.notify;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;

public class NotifyPanel extends JPanel implements INotify {

	private JPanel topPanel;
	private JButton closeBtn;
	private JProgressBar progressBar;
	private JLabel lblTitle;
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
        /*addComponentListener(new ComponentAdapter() {
            @Override
             public void componentResized(ComponentEvent e) {
                 setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
             }
         });*/
		setBorder(new EmptyBorder(0, 2, 0, 0));
		setLayout(new BorderLayout(0, 0));
		//setContentPane(contentPane);
		
		JEditorPane contentEditor = new JEditorPane();
		contentEditor.setBackground(Color.BLACK);
		contentEditor.setForeground(Color.WHITE);
		contentEditor.setText(content);
		add(contentEditor, BorderLayout.CENTER);
		
		topPanel = new JPanel();
		add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new BorderLayout(0, 0));
		
		lblTitle = new JLabel(title);
		topPanel.add(lblTitle);
		
		closeBtn = new JButton("x");
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
		closeBtn.setBorder(null);
		closeBtn.setPreferredSize(new Dimension(20, 20));
		topPanel.add(closeBtn, BorderLayout.EAST);
		
		progressBar = new JProgressBar();
		progressBar.setSize(new Dimension(0, 1));
		progressBar.setOpaque(true);
		progressBar.setPreferredSize(new Dimension(1, 2));
		progressBar.setMaximumSize(new Dimension(32767, 1));
		progressBar.setMinimumSize(new Dimension(1, 1));
		progressBar.setBorder(new EmptyBorder(0, 0, 0, 0));
		progressBar.setBorderPainted(false);
		add(progressBar, BorderLayout.SOUTH);
		

        JEditorPane dummyEditorPane=new JEditorPane();
        dummyEditorPane.setSize(300,Short.MAX_VALUE);
        dummyEditorPane.setText(contentEditor.getText());
        
        
        contentEditor.setSize(300,Short.MAX_VALUE);

		setSize(300,(int) (dummyEditorPane.getPreferredSize().height+lblTitle.getPreferredSize().getHeight()
				+3));
		System.out.println(dummyEditorPane.getPreferredSize().height);
		System.out.println(getSize().height);

		
	}

	public NotifyPanel(AsDesktop asDesktop, Notify notification, ImageIcon image, Theme theme) {
		this(notification.title,notification.text);
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
	public void updateUI() {
		// TODO Auto-generated method stub
		
	}

	public Dimension getNotifySize() {
		// TODO Auto-generated method stub
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
		// TODO Auto- method stub
		
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}
}
