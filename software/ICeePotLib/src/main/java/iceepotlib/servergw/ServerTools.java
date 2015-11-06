package iceepotlib.servergw;



import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author tsantakismanos 
 * 		the class that models the server entity of the project
 *      and implements the interface between the present client and the
 *      arduino server. The time unit of measurements requested is a month
 * 
 */
public class ServerTools{
	
	
	/**
	 * @param date
	 *            : a String representing the month for which the measurements
	 *            are requested
	 * @param pot
	 *            : pot defined by the caller
	 * @return: an arraylist of measurements (pot-moment-value)
	 * @throws Exception 
	 */
	public synchronized static ArrayList<Measurement> GetMeasurements(Calendar c, int potId, String host, int port, int timeout) throws Exception {

		
		ArrayList<Measurement> measurements = new ArrayList<Measurement>();
		
		
		
		//construct the request in a string
		String request_str = Integer.toString(c.get(Calendar.MONTH)) + Integer.toString(c.get(Calendar.YEAR));

		InputStream is = null;
		OutputStream os = null;
		Socket s = null;
		try {
			
			request_str = request_str.concat("\0");
			byte[] request = request_str.getBytes();
			
			byte[] responsePacket = new byte[9];
			int respPacketIdx = 0;
			
			int response = 0;
			//String response_str = "";
			s = new Socket(host, port);
			s.setSoTimeout(timeout);
			os = s.getOutputStream();

			// send request
			os.write(request);
			os.flush();

			is = s.getInputStream();

			// get & print response
			response = is.read();

			while (response != -1) {
				if(respPacketIdx == 9) //a new packet has been received
				{
					// parse, store and begin a new line
					Measurement m = ParseMeasurementRow(responsePacket);
					if(m.getId() == potId)
					measurements.add(m);
					
					respPacketIdx = 0;
				}
				
				responsePacket[respPacketIdx] = (byte)response;
				respPacketIdx++;
				
				
				response = is.read();
			}
			
			try {
				is.close();
				s.close();
			} catch (Exception exi) {
				
			}

		} catch (Exception ex) {
			
			try {
				is.close();
				s.close();
			} catch (Exception exi) {
				
			}
			throw new Exception(ex);
		} 
		return measurements;
	}
	
	
	/**
	 * @param s: a packet byte of the form: <time><type><id><value>
	 * @return a measurement object with the above values after parsing that row
	 */
	private static Measurement ParseMeasurementRow(byte[] s){
		
		
		Measurement m = null;
		
		long moment = (0xFF & s[0])|
				      ((0xFF & s[1]) << 8)|
				      ((0xFF & s[2]) << 16)|
				      ((0xFF & s[3]) << 24);
		int type = (0xFF & s[4]);
		int id = (0xFF & s[5])|
			      ((0xFF & s[6]) << 8);
		double value = (0xFF & s[7])|
			      		((0xFF & s[8]) << 8);
		
		m = new Measurement(moment*1000, type, id, value);
		
		
		//Meauserement m = new Meauserement(Integer.parseInt(parts[1]), Long.parseLong(parts[0])*1000, Double.parseDouble(parts[2]));
		
		return m;
	}


}
