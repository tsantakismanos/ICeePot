package iceepotpc.appication;



import java.io.File;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Observer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



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
	private int serverPort = -1;
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
	 * @throws IOException 
	 */
	private Context() throws Exception {
		
		File f = new File("settings.xml");
		if(!f.exists())
			createSettings();
		
		fetchSettings();
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

	/**
	 * helper method to be called when UI is to create a new pot
	 * @param p: the pot to be added to the context & to the settings file
	 * @throws Exception 
	 */
	public void addPot(Pot p) throws Exception{
		potDescrs.add(p);
		addPotToSettings(p);
		
		//notify the observers
		if(ObserversForNewPots != null)
			for(int i=0; i<ObserversForNewPots.size(); i++)
				ObserversForNewPots.get(i).update(null, potDescrs.size()-1);
	}
	
	/**Helper method to be called from UI when the minimum
	 * moisture value for a pot has changed	 * 
	 * @param isMinimum: the same method is to be used for min and max values
	 * @param pin: the pin of the pot whose minimum value has changed
	 * @param newValue: of the moisture level
	 * @throws Exception 
	 */
	public void modifyMinMoistValue(boolean isMinimum, int pin, double newValue) throws Exception{
		Document dom;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.parse("settings.xml");
			
			
			//set the value
			if(isMinimum){
				NodeList nlstServer = dom.getElementsByTagName("minMoist");
				nlstServer.item(0).setTextContent(String.valueOf(newValue));
			}else{
				NodeList nlstServer = dom.getElementsByTagName("maxMoist");
				nlstServer.item(0).setTextContent(String.valueOf(newValue));
			}
								
		
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			StreamResult result = new StreamResult(new File("settings.xml"));
			transformer.transform(source, result);
			
			
			
		} catch (Exception e){
			throw e;
		}
	}
	
	/** helper method to be called when UI wants to update the server
	 * information, Context is updated & settings file is modified 
	 * @param hostName
	 * @param port
	 * @throws Exception 
	 */
	/**
	 * @param hostName
	 * @param port
	 * @throws Exception
	 */
	public void updateServer(String hostName, int port, int timeOut) throws Exception{
		
		serverHost = hostName;
		serverPort = port;
		serverTimeout = timeOut;
		
		updateServerSettings();
	}
	
	/**
	 * helper method which stores the context variables to the settings file
	 *  @param p: the pot to be added to the settings file 
	 * @throws Exception 
	 */
	private void addPotToSettings(Pot p) throws Exception{
		Document dom;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.parse("settings.xml");
			
			//get the root element
			NodeList nRoot = dom.getElementsByTagName("settings");
			
			Element elDescr = dom.createElement("descr");
			elDescr.setTextContent(p.getDescr());
			
			Element elPin = dom.createElement("pin");
			elPin.setTextContent(String.valueOf(p.getPin()));
			
			Element elMinMoist = dom.createElement("minMoist");
			elMinMoist.setTextContent(String.valueOf(p.getMinMoistVal()));
			
			Element elMaxMoist = dom.createElement("maxMoist");
			elMaxMoist.setTextContent(String.valueOf(p.getMaxMoistVal()));
			
			Node elPot = dom.createElement("pot");
			
			elPot.appendChild(elDescr);
			elPot.appendChild(elPin);
			elPot.appendChild(elMinMoist);
			elPot.appendChild(elMaxMoist);
			
			nRoot.item(0).appendChild(elPot);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			StreamResult result = new StreamResult(new File("settings.xml"));
			transformer.transform(source, result);
			
		} catch (Exception e){
			throw e;
		}
	}
	
	
	/**
	 * helper method which stores the context server variables to the settings file 
	 * @throws Exception 
	 */
	private void updateServerSettings() throws Exception{
		Document dom;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.parse("settings.xml");
			
			
			//set the server name
			NodeList nlstServer = dom.getElementsByTagName("server_hostname");
			nlstServer.item(0).setTextContent(serverHost);
								
		
			//set the server port
			nlstServer = dom.getElementsByTagName("server_port");
			nlstServer.item(0).setTextContent(String.valueOf(serverPort));
			
			//set the server timeout
			nlstServer = dom.getElementsByTagName("server_timeout");
			nlstServer.item(0).setTextContent(String.valueOf(serverTimeout));
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			StreamResult result = new StreamResult(new File("settings.xml"));
			transformer.transform(source, result);
			
		} catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * helper private method called by the constructor to fetch the 
	 * settings from settings.xml file & store it to the context variables
	 */
	private void fetchSettings(){
		ObserversForNewPots = new ArrayList<Observer>();
				
		Document dom;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.parse("settings.xml");
			
			
			//get the server name
			try{
				NodeList nlstServer = dom.getElementsByTagName("server_hostname");
				serverHost = nlstServer.item(0).getTextContent();
			}catch(Exception e){
				serverHost = "N/A";
			}
					
		
			//get the server port
			try{
				NodeList nlstServer = dom.getElementsByTagName("server_port");
				serverPort = Integer.parseInt(nlstServer.item(0).getTextContent());
			}catch(Exception e){
				serverPort = -1;
			}
			
			//get the server timeout
			try{
				NodeList nlstTO = dom.getElementsByTagName("server_timeout");
				serverTimeout = Integer.parseInt(nlstTO.item(0).getTextContent());
			}catch(Exception e){
				serverTimeout = 15000;
			}
			
			try {
				//get the list of pots
				NodeList nlstPots = dom.getElementsByTagName("pot");
				
				//getting each pot's details
				for(int i=0; i<nlstPots.getLength(); i++){
					
					String s = (((Element)nlstPots.item(i)).getElementsByTagName("descr")).item(0).getTextContent();
					int j = Integer.parseInt((((Element)nlstPots.item(i)).getElementsByTagName("pin")).item(0).getTextContent());
					double minMoistVal = Double.parseDouble((((Element)nlstPots.item(i)).getElementsByTagName("minMoist")).item(0).getTextContent());
					double maxMoistVal = Double.parseDouble((((Element)nlstPots.item(i)).getElementsByTagName("maxMoist")).item(0).getTextContent());
					
					Pot p = new Pot(s,j, minMoistVal, maxMoistVal);
					
					potDescrs.add(p);
				}
			} catch (Exception e){
				potDescrs = null;
			}
			
		} catch (Exception e){
			serverHost = "N/A";
			serverPort = -1;
			potDescrs = null;
		}
	}
	
	
	/**
	 * helper method that creates the settings.xml file
	 * @throws IOException 
	 */
	private void createSettings() throws Exception{
		
		Document dom;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				
		
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.newDocument();
			
			Element rootElement = dom.createElement("settings");
			dom.appendChild(rootElement);
			
			Element srvNameElement = dom.createElement("server_hostname");
			srvNameElement.appendChild(dom.createTextNode(""));
			rootElement.appendChild(srvNameElement);
			
			Element srvPortElement = dom.createElement("server_port");
			srvPortElement.appendChild(dom.createTextNode(""));
			rootElement.appendChild(srvPortElement);
			
			Element srvTOElement = dom.createElement("server_timeout");
			srvTOElement.appendChild(dom.createTextNode(String.valueOf(serverTimeout)));
			rootElement.appendChild(srvTOElement);
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			StreamResult result = new StreamResult(new File("settings.xml"));
			transformer.transform(source, result);
			
			
		
	
	}
	
	public void registerObserverForNewPots(Observer obs){
		ObserversForNewPots.add(obs);
	}

}
