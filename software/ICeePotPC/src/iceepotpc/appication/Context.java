package iceepotpc.appication;

import java.io.File;
import java.util.ArrayList;

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
	
	
	/**
	 * constructor which reads the settings xml file and applies the
	 * configuration to Context variables
	 */
	public Context() {
		
		Document dom;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.parse("Settings.xml");
			
			
			//get the server name
			try{
				NodeList nlstServer = dom.getElementsByTagName("server_hostname");
				Context.serverHost = nlstServer.item(0).getTextContent();
			}catch(Exception e){
				Context.serverHost = "N/A";
			}
					
		
			//get the server port
			try{
				NodeList nlstServer = dom.getElementsByTagName("server_port");
				Context.serverPort = Integer.parseInt(nlstServer.item(0).getTextContent());
			}catch(Exception e){
				Context.serverPort = -1;
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
					
					Context.potDescrs.add(p);
				}
			} catch (Exception e){
				Context.potDescrs = null;
			}
			
		} catch (Exception e){
			Context.serverHost = "N/A";
			Context.serverPort = -1;
			Context.potDescrs = null;
		}
	}
	
	
	/**
	 * helper method to be called when UI is to create a new pot
	 * @param p: the pot to be added to the context & to the settings file
	 */
	public static void addPot(Pot p){
		potDescrs.add(p);
		addPotToSettings(p);
	}
	
	/**
	 * helper method which stores the context variables to the settings file 
	 */
	private static void addPotToSettings(Pot p){
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
			//TODO: inform the caller
		}
	}
	
	
	/**
	 * helper method which stores the context server variables to the settings file 
	 */
	public static void updateServerSettings(){
		Document dom;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.parse("Settings.xml");
			
			
			//set the server name
			NodeList nlstServer = dom.getElementsByTagName("server_hostname");
			nlstServer.item(0).setTextContent(Context.serverHost);
								
		
			//set the server port
			nlstServer = dom.getElementsByTagName("server_port");
			nlstServer.item(0).setTextContent(String.valueOf(Context.serverPort));
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			StreamResult result = new StreamResult(new File("settings.xml"));
			transformer.transform(source, result);
			
		} catch (Exception e){
			//TODO: inform the caller
		}
	}


}
