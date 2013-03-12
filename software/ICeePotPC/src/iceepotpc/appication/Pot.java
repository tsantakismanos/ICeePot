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
	private double minMoistVal;
	private double maxMoistVal;
	
	
	
	public Pot(String descr, int pin, double min, double max) {
		super();
		this.descr = descr;
		this.pin = pin;
		this.minMoistVal = min;
		this.maxMoistVal = max;
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
	public double getMinMoistVal() {
		return minMoistVal;
	}
	public void setMinMoistVal(double minMoistVal) {
		this.minMoistVal = minMoistVal;
	}
	public double getMaxMoistVal() {
		return maxMoistVal;
	}
	public void setMaxMoistVal(double maxMoistVal) {
		this.maxMoistVal = maxMoistVal;
	}

}
