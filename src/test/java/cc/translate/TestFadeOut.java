package cc.translate;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class TestFadeOut {
  JFrame frame = new JFrame();
  JButton button = new JButton("Catch the screenshot");
  Timer timer;
  Robot robot;
  JLabel label = new JLabel();

  public TestFadeOut() {
    try {
      robot = new Robot();
    } catch (Exception e1) {
      e1.printStackTrace();
    }

    timer = new Timer(3000, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Rectangle size = new Rectangle(Toolkit.getDefaultToolkit()
            .getScreenSize());
        Image image = robot.createScreenCapture(size);
        label.setIcon(new ImageIcon(image));
        frame.setVisible(true);
      }
    });
    timer.setRepeats(false);

    button.addActionListener(e -> {
      frame.setVisible(false);
      timer.start();
    });

    frame.add(button, BorderLayout.NORTH);
    frame.add(label, BorderLayout.CENTER);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1024, 768);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    new TestFadeOut();
  }
}