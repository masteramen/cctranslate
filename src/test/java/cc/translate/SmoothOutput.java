package cc.translate;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;
  
public class SmoothOutput
{
    public static void main(String[] args)
    {
        OutputPanel outputPanel = new OutputPanel();
        JFrame f = new JFrame("Smooth Output");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(outputPanel.getUIPanel(), "North");
        f.getContentPane().add(outputPanel);
        f.setSize(400,300);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}
  
class OutputPanel extends JPanel
{
    Font font;
    String text;
  
    public OutputPanel()
    {
        font = new Font("lucida sans unicode", Font.PLAIN, 14);
        text = "This is a notification " + 2 + "" +
                "notifi jkj kjkj kjkj kjk kjkj kjkjkk jkjj kj jkjkjkk jkkjkj  jk jkkjk kj jkj"
                + "k kjkjk kjj kk kjkjkj jjkjkjjkj jjk abcd efg hjk 1 2 3 4 5 6 7 8 a"
                + "k kjkjk kjj kk kjkjkj jjkjkjjkj jjk abcd efg hjk 1 2 3 4 5 6 7 8 a"
                + "k kjkjk kjj kk kjkjkj jjkjkjjkj jjk abcd efg hjk 1 2 3 4 5 6 7 8 a"
                + "k kjkjk kjj kk kjkjkj jjkjkjjkj jjk abcd efg hjk 1 2 3 4 5 6 7 8 a"
                + "k kjkjk kjj kk kjkjkj jjkjkjjkj jjk abcd efg hjk 1 2 3 4 5 6 7 8 a"
                + "lex gg gg g gg g";
        setBackground(Color.white);
    }
  
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(font);
        FontRenderContext frc = g2.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics(text, frc);
        int width = getWidth();
        int height = getHeight();
        float textWidth = (float)font.getStringBounds(text, frc).getWidth();
        float x = (width - textWidth)/2;
        float y = (height + lm.getHeight())/2 - lm.getDescent();
        g2.drawString(text, 40, 40);
        
        
        Font font = new Font(Font.SERIF, Font.PLAIN, 12);
        g2.setFont(font);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.drawString(text, 20, 30);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString(text, 20, 50);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g2.drawString(text, 20, 70);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.drawString(text, 20, 90);
        
    }
  
    private void saveImage()
    {
        int w = getWidth();
        int h = getHeight();
        BufferedImage image = new BufferedImage(w, h+35, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        paint(g2);
        g2.dispose();
        try
        {
            ImageIO.write(image, "png", new File("example.png"));
        }
        catch(IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
    }
  
    
    public JPanel getUIPanel()
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final Font[] fonts = ge.getAllFonts();
        final JComboBox fontCombo = new JComboBox();
        for(int i = 0; i < fonts.length; i++)
            fontCombo.addItem(fonts[i].getFontName());
        fontCombo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int index = fontCombo.getSelectedIndex();
                font = fonts[index].deriveFont((float)font.getSize());
                repaint();
            }
        });
        fontCombo.setSelectedItem(font.getFontName());
        SpinnerNumberModel model = new SpinnerNumberModel(22, 8, 64, 1);
        final JSpinner spinner = new JSpinner(model);
        spinner.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                float size = ((Integer)spinner.getValue()).floatValue();
                font = font.deriveFont(size);
                repaint();
            }
        });
        JButton saveButton = new JButton("save");
        saveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                saveImage();
            }
        });
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2,2,2,2);
        gbc.weightx = 1.0;
        panel.add(fontCombo, gbc);
        gbc.anchor = gbc.EAST;
        panel.add(new JLabel("size"), gbc);
        gbc.anchor = gbc.WEST;
        panel.add(spinner, gbc);
        gbc.anchor = gbc.CENTER;
        panel.add(saveButton, gbc);
        return panel;
    }
}