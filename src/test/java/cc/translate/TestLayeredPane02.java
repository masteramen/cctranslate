package cc.translate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

public class TestLayeredPane02 {

    public static void main(String[] args) {
    	System.setProperty("java.net.useSystemProxies", "true");

        new TestLayeredPane02();
    }

    public TestLayeredPane02() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("Testing");
                frame.setUndecorated(true);
                frame.setBackground(new Color(50, 50, 50, 0));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                
                frame.add(new TestPane());
                frame.setSize(1800, 600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                
                /*
                 *         frame = new JFrame();
        frame.setTitle("Example");
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//app exited when frame closes
        //frame.setResizable(false);
        frame.setLayout(new GridBagLayout());
        frame.setBackground(new Color(50, 50, 50, 0));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(10, 10, 10,10);
        frame.add(new RoundedPanel(), gc);

        //pack frame (size components to preferred size)
        frame.pack();
        frame.setVisible(true);//make frame visible
                 * */
            }
        });
    }

    public class TestPane extends JLayeredPane {

        private JTextPane tp;

        public TestPane() {
            tp = new JTextPane();
            tp.setContentType("text/html");
            Reader reader = null;
            try {
                reader = new FileReader(new File("D:\\pwpis\\pwpis-reports\\src\\main\\java\\test.html"));
                tp.read(reader, "Help");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
            tp.setFont(UIManager.getFont("TextArea.font"));
            
            JScrollPane scrollPane = new JScrollPane(tp);
            tp.setOpaque(false);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            
            scrollPane.setRowHeaderView(new LineNumberHeader(tp));

            OverLayPane overLayPane = new OverLayPane();

            add(tp, new Integer(6));
            add(overLayPane, new Integer(5));

            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.insets = new Insets(10, 10, 10,10);
            add(new RoundedPanel(), new Integer(4));
            
            moveToFront(overLayPane);
        }

        @Override
        public void doLayout() {
        	int i=0;
            for (Component comp : getComponents()) {
            	if(comp instanceof RoundedPanel)
                	comp.setBounds(0, 0, getWidth(), getHeight());
            	else
            		comp.setBounds(10, 10, 100, 100);
            	i++;
            }
        }

        @Override
        public Dimension getPreferredSize() {
        	// TODO Auto-generated method stub
        	return tp.getPreferredSize();
        }
    }

    public class OverLayPane extends JPanel {

        public OverLayPane() {
            setLayout(new BorderLayout());
            try {
                BufferedImage img = ImageIO.read(new File("D:\\pwpis\\pwpis-reports\\src\\main\\java\\6.jpg"));
                JLabel label = new JLabel(new ImageIcon(img.getScaledInstance(-1, 200, Image.SCALE_SMOOTH)));
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.CENTER);
                add(label);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            setOpaque(false);
            setBackground(new Color(255, 0, 0, 128));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(getBackground());
            g2d.fill(new Rectangle(0, 0, getWidth(), getHeight()));
            g2d.dispose();
        }
    }
    public class LineNumberHeader extends JPanel {

        private JTextPane field;

        public LineNumberHeader(JTextPane field) {
            this.field = field;
            field.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    revalidate();
                    repaint();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    revalidate();
                    repaint();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    revalidate();
                    repaint();
                }

            });
        }

        @Override
        public Dimension getPreferredSize() {
            int height = field.getPreferredSize().height;
            Element root = field.getDocument().getDefaultRootElement();
            int lineCount = root.getElementCount();
            FontMetrics fm = getFontMetrics(getFont());
            int width = fm.stringWidth(Integer.toString(lineCount)) + 4;
            return new Dimension(width, height);
        }

        protected void drawLineNumber(Graphics2D g2d, int line, Element element) {
            int startIndex = element.getStartOffset();
            FontMetrics fm = g2d.getFontMetrics();
            String strLine = Integer.toString(line);
            try {
                Rectangle rect = field.modelToView(startIndex);
                int strWidth = fm.stringWidth(strLine);
                g2d.drawString(strLine, getWidth() - 2 - strWidth, rect.y + fm.getAscent());
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            Element root = field.getDocument().getDefaultRootElement();
            drawLineNumber(g2d, 1, root);
            for (int index = 0; index < root.getElementCount(); index++) {
                Element element = root.getElement(index);
                drawLineNumber(g2d, index + 1, element);
            }
            g2d.dispose();
        }
    }        
}