package iceepotpc.servergw;

import java.util.ArrayList;
import java.util.Calendar;

import iceepotpc.application.Callable;

/** Runnable class to perform the async task of sending
 * time requests to the server using the ServerTools methods
 * It is also responsible of updating the callers progresssbar 
 * between the requests and give them the data in the end
 * 
 * @author tsantakis
 *
 */
public class ServerService implements Runnable {
	
	private Callable caller;
	private ArrayList<Meauserement> measurements;
	private Calendar from,to;
	private int pin;
	private Exception exception;
	
	
	public ServerService(Callable caller, Calendar from, Calendar to, int pin) {
		
		this.measurements = new ArrayList<Meauserement>();
		this.caller = caller;
		this.from = Calendar.getInstance();
		this.from.setTime(from.getTime());
		this.to = Calendar.getInstance();
		this.to.setTime(to.getTime());
		this.pin = pin;
	}
	
	
	@Override
	public void run() {
		
		try{
			//for all months in range
			while((from.get(Calendar.MONTH) != to.get(Calendar.MONTH)) 
					|| (from.get(Calendar.YEAR) != to.get(Calendar.YEAR))){
				measurements.addAll(ServerTools.GetMeasurements(from, pin));
				from.add(Calendar.MONTH, 1);
				caller.updateProgressBar();
			}
			
			measurements.addAll(ServerTools.GetMeasurements(to, pin));
			caller.updateProgressBar();
			
			caller.updateMeasurementData(measurements, null);
		}catch(Exception e){
			this.exception = new Exception(e.getMessage());
			caller.updateMeasurementData(measurements, this.exception);
		}
	}

}
