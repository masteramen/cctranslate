package cc.translate.notify;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class TargetedMouseHandler implements AWTEventListener
{

    private Component parent;
    private Component innerBound;
    private volatile boolean hasExited = true;
	private MouseListener mouseListener;

    public TargetedMouseHandler(Component p, Component p2, MouseListener mouseListener)
    {
        parent = p;
        innerBound = p2;
        this.mouseListener = mouseListener;
    }

    @Override
    public void eventDispatched(AWTEvent e)
    {
        if (e instanceof MouseEvent && e.getSource() instanceof JComponent)
        {
            if (SwingUtilities.isDescendingFrom(
                (Component) e.getSource(), parent))
            {
            	synchronized (this) {
					
				
                MouseEvent m = (MouseEvent) e;
                if (m.getID() == MouseEvent.MOUSE_ENTERED)
                {
                    if (hasExited)
                    {
                        System.out.println("Entered...");
                        hasExited = false;
                        mouseListener.mouseEntered((MouseEvent) e);
                    }
                } else if (m.getID() == MouseEvent.MOUSE_EXITED)
                {
                    Point p = SwingUtilities.convertPoint(
                        (Component) e.getSource(),
                        m.getPoint(),
                        innerBound);
                    if (!hasExited&&!innerBound.getBounds().contains(p))
                    {
                    	System.out.println("hasExited:"+hasExited);
                        System.out.println("Exited...");
                        hasExited = true;
                        mouseListener.mouseExited((MouseEvent) e);
                    }
                }else if(m.getID()==MouseEvent.MOUSE_RELEASED){
                    mouseListener.mouseReleased((MouseEvent) e);

                }
            	}
            }
        }
    }
}