package cc.translate;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URL;

import fr.jcgay.notification.Application;
import fr.jcgay.notification.Icon;
import fr.jcgay.notification.Notification;
import fr.jcgay.notification.Notifier;
import fr.jcgay.notification.SendNotification;

public class Notify {

	public static void notify(String message){
        URL icon = Notify.class.getResource("/dialog-clean.png");

        Application application = Application.builder()
            .id("notify-example")
            .name("Notify Example")
            .icon(Icon.create(icon, "app"))
            .build();

        Notifier notifier = new SendNotification()
            .setApplication(application)
            .setChosenNotifier("notify")
            .initNotifier();

        Notification notification = Notification.builder()
            .title("Notify Notification")
            .message(message)
            .icon(Icon.create(icon, "ok"))
            .build();

        notifier.send(notification);
        notifier.close();
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
