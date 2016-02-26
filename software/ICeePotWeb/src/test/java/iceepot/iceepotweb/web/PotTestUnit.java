package iceepot.iceepotweb.web;

public class PotTestUnit {
	private int potId;
	private int monthFrom;
	private int yearFrom;
	private int monthTo;
	private int yearTo;
	
	public PotTestUnit(int potId, int monthFrom, int yearFrom, int monthTo,
			int yearTo) {
		this.potId = potId;
		this.monthFrom = monthFrom;
		this.yearFrom = yearFrom;
		this.monthTo = monthTo;
		this.yearTo = yearTo;
	}

	public int getPotId() {
		return potId;
	}

	public int getMonthFrom() {
		return monthFrom;
	}

	public int getYearFrom() {
		return yearFrom;
	}

	public int getMonthTo() {
		return monthTo;
	}

	public int getYearTo() {
		return yearTo;
	}
}
