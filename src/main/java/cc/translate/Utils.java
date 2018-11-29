package cc.translate;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import dorkbox.notify.Notify;
import dorkbox.notify.Pos;
import dorkbox.util.ActionHandler;

public class Utils {

	public static void notify(String message){

        Notify notify = Notify.create()
         .title("翻译..." )
         .text(message)
         .hideAfter(5000)
         .position(Pos.TOP_RIGHT)
         //     .setScreen(0)
         .darkStyle()
         // .shake(1300, 4)
         //.image(ImageIO.read(NotifyTest.class.getResource("/cc.gif")))
         //.hideCloseButton()
         .onAction(new ActionHandler<Notify>() {
             @Override
             public
             void handle(final Notify arg0) {
                 System.err.println("Notification 1 clicked on!");
             }
         });
        notify.show();
        

        new Thread(new Runnable() {
			
			@Override
			public void run() {
				String result = "";
				try {
					 result = Cc.translate(message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					 result = "error!";
				}
				notify.text(result).title("翻译完毕");

				notify.updateUI();
			}
		}).start();

	}
	
	public static String getgetSystemClipboardData(){
		String data = "";
		try {
			data = (String) Toolkit.getDefaultToolkit()
			        .getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (HeadlessException | UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		} 
		return data;
	}
}
