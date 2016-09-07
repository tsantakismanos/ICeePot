package iceepot.iceepotweb.web;

import iceepot.iceepotweb.model.Date;
import iceepot.iceepotweb.model.Measurement;
import iceepot.iceepotweb.model.MeasurementType;
import iceepot.iceepotweb.sources.Cache;
import iceepot.iceepotweb.sources.Source;
import iceepot.iceepotweb.sources.SourceException;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/pots")
public class PotsController {
	
	private Source remoteSource;
	private Cache localSource;
	
	public PotsController() {
	
	}

	@Autowired
	public PotsController(Source remoteSource, Cache localSource) {
		
		this.remoteSource = remoteSource;
		this.localSource = localSource;
	}
	

	@RequestMapping(value="/{potId}/moisture", method=RequestMethod.GET)
	public List<Measurement> potMoisture(
			@PathVariable("potId") int potId,
			@RequestParam("monthFrom") int monthFrom,
			@RequestParam("yearFrom") int yearFrom,
			@RequestParam("monthTo") int monthTo,
			@RequestParam("yearTo") int yearTo){
	
		List<Measurement> measurements = new ArrayList<Measurement>();
		
		List<Date> dates = Date.createDateRange(monthFrom, yearFrom, monthTo, yearTo);
		
		for(Date date:dates){
			
			List<Measurement> measurementsByDate = localSource.find(potId, date, MeasurementType.MOISTURE);
			
			if(measurementsByDate == null || measurementsByDate.isEmpty()){
				measurementsByDate = remoteSource.listPotMeasurementByTypeDate(potId, date, MeasurementType.MOISTURE);
				localSource.store(potId, date, MeasurementType.MOISTURE, measurementsByDate);
			}
			measurements.addAll(measurementsByDate);
		}
		
		return measurements;
		
	}
	
	@ExceptionHandler(SourceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleSourceException(SourceException ex){
		
		Logger logger = LoggerFactory.getLogger(PotsController.class);
	    logger.error(ex.getMessage());
		
	}
	

}
