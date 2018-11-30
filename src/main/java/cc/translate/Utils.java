package cc.translate;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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
	
	public static String getTk(String text,String token) throws Exception{
		PipedReader prd = new PipedReader();
		PipedWriter pwt = new PipedWriter(prd);
		//设置执行结果内容的输出流
		ScriptEngineManager m = new ScriptEngineManager();
		//获取JavaScript执行引擎
		ScriptEngine engine = m.getEngineByName("JavaScript");
		engine.getContext().setWriter(pwt);
		//js文件的路径
		InputStream in = Utils.class.getClass().getResourceAsStream("/tk.js"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		engine.eval(reader);

		Invocable inv = (Invocable) engine;
		// call function from script file
		return inv.invokeFunction("calcHash", text,token).toString();
	}
}
