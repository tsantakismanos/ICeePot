package iceepotpc.application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/** Class that models the settings of the application
 *  which includes:
 *  --server settings (name, port, timeout)
 *  --pots settings (descr, id, min-max value for each one)
 * @author tsantakis
 *
 */
public class Settings {
	
	/**
	 * helper method that creates the settings.xml file
	 * @throws IOException 
	 */
	public static void createSettings(Context c) throws Exception{
		
		Document dom;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				
		
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.newDocument();
			
			Element rootElement = dom.createElement("settings");
			dom.appendChild(rootElement);
			
			Element srvNameElement = dom.createElement("server_hostname");
			srvNameElement.appendChild(dom.createTextNode(c.getServerHost()));
			rootElement.appendChild(srvNameElement);
			
			Element srvPortElement = dom.createElement("server_port");
			srvPortElement.appendChild(dom.createTextNode(String.valueOf(c.getServerPort())));
			rootElement.appendChild(srvPortElement);
			
			Element srvTOElement = dom.createElement("server_timeout");
			srvTOElement.appendChild(dom.createTextNode(String.valueOf(c.getServerTimeout())));
			rootElement.appendChild(srvTOElement);
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			StreamResult result = new StreamResult(new File("settings.xml"));
			transformer.transform(source, result);
	}
	
	/**
	 * helper private method called by the constructor to fetch the 
	 * settings from settings.xml file & store it to the context variables
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static void fetchSettings(Context c) throws ParserConfigurationException, SAXException, IOException{
		
		c.setObserversForNewPots(new ArrayList<Observer>());
				
		Document dom;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.parse("settings.xml");
			
			
			//get the server name
			
				NodeList nlstServer = dom.getElementsByTagName("server_hostname");
				c.setServerHost(nlstServer.item(0).getTextContent());
			
					
		
			//get the server port
			
				nlstServer = dom.getElementsByTagName("server_port");
				c.setServerPort(Integer.parseInt(nlstServer.item(0).getTextContent()));
			
			
			//get the server timeout
			
				NodeList nlstTO = dom.getElementsByTagName("server_timeout");
				c.setServerTimeout(Integer.parseInt(nlstTO.item(0).getTextContent()));
			
			
			
				//get the list of pots
				NodeList nlstPots = dom.getElementsByTagName("pot");
				
				//getting each pot's details
				for(int i=0; i<nlstPots.getLength(); i++){
					
					String s = (((Element)nlstPots.item(i)).getElementsByTagName("descr")).item(0).getTextContent();
					int j = Integer.parseInt((((Element)nlstPots.item(i)).getElementsByTagName("id")).item(0).getTextContent());
					double minMoistVal = Double.parseDouble((((Element)nlstPots.item(i)).getElementsByTagName("minMoist")).item(0).getTextContent());
					double maxMoistVal = Double.parseDouble((((Element)nlstPots.item(i)).getElementsByTagName("maxMoist")).item(0).getTextContent());
					
					Pot p = new Pot(s,j, minMoistVal, maxMoistVal);
					
					c.getPots().add(p);
				}
	}
	
	
	/**
	 * helper method which stores the context variables to the settings file
	 *  @param p: the pot to be added to the settings file 
	 * @throws Exception 
	 */
	public static void addPotToSettings(Pot p) throws Exception{
		Document dom;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.parse("settings.xml");
			
			//get the root element
			NodeList nRoot = dom.getElementsByTagName("settings");
			
			Element elDescr = dom.createElement("descr");
			elDescr.setTextContent(p.getDescr());
			
			Element elId = dom.createElement("id");
			elId.setTextContent(String.valueOf(p.getId()));
			
			Element elMinMoist = dom.createElement("minMoist");
			elMinMoist.setTextContent(String.valueOf(p.getMinMoistVal()));
			
			Element elMaxMoist = dom.createElement("maxMoist");
			elMaxMoist.setTextContent(String.valueOf(p.getMaxMoistVal()));
			
			Node elPot = dom.createElement("pot");
			
			elPot.appendChild(elDescr);
			elPot.appendChild(elId);
			elPot.appendChild(elMinMoist);
			elPot.appendChild(elMaxMoist);
			
			nRoot.item(0).appendChild(elPot);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			StreamResult result = new StreamResult(new File("settings.xml"));
			transformer.transform(source, result);
			
		
	}
	
	/**
	 * helper method which stores the context server variables to the settings file 
	 * @throws Exception 
	 */
	public static void updateServerSettings(Context c) throws Exception{
		Document dom;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.parse("settings.xml");
			
			
			//set the server name
			NodeList nlstServer = dom.getElementsByTagName("server_hostname");
			nlstServer.item(0).setTextContent(c.getServerHost());
								
		
			//set the server port
			nlstServer = dom.getElementsByTagName("server_port");
			nlstServer.item(0).setTextContent(String.valueOf(c.getServerPort()));
			
			//set the server timeout
			nlstServer = dom.getElementsByTagName("server_timeout");
			nlstServer.item(0).setTextContent(String.valueOf(c.getServerTimeout()));
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			StreamResult result = new StreamResult(new File("settings.xml"));
			transformer.transform(source, result);
	}
	
	/**Helper method to be called from UI when the minimum
	 * moisture value for a pot has changed	 * 
	 * @param id: the id of the pot whose minimum value has changed
	 * @param newMinValue: of the moisture level
	 * @param newMaxValue: of the moisture level
	 * @throws Exception 
	 */
	public static void modifyMinMoistValue(int id, double newMinValue, double newMaxValue) throws Exception{
		Document dom;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.parse("settings.xml");
			
			//get the list of pots
			NodeList nlstPots = dom.getElementsByTagName("pot");
			
			//getting each pot's details
			for(int i=0; i<nlstPots.getLength(); i++){
				
				int idx = Integer.parseInt((((Element)nlstPots.item(i)).getElementsByTagName("id")).item(0).getTextContent());
				if(idx == id){
					(((Element)nlstPots.item(i)).getElementsByTagName("minMoist")).item(0).setTextContent(String.valueOf(newMinValue));
					(((Element)nlstPots.item(i)).getElementsByTagName("maxMoist")).item(0).setTextContent(String.valueOf(newMaxValue));
				}
				
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			StreamResult result = new StreamResult(new File("settings.xml"));
			transformer.transform(source, result);
		
	}

}
