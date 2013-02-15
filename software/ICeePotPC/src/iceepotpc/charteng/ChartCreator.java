package iceepotpc.charteng;

import iceepotpc.servergw.Meauserement;

import java.util.ArrayList;
import java.util.Date;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

/**
 * @author tsantakis
 * class containing static methods for creating a Time chart
 * of the measurements given based on date parameters
 *  */
public class ChartCreator 
{
	
	public static ChartPanel createChart(ArrayList<Meauserement> measurements){
		
		
		
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		
		TimeSeries ts = new TimeSeries("Moisture Level");
		
		for(int i=0; i< measurements.size(); i++){
			Date d = new Date((long)measurements.get(i).getMoment());
			ts.add(new TimeSeriesDataItem(new Millisecond(d), measurements.get(i).getValue()));
		}
		
		dataset.addSeries(ts);
			
		JFreeChart fchart = ChartFactory.createTimeSeriesChart("", "Time", "Moisture level", dataset, true, true, false);
		
		ChartPanel cp = new ChartPanel(fchart);
		
		return cp;
		
	}

}
