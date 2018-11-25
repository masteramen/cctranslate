package cc.translate;

/*
 *  ====================================================================
 *  DemoRoundRectangle.java : This applet draws a collection of round-
 *  cornered rectagles over a canvas. The program provides code that
 *  allows a user to interact with the rectangles using the mouse. 
 *  More precisely, a user can click the mouse over any rectangle.
 *  The program recognizes the mouse clicks using API methods supported 
 *  for checking whether a rectangle contains a point. It then uses 
 *  a bounding box to draw small rectangular box indicating the box
 *  that has focus. Finally, a user can select a color from the list 
 *  of colors in the combo box -- the focus rectangle will be filled in.
 * 
 *  Adapted from : Pantham S., Pure JFC Swing, 1999.
 *  Modified by : Mark Austin                                March, 2001
 *  ====================================================================
 */

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Vector;

public class DemoRoundRectangle extends JApplet {
    RectanglesCanvas canvas;
    String[] colorLabels = {"White", "Red", "Blue", "Green", "Yellow", 
                            "Magenta", "Light Gray", "Black"};

    public void init() {

        // 1. Get the content pane and assign layout

        Container container = getContentPane();

        // 2. Add the canvas with rectangles

        canvas = new RectanglesCanvas();
        container.add(canvas);

        // 3. Create a control panel with titled border

        JPanel panel = new JPanel();
        TitledBorder border = new TitledBorder(
                  "Select a Color and Click Fill Button");
        panel.setBorder(border);

        // 4. Create a label, combo box and button

        JLabel label = new JLabel("Select a Color: ", JLabel.RIGHT);

        JComboBox comboBox = new JComboBox(colorLabels);
        comboBox.addActionListener(new ComboBoxListener());

        JButton fillButton = new JButton("Fill Color");
        fillButton.addActionListener(new ButtonListener());

        // 5. Add the label, combo box, and button to the panel

        panel.add(label);
        panel.add(comboBox);
        panel.add(fillButton);

        // 6. Add the panel to the frame

        container.add(BorderLayout.SOUTH, panel);
    }

    // 7. Canvas to draw round-cornered rectangles

    class RectanglesCanvas extends Canvas {

        // Vectors to store rectangles and colors

        Vector recVector    = new Vector();
        Vector filledRecs   = new Vector();
        Vector filledColors = new Vector();

        // References for bounding and selected rectangles

        Rectangle2D boundingRec      = null;
        RoundRectangle2D selectedRec = null;
           
        // A reference and colors to fill the round-rectangles

        Color fillColor = null;
        Color[] colors = {Color.white, Color.red, Color.blue, Color.green,
                          Color.yellow, Color.magenta, Color.lightGray,
                          Color.black};
        
        //  Constructor

        RectanglesCanvas() {

            // 8. Create instances of 2D rectangles

            recVector.addElement(
                new RoundRectangle2D.Float(25, 25, 75, 150, 20, 20));
            recVector.addElement(
                new RoundRectangle2D.Float(125, 25, 100, 75, 20, 10));
            recVector.addElement(
                new RoundRectangle2D.Float(75, 125, 125, 75, 20, 10));
            recVector.addElement(
                new RoundRectangle2D.Float(225, 125, 125, 75, 15, 20));
            recVector.addElement(
                new RoundRectangle2D.Float(150, 50, 125, 175, 50, 50));

            // 9. Add the mouse listener to receive a rectangle selection

            addMouseListener(new MyMouseListener());
            setBackground(Color.white);  // For canvas background color
            setSize(400, 225);  // Canvas width=400 height=225
        }

        public void paint(Graphics g) {

            // 10. Create the graphics context object

            Graphics2D g2D = (Graphics2D) g;

            // 11. Draw round rectangles defined in the constructor

            for (int i=0; i<recVector.size(); i++) {
                RoundRectangle2D r = (RoundRectangle2D) recVector.elementAt(i);
                g2D.draw(r);
            }
            g2D.drawString("hello world Hello world!",20,20);
            // 12. This snippet draws all the selected rectangles with
            //     specified colors while repainting.

            if (selectedRec != null) {

                for (int i=0; i<filledRecs.size(); i++) {
                    Color currColor = (Color)filledColors.elementAt(i);
                    g2D.setColor(currColor);
                    RoundRectangle2D currRec = (RoundRectangle2D)filledRecs.elementAt(i);
                    g2D.fill(currRec);

                    // Erase the black outline of slected rectangles; this is
                    // one easy way!

                    if (currColor == Color.white)
                        g2D.setColor(Color.black);
                    g2D.draw(currRec);
                }
            }

            // 13. Draw the dot-like rectangles whenever a rectangle is
            // selected

            if (boundingRec != null) {
                drawHighlightSquares(g2D, boundingRec);
            }

            // 14. Set the bounding rectangle back to null

            boundingRec = null;
        }

        public void drawHighlightSquares(Graphics2D g2D, Rectangle2D r) {
            double x = r.getX();
            double y = r.getY();
            double w = r.getWidth();
            double h = r.getHeight();
            g2D.setColor(Color.black);
            
            g2D.fill(new Rectangle.Double(x-3.0, y-3.0, 6.0, 6.0));
            g2D.fill(new Rectangle.Double(x+w*0.5-3.0, y-3.0, 6.0, 6.0));
            g2D.fill(new Rectangle.Double(x+w-3.0, y-3.0, 6.0, 6.0));
            g2D.fill(new Rectangle.Double(x-3.0, y+h*0.5-3.0, 6.0, 6.0));
            g2D.fill(new Rectangle.Double(x+w-3.0, y+h*0.5-3.0, 6.0, 6.0));
            g2D.fill(new Rectangle.Double(x-3.0, y+h-3.0, 6.0, 6.0));
            g2D.fill(new Rectangle.Double(x+w*0.5-3.0, y+h-3.0, 6.0, 6.0));
            g2D.fill(new Rectangle.Double(x+w-3.0, y+h-3.0, 6.0, 6.0));
        }
    }

    // 15. The mouse listener to handle hit detection of a rounded rectangle. 

    class MyMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            // Check if a rectangle contains the point of
            // mouse click
            for (int i=0; i<canvas.recVector.size(); i++) {
                RoundRectangle2D rec = (RoundRectangle2D) canvas.recVector.elementAt(i);

                if (rec.contains(e.getX(), e.getY())) {
                    canvas.selectedRec = rec;
                    canvas.boundingRec = canvas.selectedRec.getBounds2D();
                    canvas.repaint();
                    return;
                }
            }
        }
    }

    // 16. Combo box listener to handle color selections

    class ComboBoxListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JComboBox cBox = (JComboBox) e.getSource();
            String color = (String) cBox.getSelectedItem();
            for (int i=0; i<colorLabels.length; i++) {
                if(color.equals(colorLabels[i])) {
                    canvas.fillColor = canvas.colors[i];
                    return;
                }
            }
        }
    }

    // 17. Button listener to handle repaiting of canvas by using
    //     suitable colors for each of the selected rectangles.

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (canvas.selectedRec != null && canvas.fillColor != null) {
                canvas.filledRecs.addElement(canvas.selectedRec);
                canvas.filledColors.addElement(canvas.fillColor);
                canvas.repaint();
            }
        }
    }
}