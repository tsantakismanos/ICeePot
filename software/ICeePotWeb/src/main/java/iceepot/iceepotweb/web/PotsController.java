package iceepot.iceepotweb.web;

import iceepot.iceepotweb.model.Measurement;
import iceepot.iceepotweb.model.MeasurementType;
import iceepot.iceepotweb.sources.MeasurementsSource;
import iceepot.iceepotweb.sources.SourceException;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class PotsController {
	
	private MeasurementsSource remoteSource;
	
	public PotsController() {
	
	}

	@Autowired
	public PotsController(MeasurementsSource remoteSource) {
		
		this.remoteSource = remoteSource;
	}
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String hello(){
		return "hello";
	}


	@RequestMapping(value="/{potId}/moisture", method=RequestMethod.GET)
	public HashMap<Long, Double> potMoisture(
			@PathVariable("potId") int potId,
			@RequestParam("monthFrom") int monthFrom,
			@RequestParam("yearFrom") int yearFrom,
			@RequestParam("monthTo") int monthTo,
			@RequestParam("yearTo") int yearTo){
	
		List<Measurement> measurements;
				
		if(monthFrom == monthTo && yearFrom == yearTo)
			measurements = remoteSource.getByMonthNYear(monthFrom, yearFrom, potId, MeasurementType.MOISTURE);
		else
			measurements = remoteSource.getByRange(monthFrom, yearFrom, monthTo, yearTo, potId, MeasurementType.MOISTURE);
		
		return Measurement.getHashMap(measurements);
		
	}
	
	@ExceptionHandler(SourceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleSourceException(SourceException ex){
		
		Logger logger = LoggerFactory.getLogger(PotsController.class);
	    logger.error(ex.getMessage());
		
	}
	

}
