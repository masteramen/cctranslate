package dorkbox.notify;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;
import javax.swing.JEditorPane;
import java.awt.Color;
import javax.swing.JLabel;

public class test extends JWindow {

	private JPanel contentPane;
	private JLabel lblTitle;

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    ((Graphics2D) g).setStroke(new BasicStroke(0));
	    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		contentPane.paint(g);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					test frame = new test();
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
	public test() {
		setBounds(100, 100, 200, 200);
        addComponentListener(new ComponentAdapter() {
            @Override
             public void componentResized(ComponentEvent e) {
                 setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
             }
         });
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JEditorPane dtrpnHello = new JEditorPane();
		dtrpnHello.setBackground(Color.BLACK);
		dtrpnHello.setForeground(Color.WHITE);
		dtrpnHello.setText("Hello World ");
		contentPane.add(dtrpnHello, BorderLayout.CENTER);
		
		lblTitle = new JLabel("title \u6807\u9898");
		contentPane.add(lblTitle, BorderLayout.NORTH);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					System.out.println(dtrpnHello.getHeight());
					try {
						dtrpnHello.setText(dtrpnHello.getText()+" hello ");

				        JEditorPane dummyEditorPane=new JEditorPane();
				        dummyEditorPane.setSize(dtrpnHello.getWidth(),Short.MAX_VALUE);
				        dummyEditorPane.setText(dtrpnHello.getText());
				        
				        
				        dtrpnHello.setSize(dtrpnHello.getWidth(),Short.MAX_VALUE);
						System.out.println(dtrpnHello.getPreferredSize().height);

						test.this.setSize((int) test.this.getSize().getWidth(),(int) (dummyEditorPane.getPreferredSize().height+lblTitle.getPreferredSize().getHeight()));
						
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				
			}
		}).start();
	}

}
