package dorkbox.notify;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.StrokeBorder;

public class ShapedWindow {

    public static void main(String[] args) {
        new ShapedWindow();
    }

    public ShapedWindow() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JWindow frame = new JWindow();
                frame.setBackground(new Color(0, 0, 0, 0));
                frame.setContentPane(new ShapedPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setAlwaysOnTop(true);
            }
        });
    }

    public class ShapedPane extends JPanel {

        public ShapedPane() {

            setOpaque(false);
            setLayout(new GridBagLayout());
            setBackground(new Color(0, 0, 0, 0));
            JButton button = new JButton("Close");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            add(button);

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
            Graphics2D g2d = (Graphics2D) g.create();
            RenderingHints hints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            RenderingHints qualityHints = new RenderingHints(
            		  RenderingHints.KEY_ANTIALIASING,
            		  RenderingHints.VALUE_ANTIALIAS_ON );
            		qualityHints.put(
            		  RenderingHints.KEY_RENDERING,
            		  RenderingHints.VALUE_RENDER_QUALITY );
            		g2d.setRenderingHints( qualityHints );
            g2d.setStroke(new BasicStroke(5)); 		
            g2d.setRenderingHints(hints);
            g2d.setColor(Color.BLACK);

           // g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            g2d.draw(roundedRectangle);
           // g2d.fill(new Ellipse2D.Float(0, 0, getWidth(), getHeight()));
            g2d.dispose();
        }
    }

}