package iceepotpc.servergw;

import java.util.ArrayList;
import java.util.Calendar;

import iceepotpc.application.Callable;

/** Runnable class to perform the asynchronous task of sending
 * time requests to the server using the ServerTools methods
 * It is also responsible of updating the callers progress bar 
 * between the requests and give them the data in the end
 * 
 * @author tsantakis
 *
 */
public class ServerService implements Runnable {
	
	private Callable caller;
	private ArrayList<Meauserement> measurements;
	private Calendar from,to;
	private int potId;
	private Exception exception;
	
	
	/** Constructor that sets up the service 
	 * @param caller: the class triggering the service and receiving the results when ready
	 * @param from: time window of the results 
	 * @param to: time window of the results
	 * @param pot: indicator of the measured pot (0,1,2,...)
	 */
	public ServerService(Callable caller, Calendar from, Calendar to, int potId) {
		
		this.measurements = new ArrayList<Meauserement>();
		this.caller = caller;
		this.from = Calendar.getInstance();
		this.from.setTime(from.getTime());
		this.to = Calendar.getInstance();
		this.to.setTime(to.getTime());
		this.potId = potId;
	}
	
	
	@Override
	public void run() {
		
		try{
			//for all months in range
			while((from.get(Calendar.MONTH) != to.get(Calendar.MONTH)) 
					|| (from.get(Calendar.YEAR) != to.get(Calendar.YEAR))){
				measurements.addAll(ServerTools.GetMeasurements(from, potId));
				from.add(Calendar.MONTH, 1);
				caller.updateProgressBar();
			}
			
			measurements.addAll(ServerTools.GetMeasurements(to, potId));
			caller.updateProgressBar();
			
			caller.updateMeasurementData(measurements, null);
		}catch(Exception e){
			this.exception = new Exception(e.getMessage());
			caller.updateMeasurementData(measurements, this.exception);
		}
	}

}
