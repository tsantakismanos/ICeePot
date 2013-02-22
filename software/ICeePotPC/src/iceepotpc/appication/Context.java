package iceepotpc.appication;


import java.io.File;
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
	
	
	/**
	 *  arrayList keeping the descriptions of all monitored pots (the tabs to be generated)
	 */
	public ArrayList<Pot> potDescrs = new ArrayList<Pot>();
	
	
	/**
	 * server configuration (for opening the connection) 
	 */
	public String serverHost = "homeplants.dyndns.org";
	public int serverPort = 3629;
	
	public boolean isDebug = true;
	
	public ArrayList<Observer> uiElements = null; 
	
	
	/**
	 * constructor which reads the settings xml file and applies the
	 * configuration to Context variables
	 */
	public Context() {
		
		uiElements = new ArrayList<Observer>();
		
		Document dom;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.parse("Settings.xml");
			
			
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
			
			try {
				//get the list of pots
				NodeList nlstPots = dom.getElementsByTagName("pot");
				
				//getting each pot's details
				for(int i=0; i<nlstPots.getLength(); i++){
					
					//NodeList nlstPotDetails = nlstPots.item(i).getChildNodes();
					
					String s = (((Element)nlstPots.item(i)).getElementsByTagName("descr")).item(0).getTextContent();
					int j = Integer.parseInt((((Element)nlstPots.item(i)).getElementsByTagName("pin")).item(0).getTextContent());
					
					Pot p = new Pot(s,j);
					
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
	 * helper method to be called when UI is to create a new pot
	 * @param p: the pot to be added to the context & to the settings file
	 * @throws Exception 
	 */
	public void addPot(Pot p) throws Exception{
		potDescrs.add(p);
		addPotToSettings(p);
		
		//notify the observers
		if(uiElements != null)
			for(int i=0; i<uiElements.size(); i++)
				uiElements.get(i).update(null, null);
	}
	
	/** helper method to be called when UI wants to update the server
	 * information, Context is updated & settings file is modified 
	 * @param hostName
	 * @param port
	 * @throws Exception 
	 */
	public void updateServer(String hostName, int port) throws Exception{
		
		serverHost = hostName;
		serverPort = port;
		
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
			
			dom = db.parse("Settings.xml");
			
			//get the root element
			NodeList nRoot = dom.getElementsByTagName("settings");
			
			Element elDescr = dom.createElement("descr");
			elDescr.setTextContent(p.getDescr());
			
			Element elPin = dom.createElement("pin");
			elPin.setTextContent(String.valueOf(p.getPin()));
			
			Node elPot = dom.createElement("pot");
			
			elPot.appendChild(elDescr);
			elPot.appendChild(elPin);
			
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
			
			dom = db.parse("Settings.xml");
			
			
			//set the server name
			NodeList nlstServer = dom.getElementsByTagName("server_hostname");
			nlstServer.item(0).setTextContent(serverHost);
								
		
			//set the server port
			nlstServer = dom.getElementsByTagName("server_port");
			nlstServer.item(0).setTextContent(String.valueOf(serverPort));
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			StreamResult result = new StreamResult(new File("settings.xml"));
			transformer.transform(source, result);
			
		} catch (Exception e){
			throw e;
		}
	}


}
