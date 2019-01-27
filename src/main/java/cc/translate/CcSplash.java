package cc.translate;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.channels.FileLock;
 
public class CcSplash  implements Runnable {
	Thread splashThread; // 进度条更新线程
	JProgressBar progress; // 进度条
	JWindow window = new JWindow();
	public CcSplash() {
		Container container = window.getContentPane(); // 得到容器
		window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // 设置光标
		URL url = getClass().getResource("/ccsplash.jpg"); // 图片的位置
		if (url != null) {
			container.add(new JLabel(new ImageIcon(url)), BorderLayout.CENTER); // 增加图片
		}
		progress = new JProgressBar(1, 100); // 实例化进度条
		progress.setStringPainted(true); // 描绘文字
		progress.setString("加载程序中,请稍候......"); // 设置显示文字
		progress.setBackground(Color.white); // 设置背景色
		container.add(progress, BorderLayout.SOUTH); // 增加进度条到容器上
 
		Dimension screen = window.getToolkit().getScreenSize(); // 得到屏幕尺寸
		window.pack(); // 窗口适应组件尺寸
		window.setAlwaysOnTop(true);
		window.setLocation((screen.width - window.getSize().width) / 2,
				(screen.height - window.getSize().height) / 2); // 设置窗口位置
	}
 
	public void start() {
		window.toFront(); // 窗口前端显示
		splashThread = new Thread(this); // 实例化线程
		splashThread.start(); // 开始运行线程
	}
 
	public void run() {
		window.setVisible(true); // 显示窗口
	    FileLock lck;
		try {
			lck = new FileOutputStream(System.getProperty("user.home")+File.separator+".cc.lock").getChannel().tryLock();
			 if(lck == null) {
			      progress.setString("A previous instance is already running.");
			      window.getContentPane().addMouseListener(new MouseAdapter() {
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
				progress.setValue(progress.getValue() + 1); // 设置进度条值
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	   		window.dispose(); // 释放窗口
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
}
 
