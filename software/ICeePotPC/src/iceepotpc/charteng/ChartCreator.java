package iceepotpc.charteng;

import iceepotlib.gateway.*;

import java.util.ArrayList;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Second;
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
	
	public static JFreeChart createChart(ArrayList<Measurement> measurements, double minMoist, double maxMoist){
		
		
		
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		
		TimeSeries ts = new TimeSeries("Moisture Level");
		
		TimeSeries tsWarningHigh = new TimeSeries("Too much level");
		TimeSeries tsWarningLow = new TimeSeries("Too low level");
		
		for(int i=0; i< measurements.size(); i++){
			Date d = new Date(measurements.get(i).getMoment());
			ts.add(new TimeSeriesDataItem(new Second(d), measurements.get(i).getValue()));
			tsWarningHigh.add(new TimeSeriesDataItem(new Second(d), maxMoist));
			tsWarningLow.add(new TimeSeriesDataItem(new Second(d), minMoist));
		}
		
		
		dataset.addSeries(tsWarningHigh);
		dataset.addSeries(ts);
		dataset.addSeries(tsWarningLow);
		
		JFreeChart fchart = ChartFactory.createTimeSeriesChart("", "Time", "Moisture level", dataset, false, true, false);
		
		
		return fchart;
		
	}

}
