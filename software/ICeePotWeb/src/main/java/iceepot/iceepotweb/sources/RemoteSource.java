package iceepot.iceepotweb.sources;

import iceepot.iceepotweb.model.Date;
import iceepot.iceepotweb.model.Measurement;
import iceepot.iceepotweb.model.MeasurementType;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RemoteSource implements Source {

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

	
	@Override
	public List<Measurement> listPotMeasurementByTypeDate(int potId, Date date,
			MeasurementType type)  throws SourceException{
		
		ArrayList<Measurement> measurements = new ArrayList<Measurement>();
		
		
		//construct the request in a string
		String request_str = date.toString();

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

					if(row.id == potId && row.type == type.ordinal()){
						Measurement m = new Measurement(row.moment*1000, row.value);
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
			} catch (Exception ex) {
				throw new SourceException(ex.getLocalizedMessage());
			}

		} catch (Exception ex) {
			
			try {
				is.close();
				s.close();
			} catch (Exception exi) {
				throw new SourceException(exi.getLocalizedMessage());
			}
			throw new SourceException(ex.getLocalizedMessage());
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
