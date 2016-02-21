package iceepot.iceepotweb.model;

public class Measurement {

	private long moment;
	private MeasurementType type;
	private double value;
		
	public Measurement(long moment, MeasurementType type, double value) {
		this.moment = moment;
		this.type = type;
		this.value = value;
	}
	
	public Measurement(long moment, int type, double value) {
		this.moment = moment;
		
		switch(type){
		case 0:
			this.type = MeasurementType.MOISTURE;
		case 1:
			this.type = MeasurementType.BATTERY;
		}
		
		this.value = value;
	}

	public long getMoment() {
		return moment;
	}

	public void setMoment(long moment) {
		this.moment = moment;
	}

	public MeasurementType getType() {
		return type;
	}

	public void setType(MeasurementType type) {
		this.type = type;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	
}
