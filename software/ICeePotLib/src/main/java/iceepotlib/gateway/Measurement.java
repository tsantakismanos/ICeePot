package iceepotlib.gateway;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author tsantakismanos
 * 
 * this class is the basic unit of communication
 * between the client and the server and it models
 * the result of a single moisture measurement taken
 * from a pot. 
 * - The moment is in seconds from 1970, 
 * - The pot is the specified pot for which the measurement is taken 
 * - The value is a number from 300-700
 */

public class Measurement implements Serializable{
	
	private static final long serialVersionUID = 3977644217746517687L;
	private long moment;
	private MeasurementType type;
	private int id;
	private double value;
	
	
	/** class constructor 
	 * @param pot: a number indicating the pot number (0,1,2,...)
	 * @param moment: time in which the measurement was taken
	 * @param value: the value of the parameter taken (moisture, temperature, battery level)
	 */
	public Measurement(long moment, int type, int id, double value) {
		super();
		this.moment = moment;
		switch(type){
		case 0:
			this.type = MeasurementType.MOISTURE;
		case 1:
			this.type = MeasurementType.BATTERY;
		}
		
		this.id = id;
		this.value = value;
	}

	//getters
	public int getId() {
		return id;
	}
	public long getMoment() {
		return moment;
	}
	public double getValue() {
		return value;
	}
	public MeasurementType getType() {
		return type;
	}


	//setters
	public void setId(int id) {
		this.id = id;
	}
	public void setMoment(long moment) {
		this.moment = moment;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public void setType(MeasurementType type) {
		this.type = type;
	}

	public static HashMap<Long, Double> getHashMap(ArrayList<Measurement> list){
		
		HashMap<Long, Double> map = new HashMap<Long, Double>(list.size());
		
		for(int i=0;i<list.size();i++){
			map.put(list.get(i).getMoment(), list.get(i).getValue());
		}
		
		return map;
	}
	

}
