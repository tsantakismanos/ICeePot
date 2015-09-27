package iceepotlib.entities;

/**
 * @author tsantakis
 * Class that models the pot which is identified by a 
 * descrtiption and a number which represents the pot in which the 
 * sensor is connected on the server (0,1,2,3,4,...) 
 *
 */

public class Pot {

	private String descr;
	private int id;
	private double minMoistVal;
	private double maxMoistVal;
	
	
	public Pot(){
		id = -1;
		descr = null;
		minMoistVal = -1;
		maxMoistVal = -1;
	}
	
	
	/**Class constructor creating the Pot 
	 * @param descr: like Mint, Basil, ...
	 * @param id: identification of the pot 
	 * @param min: the minimum value of the moisture
	 * @param max: the maximum value of the moisture
	 */
	public Pot(String descr, int id, double min, double max) {
		this.descr = descr;
		this.id = id;
		this.minMoistVal = min;
		this.maxMoistVal = max;
	}
	
	//getters
	public String getDescr() {
		return descr;
	}
	public int getId() {
		return id;
	}
	public double getMinMoistVal() {
		return minMoistVal;
	}
	public double getMaxMoistVal() {
		return maxMoistVal;
	}
	
	//setters
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setMinMoistVal(double minMoistVal) throws Exception {
		this.minMoistVal = minMoistVal;
	}
	public void setMaxMoistVal(double maxMoistVal) throws Exception {
		this.maxMoistVal = maxMoistVal;
	}
}
