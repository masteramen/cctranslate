
package cc.translate.notify;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JWindow;

import com.sun.awt.AWTUtilities;

import cc.translate.util.ScreenUtil;
import cc.translate.util.SwingUtil;


@SuppressWarnings({"Duplicates", "FieldCanBeLocal", "WeakerAccess", "DanglingJavadoc"})
public
class AsDesktop extends JWindow implements INotify {
    private static final long serialVersionUID = 1L;

    private final LookAndFeel look;
    private final Notify notification;

	private NotifyPanel notifyCanvas;


    // this is on the swing EDT
    @SuppressWarnings("NumericCastThatLosesPrecision")
    AsDesktop(final Notify notification, final ImageIcon image, final Theme theme) {
        this.notification = notification;

        setAlwaysOnTop(true);


        setLocation(Short.MIN_VALUE, Short.MIN_VALUE);

        Rectangle bounds;
        GraphicsDevice device;

        if (notification.screenNumber == Short.MIN_VALUE) {
            // set screen position based on mouse
            Point mouseLocation = MouseInfo.getPointerInfo()
                                           .getLocation();

            device = ScreenUtil.getMonitorAtLocation(mouseLocation);
        }
        else {
            // set screen position based on specified screen
            int screenNumber = notification.screenNumber;
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice screenDevices[] = ge.getScreenDevices();

            if (screenNumber < 0) {
                screenNumber = 0;
            }
            else if (screenNumber > screenDevices.length - 1) {
                screenNumber = screenDevices.length - 1;
            }

            device = screenDevices[screenNumber];
        }

        bounds = device.getDefaultConfiguration()
                       .getBounds();


        notifyCanvas = new NotifyPanel(this, notification, image, theme);
        
        setContentPane(notifyCanvas);
        //getContentPane().add(notifyCanvas);

        setSize(notifyCanvas.getNotifySize());
        look = new LookAndFeel(this, this, notifyCanvas, notification, bounds, true);
      

    }

    @Override
    public
    void onClick(final int x, final int y) {
        look.onClick(x, y);
    }

    /**
     * Shakes the popup
     *
     * @param durationInMillis now long it will shake
     * @param amplitude a measure of how much it needs to shake. 4 is a small amount of shaking, 10 is a lot.
     */


    @Override
    public void setLocation(int x, int y) {
    	// TODO Auto-generated method stub
    	super.setLocation(x, y);
    }
    @Override
    public
    void setVisible(final boolean visible) {
        // was it already visible?
        if (visible == isVisible()) {
            // prevent "double setting" visible state
            return;
        }
    
        // this is because the order of operations are different based upon visibility.
        look.updatePositionsPre(visible);

        super.setVisible(visible);

        // this is because the order of operations are different based upon visibility.
        look.updatePositionsPost(visible);

        if (visible) {
        	
            this.toFront();
        }
    }

    // setVisible(false) with any extra logic
    void doHide() {
        super.setVisible(false);
    }

    @Override
    public
    void close() {
        // this must happen in the Swing EDT. This is usually called by the active renderer
        SwingUtil.invokeLater(new Runnable() {
            @Override
            public
            void run() {
                doHide();
                look.close();

                removeAll();
                dispose();

                notification.onClose();
            }
        });
    }

	@Override
	public void updateNotify() {
		look.updateUI();

	}
}
