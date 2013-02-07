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
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3977644217746517687L;
	private int pot;
	private double moment;
	private double value;
	
	
	public Meauserement(int pot, double moment, double value) {
		super();
		this.pot = pot;
		this.moment = moment;
		this.value = value;
	}
	public int getPot() {
		return pot;
	}
	public void setPot(int pot) {
		this.pot = pot;
	}
	public double getMoment() {
		return moment;
	}
	public void setMoment(double moment) {
		this.moment = moment;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
	

}
