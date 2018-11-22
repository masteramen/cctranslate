package cc.translate;


import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
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
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.ligboy.translate.Translate;
import org.ligboy.translate.exception.IllegalTokenKeyException;
import org.ligboy.translate.exception.RetrieveTokenKeyFailedException;
import org.ligboy.translate.exception.TranslateFailedException;
import org.ligboy.translate.model.TranslateResult;

public class CcTranslator implements NativeKeyListener {
	private boolean ctrlKeyDown = false;
	private int cCount = 0;
	public void nativeKeyPressed(NativeKeyEvent e) {
		//System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
		if(e.getKeyCode()==NativeKeyEvent.VC_CONTROL){
			this.ctrlKeyDown = true;
		}else if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
			try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		
		if(e.getKeyCode()==NativeKeyEvent.VC_CONTROL){
			this.ctrlKeyDown = false;
		} 
		if(ctrlKeyDown && "C".equals(NativeKeyEvent.getKeyText(e.getKeyCode()))){
			this.cCount  +=1;
			
		}
		if(this.cCount>=2){
			this.cCount = 0;
			//System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
			SwingUtilities.invokeLater(new Runnable() {
				
				 public void run() {
						try {
							Notify.notify(translate(Notify.getgetSystemClipboardData()));
						} catch (RetrieveTokenKeyFailedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TranslateFailedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalTokenKeyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				 }
				});
		}

	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		//System.out.println("Key Typed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
	}

	public static void main(String[] args) {
		
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event-dispatching thread:
        //adding TrayIcon.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        LogManager.getLogManager().reset();

     // Get the logger for "org.jnativehook" and set the level to off.
     Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
     logger.setLevel(Level.OFF);
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

		GlobalScreen.addNativeKeyListener(new CcTranslator());
	

	}
	
	public String translate(String raw) throws RetrieveTokenKeyFailedException, TranslateFailedException, IllegalTokenKeyException, URISyntaxException {
		List <String> times = new ArrayList();
		System.setProperty("java.net.useSystemProxies","true");
		List<Proxy> l = ProxySelector.getDefault().select(new URI("http://java.sun.com/"));
		Proxy theProxy = null;
		for (Iterator iter = l.iterator(); iter.hasNext(); )  {
		  Proxy proxy = (Proxy) iter.next();
		  InetSocketAddress addr = (InetSocketAddress)proxy.address();
		  if(addr!=null){
			  System.out.println("proxy hostname : " + addr.getHostName());
			  System.out.println("proxy port : " + addr.getPort());  
		  }
		  theProxy = new Proxy(Proxy.Type.HTTP,  new InetSocketAddress("192.168.1.28", 8080));

		}
		System.out.println(theProxy);

			//HttpsClient.testIt();
		  theProxy = new Proxy(Proxy.Type.HTTP,  new InetSocketAddress("192.168.1.28", 8080));

		System.setProperty("java.net.useSystemProxies","false");
		
	

		final Translate translate = new Translate.Builder()
	            .logLevel(Translate.LogLevel.BODY)
	            .proxy(Proxy.NO_PROXY)
	            .build();
	    
	        translate.refreshTokenKey();
	        TranslateResult result = translate.translate(raw,Translate.SOURCE_LANG_AUTO, isChinese(raw)?"en":"zh_CN");
			return result.getTargetText();
	        
	}
	// 判断一个字符是否是中文
    public static boolean isChinese(char c) {
        return c >= 0x4E00 &&  c <= 0x9FA5;// 根据字节码判断
    }
    // 判断一个字符串是否含有中文
    public static boolean isChinese(String str) {
        if (str == null) return false;
        for (char c : str.toCharArray()) {
            if (isChinese(c)) return true;// 有一个中文字符就返回
        }
        return false;
    }


    private static void createAndShowGUI() {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(createImage("/cc.gif", "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();
        
        // Create a popup menu components
        MenuItem aboutItem = new MenuItem("About");
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        Menu displayMenu = new Menu("Display");
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warning");
        MenuItem infoItem = new MenuItem("Info");
        MenuItem noneItem = new MenuItem("None");
        MenuItem exitItem = new MenuItem("Exit");
        
        //Add components to popup menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.add(cb2);
        popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        displayMenu.add(noneItem);
        popup.add(exitItem);
        
        trayIcon.setPopupMenu(popup);
        
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }
        
        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from System Tray");
            }
        });
        
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from the About menu item");
            }
        });
        
        cb1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int cb1Id = e.getStateChange();
                if (cb1Id == ItemEvent.SELECTED){
                    trayIcon.setImageAutoSize(true);
                } else {
                    trayIcon.setImageAutoSize(false);
                }
            }
        });
        
        cb2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int cb2Id = e.getStateChange();
                if (cb2Id == ItemEvent.SELECTED){
                    trayIcon.setToolTip("Sun TrayIcon");
                } else {
                    trayIcon.setToolTip(null);
                }
            }
        });
        
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuItem item = (MenuItem)e.getSource();
                //TrayIcon.MessageType type = null;
                System.out.println(item.getLabel());
                if ("Error".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.ERROR;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an error message", TrayIcon.MessageType.ERROR);
                    
                } else if ("Warning".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.WARNING;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is a warning message", TrayIcon.MessageType.WARNING);
                    
                } else if ("Info".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.INFO;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an info message", TrayIcon.MessageType.INFO);
                    
                } else if ("None".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.NONE;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an ordinary message", TrayIcon.MessageType.NONE);
                }
            }
        };
        
        errorItem.addActionListener(listener);
        warningItem.addActionListener(listener);
        infoItem.addActionListener(listener);
        noneItem.addActionListener(listener);
        
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });
    }
    
    //Obtain the image URL
    protected static Image createImage(String path, String description) {
        URL imageURL = CcTranslator.class.getResource(path);
        
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}