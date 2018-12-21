package cc.translate;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class BgPanel extends JPanel {
    //Image bg = new ImageIcon("water.jpg").getImage();
    @Override
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
}
//https://stackoverflow.com/questions/12513436/jtextfield-with-rounded-corner-preserving-shadows
class LoginPanel extends JPanel implements DocumentListener{
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

public class FrameTestBase extends JFrame  {
    public static void main(String args[]) {
        JPanel bgPanel = new BgPanel();
        bgPanel.setLayout(new BorderLayout());
        bgPanel.add(new LoginPanel(), BorderLayout.CENTER);

        FrameTestBase t = new FrameTestBase();
        t.setContentPane(bgPanel);
        t.setDefaultCloseOperation(EXIT_ON_CLOSE);
        t.setUndecorated(true);
        t.setBackground(new Color(50,50,50,0));
        t.setSize(250, 100);
        t.pack();
        t.setVisible(true);
        t.setLocation(300,300);
    }
}