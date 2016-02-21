package iceepot.iceepotweb.model;

public class Measurement {

	private long moment;
	private double value;
		
	public Measurement(long moment, double value) {
		this.moment = moment;
		this.value = value;
	}

	public long getMoment() {
		return moment;
	}

	public void setMoment(long moment) {
		this.moment = moment;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	
}
