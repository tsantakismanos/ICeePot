package iceepot.iceepotweb.sources;

import iceepot.iceepotweb.model.Measurement;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RemoteSource implements MeasurementsSource {

	private String host; 
	private int port;
	private int timeout;
	
	
		
	public RemoteSource() {
	
	}

	public RemoteSource(String host, int port, int timeout) {
		this.host = host;
		this.port = port;
		this.timeout = timeout;
	}
	
	

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public int getTimeout() {
		return timeout;
	}

	public List<Measurement> getByMonthNYear(int month, int year, int potId) throws Exception {

		ArrayList<Measurement> measurements = new ArrayList<Measurement>();
		
		
		//construct the request in a string
		String request_str = Integer.toString(month) + Integer.toString(year);

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
			s.setSoTimeout(timeout*1000);
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
					Row row = new Row(responsePacket);

					if(row.id == potId){
						Measurement m = new Measurement(row.moment*1000, row.type, row.value);
						measurements.add(m);
					}
					
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

	public List<Measurement> getByRange(int monthFrom, int yearFrom, int monthTo, int yearTo, int potId) throws Exception {
		
		List<Measurement> measurements = new ArrayList<Measurement>();
		
		 Calendar idx = Calendar.getInstance();
		 idx.set(Calendar.MONTH, monthFrom);
		 idx.set(Calendar.YEAR, yearFrom);
		 idx.set(Calendar.DAY_OF_MONTH, 1);
		 idx.set(Calendar.HOUR,0);
		 idx.set(Calendar.MINUTE,0);
		 idx.set(Calendar.SECOND,0);
		 idx.set(Calendar.MILLISECOND,0);

         Calendar last = Calendar.getInstance();
         last.set(Calendar.MONTH, monthTo);
         last.set(Calendar.YEAR, yearTo);
         last.set(Calendar.DAY_OF_MONTH, 1);
         last.set(Calendar.HOUR,0);
         last.set(Calendar.MINUTE,0);
         last.set(Calendar.SECOND,0);
         last.set(Calendar.MILLISECOND,0);
         
         while(idx.before(last)){
        	 measurements.addAll(getByMonthNYear(idx.get(Calendar.MONTH), idx.get(Calendar.YEAR), potId));
        	 idx.add(Calendar.MONTH, 1);
         }
		return measurements;
	}
	
	
	
	private class Row{
		
		public long moment ;
		public int type;
		public int id;
		public double value;
		
		public Row(byte[] s) {
			
			moment = (0xFF & s[0])|
				      ((0xFF & s[1]) << 8)|
				      ((0xFF & s[2]) << 16)|
				      ((0xFF & s[3]) << 24);
			type = (0xFF & s[4]);
			id = (0xFF & s[5])|
				      ((0xFF & s[6]) << 8);
			value = (0xFF & s[7])|
				      		((0xFF & s[8]) << 8);
			
		}
		
	}

}
