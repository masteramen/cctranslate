package cc.translate;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Dimension;

public class TomatoPanel extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TomatoPanel dialog = new TomatoPanel();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TomatoPanel() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPane, BorderLayout.CENTER);
			{
				JScrollPane scrollPane = new JScrollPane();
				tabbedPane.addTab("New tab", null, scrollPane, null);
				{
					JTextArea textArea = new JTextArea();
					scrollPane.setViewportView(textArea);
				}
			}
			{
				JScrollPane scrollPane = new JScrollPane();
				tabbedPane.addTab("Todo", null, scrollPane, null);
				{
			        String[][] data = { 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Kundan Kumar Jha", "4031", "CSE" }, 
			                { "Anand Jha", "6014", "IT" } 
			            }; 
			      
			            // Column Names 
			            String[] columnNames = { "Name", "Roll Number", "Department" }; 
					table = new JTable(data, columnNames); 
					table.setBackground(Color.LIGHT_GRAY);
					scrollPane.setViewportView(table);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setPreferredSize(new Dimension(60, 10));
			buttonPane.setMaximumSize(new Dimension(30, 32767));
			getContentPane().add(buttonPane, BorderLayout.EAST);
			buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Ca");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
