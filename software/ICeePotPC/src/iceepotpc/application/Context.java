package iceepotpc.application;

import java.io.File;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Observer;

/**
 * @author tsantakis Class that keeps the application settings and various
 *         constants accessible from everywhere within the app classes *
 */
public class Context {

	private static Context instance = null;

	/**
	 * arrayList keeping the descriptions of all monitored pots (the tabs to be
	 * generated)
	 */
	private ArrayList<Pot> pots = new ArrayList<Pot>();

	
	/**
	 * server configuration (for opening the connection)
	 */
	private String serverHost = "";
	private int serverPort = 0;
	private int serverTimeout = 15000;

	private ArrayList<Observer> ObserversForNewPots = null;

	
	/** Creator that returns the (one and only) Context singleton
	 * object. 
	 * @return the Context object
	 * @throws Exception
	 */
	public static Context getInstance() throws Exception {
		if (instance == null) {
			instance = new Context();
			return instance;
		} else {
			return instance;
		}
	}

	/**
	 * constructor which reads the settings xml file and applies the
	 * configuration to Context variables
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	private Context() throws Exception {

		File f = new File("settings.xml");
		if (!f.exists())
			try {
				Settings.createSettings(this);
			} catch (Exception e) {
				throw new Exception("Error in context constructor - "
						+ e.getMessage());
			}

		try {
			Settings.fetchSettings(this);
		} catch (Exception e) {
			throw new Exception("Error in context constructor - "
					+ e.getMessage());
		}

	}

	// getters
	public ArrayList<Pot> getPots() {
		return pots;
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

	public Pot getPotById(int id) {

		Pot p = null;
		for (int i = 0; i < pots.size(); i++)
			if (pots.get(i).getId() == id)
				p = pots.get(i);
		return p;

	}

	// setters
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
	 * 
	 * @param p
	 *            : the pot to be added to the context & to the settings file
	 * @throws Exception
	 */
	public void addPot(Pot p) throws Exception {
		pots.add(p);
		try {
			Settings.addPotToSettings(p);
		} catch (Exception e) {
			throw new Exception("Error in context add pot - " + e.getMessage());
		}

		// notify the observers
		if (ObserversForNewPots != null)
			for (int i = 0; i < ObserversForNewPots.size(); i++)
				ObserversForNewPots.get(i).update(null, p.getId());
	}

	/**
	 * helper method to be called when UI wants to update the server
	 * information, Context is updated & settings file is modified
	 * 
	 * @param hostName
	 * @param port
	 * @throws Exception
	 */
	public void updateServer(String hostName, int port, int timeOut)
			throws Exception {

		serverHost = hostName;
		serverPort = port;
		serverTimeout = timeOut;

		try {
			Settings.updateServerSettings(this);
		} catch (Exception e) {
			throw new Exception("Error in context update server - "
					+ e.getMessage());
		}
	}

	/** Method that updates the moisture min/max levels of a pot
	 * both in the context variables and in the settings xml file
	 * @param id: the id of the pot to be updated
	 * @param newMinValue
	 * @param newMaxValue
	 * @throws Exception
	 */
	public void updateMoistLimits(int id, double newMinValue, double newMaxValue)
			throws Exception {
		try {
			Settings.modifyMinMoistValue(id, newMinValue, newMaxValue);
		} catch (Exception e) {
			throw new Exception("Error in context update limits - "
					+ e.getMessage());
		}
	}

	/** method called in order for new UI elements to be register 
	 * for being notified when the Context settings will change
	 * @param obs: the class implementing the Observer Interface
	 */
	public void registerObserverForNewPots(Observer obs) {
		ObserversForNewPots.add(obs);
	}

}
