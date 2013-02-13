package iceepotpc.appication;

/**
 * @author tsantakis
 * Class that models the pot which is identified by a 
 * descrtiption and a number which represents the analog pin in which the 
 * sensor is connected on the server (0,1,2,3,4,...) 
 *
 */
public class Pot {
	
	private String descr;
	private int pin;
	
	
	
	public Pot(String descr, int pin) {
		super();
		this.descr = descr;
		this.pin = pin;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}

}
