package iceepotpc.application;



import java.io.File;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Observer;






/**
 * @author tsantakis
 * Class that keeps the application settings and various constants
 * accessible from everywhere within the app classes *
 */
public class Context{
	
	private static Context instance = null;
	
	/**
	 *  arrayList keeping the descriptions of all monitored pots (the tabs to be generated)
	 */
	private ArrayList<Pot> potDescrs = new ArrayList<Pot>();
	
	
	/**
	 * server configuration (for opening the connection) 
	 */
	private String serverHost = "";
	private int serverPort = 0;
	private int serverTimeout = 15000;
	

	private ArrayList<Observer> ObserversForNewPots = null;
	
	public static Context getInstance() throws Exception{
		if(instance == null){
			instance = new Context();
			return instance;
		}else{
			return instance;
		}
	}
	
	/**
	 * constructor which reads the settings xml file and applies the
	 * configuration to Context variables
	 * @throws Exception 
	 * @throws IOException 
	 */
	private Context() throws Exception  {
		
		File f = new File("settings.xml");
		if(!f.exists())
			try {
				Settings.createSettings(this);
			} catch (Exception e) {
				throw new Exception("Error in context constructor - "+e.getMessage());
			}
		
		
			try {
				Settings.fetchSettings(this);
			} catch (Exception e){
				throw new Exception("Error in context constructor - "+e.getMessage());
			}
		
	}
	
	
	public ArrayList<Pot> getPotDescrs() {
		return potDescrs;
	}

	

	public String getServerHost() {
		return serverHost;
	}

	public int getServerPort() {
		return serverPort;
	}
	

	public int getServerTimeout() {
		return serverTimeout;
	}

	public void setPotDescrs(ArrayList<Pot> potDescrs) {
		this.potDescrs = potDescrs;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public void setServerTimeout(int serverTimeout) {
		this.serverTimeout = serverTimeout;
	}

	public void setObserversForNewPots(ArrayList<Observer> observersForNewPots) {
		ObserversForNewPots = observersForNewPots;
	}

	/**
	 * helper method to be called when UI is to create a new pot
	 * @param p: the pot to be added to the context & to the settings file
	 * @throws Exception 
	 */
	public void addPot(Pot p) throws Exception{
		potDescrs.add(p);
		try {
			Settings.addPotToSettings(p);
		} catch (Exception e) {
			throw new Exception("Error in context add pot - "+e.getMessage());
		}
		
		//notify the observers
		if(ObserversForNewPots != null)
			for(int i=0; i<ObserversForNewPots.size(); i++)
				ObserversForNewPots.get(i).update(null, potDescrs.size()-1);
	}
	
	
	
	/** helper method to be called when UI wants to update the server
	 * information, Context is updated & settings file is modified 
	 * @param hostName
	 * @param port
	 * @throws Exception 
	 */
	public void updateServer(String hostName, int port, int timeOut) throws Exception{
		
		serverHost = hostName;
		serverPort = port;
		serverTimeout = timeOut;
		
		try {
			Settings.updateServerSettings(this);
		} catch (Exception e) {
			throw new Exception("Error in context update server - "+e.getMessage());
		}
	}
	
	
	/** 
	 * @param pin
	 * @param newMinValue
	 * @param newMaxValue
	 * @throws Exception
	 */
	public void updateMoistLimits(int pin, double newMinValue, double newMaxValue) throws Exception{
		try {
			Settings.modifyMinMoistValue(pin, newMinValue, newMaxValue);
		} catch (Exception e) {
			throw new Exception("Error in context update limits - "+e.getMessage());
		}
	}
	
	public void registerObserverForNewPots(Observer obs){
		ObserversForNewPots.add(obs);
	}

}
