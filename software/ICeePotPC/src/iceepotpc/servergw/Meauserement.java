package iceepotpc.servergw;

import java.io.Serializable;


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

public class Meauserement implements Serializable{
	
	private static final long serialVersionUID = 3977644217746517687L;
	private int pot;
	private long moment;
	private double value;
	
	
	/** class constructor 
	 * @param pot: a number indicating the pot number (0,1,2,...)
	 * @param moment: time in which the measurement was taken
	 * @param value: the value of the parameter taken (moisture, temperature, battery level)
	 */
	public Meauserement(int pot, long moment, double value) {
		super();
		this.pot = pot;
		this.moment = moment;
		this.value = value;
	}

	//getters
	public int getPot() {
		return pot;
	}
	public long getMoment() {
		return moment;
	}
	public double getValue() {
		return value;
	}
	
	//setters
	public void setPot(int pot) {
		this.pot = pot;
	}
	public void setMoment(long moment) {
		this.moment = moment;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
	

}
