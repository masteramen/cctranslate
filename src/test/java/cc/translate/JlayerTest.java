package cc.translate;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.FlowLayout;

public class JlayerTest extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JlayerTest frame = new JlayerTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JlayerTest() {
		setUndecorated(true);

		//setOpacity(0.0f);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setBackground(new Color(50, 50,50,0));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setOpaque(false);
		setContentPane(contentPane);
		
		JLayeredPane layeredPane = new JLayeredPane();
		contentPane.add(layeredPane, BorderLayout.CENTER);
		layeredPane.setLayout(new BorderLayout(0, 2));
		
		JLabel lblNewLabel = new JLabel("1111111111111111New label");
		layeredPane.add(lblNewLabel, BorderLayout.CENTER);
		lblNewLabel.setForeground(Color.red);
		JLabel btnNewButton = new JLabel("        New buttonddddddddddddddd");
		btnNewButton.setOpaque(false);
		btnNewButton.setForeground(Color.GREEN);
		layeredPane.setLayer(btnNewButton, 1);
		layeredPane.add(btnNewButton,BorderLayout.SOUTH);
	}
}
