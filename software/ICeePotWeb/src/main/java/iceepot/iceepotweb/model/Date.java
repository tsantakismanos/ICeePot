package iceepot.iceepotweb.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Date{

	
		
	private int month;
	private int year;
	
	public Date(int month, int year) {
	
		this.month = month;
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public String toString() {
		
		return Integer.toString(month) + Integer.toString(year);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + month;
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Date other = (Date) obj;
		if (month != other.month)
			return false;
		if (year != other.year)
			return false;
		return true;
	}
	
	
	public static List<Date> createDateRange(int monthFrom, int yearFrom, int monthTo, int yearTo){
		
		
		List<Date> dates = new ArrayList<Date>();
			
		
		Calendar idx = Calendar.getInstance();
		 idx.set(Calendar.MONTH, monthFrom);
		 idx.set(Calendar.YEAR, yearFrom);
		 idx.set(Calendar.DAY_OF_MONTH, 1);
		 idx.set(Calendar.HOUR,0);
		 idx.set(Calendar.MINUTE,0);
		 idx.set(Calendar.SECOND,0);
		 idx.set(Calendar.MILLISECOND,0);

        Calendar last = Calendar.getInstance();
        last.set(Calendar.MONTH, monthTo);
        last.set(Calendar.YEAR, yearTo);
        last.set(Calendar.DAY_OF_MONTH, 1);
        last.set(Calendar.HOUR,0);
        last.set(Calendar.MINUTE,0);
        last.set(Calendar.SECOND,0);
        last.set(Calendar.MILLISECOND,0);
        
        while(idx.equals(last) || idx.before(last)){
       	 dates.add(new Date(idx.get(Calendar.MONTH), idx.get(Calendar.YEAR)));
       	 idx.add(Calendar.MONTH, 1);
        }
		
		return dates;
		
	}
}
