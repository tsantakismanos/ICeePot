package iceepot.iceepotweb.web;

import iceepot.iceepotweb.model.Date;
import iceepot.iceepotweb.model.Measurement;
import iceepot.iceepotweb.model.MeasurementType;
import iceepot.iceepotweb.sources.Cache;
import iceepot.iceepotweb.sources.Source;
import iceepot.iceepotweb.sources.SourceException;

import java.util.ArrayList;
import java.util.Calendar;
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
	
	public static final String EMPTY_PARAMETER = "-1";
	
	private Source remoteSource;
	private Cache cache;
	
	public PotsController() {
	
	}

	@Autowired
	public PotsController(Source remoteSource, Cache cache) {
		
		this.remoteSource = remoteSource;
		this.cache = cache;
	}
	
	

	@RequestMapping(value="/{potId}/moisture", method=RequestMethod.GET)
	public List<Measurement> potMoisture(
			@PathVariable("potId") int potId,
			@RequestParam(value="monthFrom", defaultValue=EMPTY_PARAMETER) int monthFrom,
			@RequestParam(value="yearFrom", defaultValue=EMPTY_PARAMETER) int yearFrom,
			@RequestParam(value="monthTo", defaultValue=EMPTY_PARAMETER) int monthTo,
			@RequestParam(value="yearTo", defaultValue=EMPTY_PARAMETER) int yearTo){

		//result
		List<Measurement> measurements = new ArrayList<Measurement>();
				
		//requested dates
		List<Date> dates = handleInput(monthFrom,yearFrom, monthTo,yearTo);
		
				
		//fetch by date (month_year)
		for(Date date:dates){
			
			List<Measurement> measurementsByDate = cache.find(potId, date, MeasurementType.MOISTURE);
			
			if(measurementsByDate == null || measurementsByDate.isEmpty()){
				measurementsByDate = remoteSource.listPotMeasurementByTypeDate(potId, date, MeasurementType.MOISTURE);
				cache.store(potId, date, MeasurementType.MOISTURE, measurementsByDate);
			}
			measurements.addAll(measurementsByDate);
		}
		
		return measurements;
		
	}
	
	private List<Date> handleInput(int monthFrom, int yearFrom, int monthTo, int yearTo){
		
				
		//fill empty input data
		if(EMPTY_PARAMETER.equals(String.valueOf(monthFrom)) ||
			EMPTY_PARAMETER.equals(String.valueOf(yearFrom))){
			
			Calendar now = Calendar.getInstance();
			
			monthFrom = now.get(Calendar.MONTH) + 1;
			yearFrom = now.get(Calendar.YEAR);
			
		}
		
		if(EMPTY_PARAMETER.equals(String.valueOf(monthTo)))
			monthTo = monthFrom;
		if(EMPTY_PARAMETER.equals(String.valueOf(yearTo)))
			yearTo = yearFrom;
		
		// validations
		if(!(monthFrom >= 1 && monthFrom <=12) || !(monthTo >= 1 && monthTo <=12))
			throw new InvalidInputException(ErrorCodes.MONTH_NOT_BETWEEN_RANGE);
		if(!(yearFrom >= 2010) || !(yearTo >= 2010))
			throw new InvalidInputException(ErrorCodes.YEAR_NOT_BETWEEN_RANGE);
		if(monthFrom > monthTo)
			throw new InvalidInputException(ErrorCodes.MONTH_RANGE_NEGATIVE);
		if(yearFrom > yearTo)
			throw new InvalidInputException(ErrorCodes.YEAR_RANGE_NEGATIVE);
		
		List<Date> dates = Date.createDateRange(monthFrom, yearFrom, monthTo, yearTo);
		
		if(dates.size() > 6)
			throw new InvalidInputException(ErrorCodes.DATE_RANGE_TOO_BIG);
		
		return dates;
	}
	
	@ExceptionHandler(InvalidInputException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public void handleInvalidInputException(InvalidInputException ex){
		
		Logger logger = LoggerFactory.getLogger(PotsController.class);
	    logger.error(ex.getMessage());
		
	}
	
	@ExceptionHandler(SourceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleSourceException(SourceException ex){
		
		Logger logger = LoggerFactory.getLogger(PotsController.class);
	    logger.error(ex.getMessage());
		
	}
	

}
