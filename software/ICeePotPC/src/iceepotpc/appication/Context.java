package iceepotpc.appication;

import java.util.ArrayList;

/**
 * @author tsantakis
 * Class that keeps the application settings and various constants
 * accessible from everywhere within the app classes *
 */
public class Context {
	
	
	/**
	 *  arrayList keeping the descriptions of all monitored pots (the tabs to be generated)
	 */
	public static ArrayList<Pot> potDescrs = new ArrayList<Pot>();
	
	
	/**
	 * server configuration (for opening the connection) 
	 */
	public static String serverHost = "homeplants.dyndns.org";
	public static int serverPort = 3629;
	
	public static boolean isDebug = true;

}
