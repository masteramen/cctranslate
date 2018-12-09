package cc.translate;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.ligboy.translate.Translate;
import org.ligboy.translate.exception.IllegalTokenKeyException;
import org.ligboy.translate.exception.RetrieveTokenKeyFailedException;
import org.ligboy.translate.exception.TranslateFailedException;
import org.ligboy.translate.model.TranslateResult;

import javazoom.jl.decoder.JavaLayerException;

public class Cc implements NativeKeyListener, NativeMouseInputListener {
	private static ProxySelector defaultProxySelector;
	private boolean ctrlKeyDown = false;
	private int cCount = 0;
	private int unregisterNativeHook = 0;

	private static Cc instance = null;

	public static Cc getCcInstance() {
		if (instance == null) {
			instance = new Cc();
		}
		return instance;
	}

	public void nativeKeyPressed(NativeKeyEvent e) {

		if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL || e.getKeyCode() == NativeKeyEvent.VC_META) {
			this.ctrlKeyDown = true;
		}
		if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {

			synchronized (this) {
				if (this.unregisterNativeHook <= 0) {
					this.unregisterNativeHook = 1;
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								int tmp = 0;
								do {
									System.out.println("unregisterNativeHook:" + unregisterNativeHook);
									tmp = unregisterNativeHook;
									Thread.sleep(10000);
								} while (unregisterNativeHook > tmp);
								unRegistHook();
								registHook();
								synchronized (Cc.this) {
									unregisterNativeHook = 0;
								}

							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}).start();
				} else {
					this.unregisterNativeHook += 1;
				}

			}

		}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL || e.getKeyCode() == NativeKeyEvent.VC_META) {
			this.ctrlKeyDown = false;
		}
		if (ctrlKeyDown && "C".equals(NativeKeyEvent.getKeyText(e.getKeyCode()))) {
			this.cCount += 1;

		} else {
			this.cCount = 0;
		}
		if (this.cCount >= 2) {
			this.cCount = 0;
			// System.out.println("Key Released: " +
			// NativeKeyEvent.getKeyText(e.getKeyCode()));
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					try {

						Utils.notify(Utils.getgetSystemClipboardData());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}

	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		// System.out.println("Key Typed: " +
		// NativeKeyEvent.getKeyText(e.getKeyCode()));
	}

	public static void main(String[] args) throws URISyntaxException {
		initNetWork();
		initGUI();
		registHook();

	}

	private static void initGUI() {
		// System.setProperty("java.awt.headless", "true");
		System.setProperty("apple.awt.UIElement", "true");

		UIManager.put("swing.boldMetal", Boolean.FALSE);
		// Schedule a job for the event-dispatching thread:
		// adding TrayIcon.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void registHook() {
		LogManager.getLogManager().reset();

		// Get the logger for "org.jnativehook" and set the level to off.
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		try {
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeKeyListener(Cc.getCcInstance());
			GlobalScreen.addNativeMouseMotionListener(Cc.getCcInstance());
			Config.registerHook = true;

		} catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

		}
		System.out.println("regist Hook");

	}

	private static void unRegistHook() {
		try {
			GlobalScreen.removeNativeKeyListener(Cc.getCcInstance());
			GlobalScreen.removeNativeMouseMotionListener(Cc.getCcInstance());
			GlobalScreen.unregisterNativeHook();
			Config.registerHook = false;
			System.out.println("unRegist Hook");
		} catch (NativeHookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void initNetWork() {
		System.setProperty("java.net.useSystemProxies", "true");
		defaultProxySelector = ProxySelector.getDefault();
		ProxySelector.setDefault(new ProxySelector() {
			@Override
			public List<Proxy> select(URI uri) {
				List<Proxy> list = new ArrayList<>();
				try {
					if (Config.usingSystemProxy) {
						List<Proxy> l = defaultProxySelector.select(new URI("http://translate.google.cn"));
						for (Iterator iter = l.iterator(); iter.hasNext();) {
							Proxy proxy = (Proxy) iter.next();

							InetSocketAddress addr = (InetSocketAddress) proxy.address();
							if (addr != null) {
								System.out.println("proxy hostname : " + addr.getHostName());
								System.out.println("proxy port : " + addr.getPort());
								list.add(proxy);
							}

						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				list.add(Proxy.NO_PROXY);

				return list;
			}

			@Override
			public void connectFailed(URI uri, SocketAddress sa, IOException ioe) { // 简单地打印信息
				// TODO Auto-generated method stub
				System.out.println("无法连接到代理！");
			}

		});
	}

	public static TranslateResult translate(String raw)
			throws TranslateFailedException, IllegalTokenKeyException, RetrieveTokenKeyFailedException {
		final Translate translate = new Translate.Builder().logLevel(Translate.LogLevel.HEADERS)
				.proxySelector(new ProxySelector() {

					@Override
					public List<Proxy> select(URI uri) {
						List<Proxy> list = new ArrayList<>();

						try {
							if (Config.usingSystemProxy) {
								List<Proxy> l = defaultProxySelector.select(new URI("http://translate.google.cn"));
								for (Iterator iter = l.iterator(); iter.hasNext();) {
									Proxy proxy = (Proxy) iter.next();

									InetSocketAddress addr = (InetSocketAddress) proxy.address();
									if (addr != null) {
										list.add(proxy);
									}

								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						list.add(Proxy.NO_PROXY);

						return list;
					}

					@Override
					public void connectFailed(URI uri, SocketAddress sa, IOException ioe) { // 简单地打印信息
						// TODO Auto-generated method stub
						System.out.println("无法连接到代理！");
					}

				}).build();
		String targetLanguage = isChinese(raw) ? "en" : "zh_CN";
		TranslateResult result = translate.translate(raw, Translate.SOURCE_LANG_AUTO,targetLanguage );
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// lyric = split.filter((s, i) => i % 2 === 0).map(x => x.match(/^\[\d+/) ?
					// x.replace(/([a-z]+)/gi, '<span>$1</span>') : x).join('\n')
					if(Config.playEn) {
						String[] ns = (targetLanguage.equals("zh_CN")?raw:result.getTargetText()).replaceAll("([.?!])[\\s\\n]+", "$1\n").split("\n+");
						for (int i = 0; i < ns.length; i++) {
							if (ns[i].trim().length() > 0)
								translate.playTextAudio(ns[i],"en");
						}
					}
					if(Config.playCn) {
						String[] ns = (targetLanguage.equals("en")?raw:result.getTargetText()).replaceAll("([.?!])[\\s\\n]+", "$1\n").split("\n+");
						for (int i = 0; i < ns.length; i++) {
							if (ns[i].trim().length() > 0)
								translate.playTextAudio(ns[i],"zh_CN");
						}
					}
					} catch (RetrieveTokenKeyFailedException | IllegalTokenKeyException | IOException
						| JavaLayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		if(Config.copyResultToClip) {
			Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();  
	        Transferable tText = new StringSelection(result.getTargetText());  
	        clip.setContents(tText, null);
		}
		return result;

	}

	// 判断一个字符是否是中文
	public static boolean isChinese(char c) {
		return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
	}

	// 判断一个字符串是否含有中文
	public static boolean isChinese(String str) {
		if (str == null)
			return false;
		for (char c : str.toCharArray()) {
			if (isChinese(c))
				return true;// 有一个中文字符就返回
		}
		return false;
	}

	private static void createAndShowGUI() {
		// Check the SystemTray support
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		final PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(createImage("/cc.gif", "CC Translate"));
		final SystemTray tray = SystemTray.getSystemTray();

		/*
		 * Runtime.getRuntime().addShutdownHook(new Thread() {
		 * 
		 * public void run() { try { if (SystemTray.isSupported()) { SystemTray
		 * systemTray = SystemTray.getSystemTray(); systemTray.remove(trayIcon);
		 * Runtime.getRuntime().halt(0); } } catch (Exception e) { e.printStackTrace();
		 * } } });
		 */

		// Create a popup menu components
		MenuItem aboutItem = new MenuItem("关于CC翻译");
		// CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
		// CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
		CheckboxMenuItem cbUsingSystemProxy = new CheckboxMenuItem("使用系统代理");
		cbUsingSystemProxy.setState(Config.usingSystemProxy);
		CheckboxMenuItem cbRegistHook = new CheckboxMenuItem("启用 CC快捷键");
		cbRegistHook.setState(Config.registerHook);
		
		CheckboxMenuItem cbResultToClip = new CheckboxMenuItem("复制结果到粘贴板");
		cbResultToClip.setState(Config.copyResultToClip);

		/*
		 * Menu displayMenu = new Menu("Display"); MenuItem errorItem = new
		 * MenuItem("Error"); MenuItem warningItem = new MenuItem("Warning"); MenuItem
		 * infoItem = new MenuItem("Info"); MenuItem noneItem = new MenuItem("None");
		 */
		MenuItem exitItem = new MenuItem("退出");

		// Add components to popup menu
		popup.add(aboutItem);
		popup.addSeparator();
		// popup.add(cb1);
		// popup.add(cb2);
		popup.add(cbUsingSystemProxy);
		popup.add(cbRegistHook);
		popup.add(cbResultToClip);
		
		
				
		Menu playMenu = new Menu("播放语音");
		CheckboxMenuItem enItem = new CheckboxMenuItem("English");
		enItem.setState(Config.playEn);
		CheckboxMenuItem cnItem = new CheckboxMenuItem("中文");
		cnItem.setState(Config.playCn);
		
		ItemListener cbListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getSource()==enItem) Config.playEn = e.getStateChange() == ItemEvent.SELECTED;
				if(e.getSource()==cnItem) Config.playCn = e.getStateChange() == ItemEvent.SELECTED;
				if(e.getSource()==cbRegistHook) Config.registerHook = e.getStateChange() == ItemEvent.SELECTED;
				if(e.getSource()==cbUsingSystemProxy) Config.usingSystemProxy = e.getStateChange() == ItemEvent.SELECTED;
				if(e.getSource()==cbResultToClip) Config.copyResultToClip = e.getStateChange() == ItemEvent.SELECTED;

			}
		};
		
		popup.add(playMenu);

		playMenu.add(enItem);
		playMenu.add(cnItem);
		cbUsingSystemProxy.addItemListener(cbListener);
		enItem.addItemListener(cbListener);
		cnItem.addItemListener(cbListener);
		cbResultToClip.addItemListener(cbListener);
		popup.addSeparator();

		/*
		 * popup.add(displayMenu); displayMenu.add(errorItem);
		 * displayMenu.add(warningItem); displayMenu.add(infoItem);
		 * displayMenu.add(noneItem);
		 */
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

//		JPopupMenu jpopup = new JPopupMenu();
//	    JMenuItem javaCupMI = new JMenuItem("Example", new ImageIcon("cc.gif"));
//	    jpopup.add(javaCupMI);
//
//	    jpopup.addSeparator();
//
//	    JMenuItem exitMI = new JMenuItem("Exit");
//	    jpopup.add(exitMI);
//	    trayIcon.addMouseListener(new MouseAdapter() {
//	        public void mouseReleased(MouseEvent e) {
//	            if (e.isPopupTrigger()) {
//	                jpopup.setLocation(e.getX(), e.getY());
//	                jpopup.setInvoker(jpopup);
//	                jpopup.setVisible(true);
//	            }
//	        }
//	    });
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
			return;
		}

		/*
		 * trayIcon.addMouseListener(new MouseAdapter() { public void
		 * mouseClicked(MouseEvent e) { if (e.getClickCount() == 1) { try {
		 * System.out.println("Click"+e.getLocationOnScreen());
		 * System.out.println(""+e.getSource()); Robot robot = new Robot(); // RIGHT
		 * CLICK robot.mousePress(InputEvent.BUTTON3_MASK);
		 * robot.mouseRelease(InputEvent.BUTTON3_MASK); } catch (AWTException e1) { //
		 * TODO Auto-generated catch block e1.printStackTrace(); }
		 * 
		 * } } });
		 */

		trayIcon.addMouseListener(new MouseAdapter() {

			private boolean leftPressed;
			private boolean rightPressed;
			private JDialog dialog;

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					leftPressed = true;
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					rightPressed = true;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				boolean leftWasPressed = leftPressed;
				boolean rightWasPressed = rightPressed;
				if (e.getButton() == MouseEvent.BUTTON1) {
					leftPressed = false;
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					rightPressed = false;
				}
				if ((e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3)
						&& !(leftWasPressed && rightWasPressed)) {
					if (dialog != null)
						dialog.dispose();
					dialog = new JDialog();
					dialog.setUndecorated(true);
					dialog.setVisible(true);
					dialog.add(popup);
					popup.show(dialog, e.getXOnScreen(), e.getYOnScreen());
				}
			}
		});
		/*
		 * trayIcon.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { JOptionPane.showMessageDialog(null,
		 * "This dialog box is run from System Tray"); } });
		 */

		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "CC translate:简单易用的翻译小工具");
			}
		});

		cbUsingSystemProxy.setState(true);

		trayIcon.setImageAutoSize(true);
		trayIcon.setToolTip("CC 翻译");

		/*
		 * errorItem.addActionListener(listener);
		 * warningItem.addActionListener(listener);
		 * infoItem.addActionListener(listener); noneItem.addActionListener(listener);
		 */
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon);
				System.exit(0);
			}
		});
		trayIcon.displayMessage("CC翻译", "CC翻译已经准备好了，欢迎使用。", TrayIcon.MessageType.INFO);

	}

	// Obtain the image URL
	protected static Image createImage(String path, String description) {
		URL imageURL = Cc.class.getResource(path);

		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else {
			return (new ImageIcon(imageURL, description)).getImage();
		}
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent nativeEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nativeMousePressed(NativeMouseEvent nativeEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent nativeEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent nativeEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent nativeEvent) {
		// TODO Auto-generated method stub

	}

	public static void windowsService(String args[]) throws Exception {
		String cmd = "start";
		if (args.length > 0) {
			cmd = args[0];
		}

		if ("start".equals(cmd)) {
			main(new String[] {});
		} else {
			System.exit(0);
		}
	}

}