package cc.translate;

public class Config {
	public static boolean playEn = true;
	public static boolean playCn = false;
	public static boolean registerHook = true;
	public static boolean usingSystemProxy = true;
	public static boolean cacheHistory = true;
	public static boolean copyResultToClip = true;
	public static ShowLocation showWhere = ShowLocation.FOLLOW_MOUSE;
	private Config() {};
	
	public enum ShowLocation{
		FOLLOW_MOUSE,
		TOP_RIGHT
	}
	

}

