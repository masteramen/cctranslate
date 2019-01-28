package cc.translate;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.channels.FileLock;
 
public class CcSplash  implements Runnable {
	private static JProgressBar progressBar;
	private static JDialog splashDialog;
	private static JLabel titleLabel;
	private static JLabel closeLabel;
	Thread splashThread; // 进度条更新线程

	public CcSplash() {

	}
 
	public void start() {
		splashThread = new Thread(this); // 实例化线程
		splashThread.start(); // 开始运行线程
	}
 
	public void run() {
		URL url = getClass().getResource("/ccsplash.jpg"); // 图片的位置
		if(CcSplash.splashDialog==null)  {
			CcSplash.splashDialog = new JDialog();
			CcSplash.progressBar = new JProgressBar(1, 100);
			Container container = splashDialog.getContentPane(); // 得到容器
			splashDialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // 设置光标
			if (url != null) {
				container.add(new JLabel(new ImageIcon(url)), BorderLayout.CENTER); // 增加图片
			}
			container.add(progressBar, BorderLayout.SOUTH); // 增加进度条到容器上
			
			Dimension screen = splashDialog.getToolkit().getScreenSize(); // 得到屏幕尺寸
			splashDialog.pack(); // 窗口适应组件尺寸
			splashDialog.toFront(); // 窗口前端显示
			splashDialog.setAlwaysOnTop(true);
			splashDialog.setLocation((screen.width - splashDialog.getSize().width) / 2,(screen.height - splashDialog.getSize().height) / 2); // 设置窗口位置
			splashDialog.setVisible(true); // 显示窗口
		}

		progressBar.setString("加载程序中,请稍候......"); // 设置显示文字

	    FileLock lck;
		try {
			lck = new FileOutputStream("cc.lock").getChannel().tryLock();
			 if(lck == null) {
				 progressBar.setString("A previous instance is already running.");
				if(CcSplash.closeLabel!=null) CcSplash.closeLabel.addMouseListener(new MouseAdapter() {
			    	  @Override
			    	public void mouseClicked(MouseEvent e) {
			    		// TODO Auto-generated method stub
			    		super.mouseClicked(e);
					      System.exit(1);
			    	}
				});
			      Thread.sleep(10000);
			      System.exit(1);
			    }			 

		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}         
	   		try {
			for (int i = 0; i < 100; i++) {
				Thread.sleep(30); // 线程休眠
				progressBar.setValue(progressBar.getValue() + 1); // 设置进度条值
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	   		splashDialog.dispose(); // 释放窗口
		try {
			Cc.main(new String[] {});
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 
	 
	public static void main(String[] args) {
		System.setProperty("apple.awt.UIElement", "true");
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				CcSplash splash = new CcSplash();
				splash.start(); // 运行启动界面
				
			}
		});
	}
	
	public static void initSplashComponents(JDialog splashDialog,JProgressBar progressBar,JLabel titleLabel,JLabel closeLabel,String[] args){
		
		CcSplash.progressBar = progressBar;
		CcSplash.splashDialog = splashDialog;
		CcSplash.titleLabel = titleLabel;
		CcSplash.closeLabel = closeLabel;
		main(args);
		
	}
}
 
