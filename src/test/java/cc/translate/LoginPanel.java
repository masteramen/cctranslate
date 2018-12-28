package cc.translate;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class LoginPanel extends JPanel implements DocumentListener{
    private JTextArea tx;

	LoginPanel() {
        setOpaque(false);
        setLayout(new FlowLayout());
        add(new JLabel("username: ")); 
        JTextField textfield = new JTextField(10);
        textfield.setBorder(new TextBubbleBorder(Color.MAGENTA.darker(),2,4,0));
        
        JTextField o = new JTextField("The quick brown fox jumps over the lazy dog!");
        o.setUI(new RoundedFieldUI ());
        o.setBackground(Color.white);
        //o.setBorder(new TextBubbleBorder(Color.MAGENTA.darker(),2,20,0));
        
        tx = new AutoResizingTextArea(1,10,30);
		LimitedRowLengthDocument myDoc = new LimitedRowLengthDocument(tx, 30);		
		tx.setDocument(myDoc);
		//myDoc.addDocumentListener(this);
        
        tx.setUI(new RoundedFieldUI ());
        tx.setColumns(30);
        tx.setLineWrap(true);
        tx.setWrapStyleWord(true);
        
        //tx.setMaximumSize(new Dimension(100,50));
        //tx.setPreferredSize(new Dimension(100,80));
        add(new JScrollPane(tx));
        //add(tx);
		add(o);
        add(new JLabel("password: ")); JPasswordField passfield = new JPasswordField(10);
        passfield.setUI(new RoundedFieldUI ());

		add(passfield);
    }

	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		//tx.setSize(tx.getPreferredSize());
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}
}