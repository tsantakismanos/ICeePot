package iceepot.iceepotweb.web;

import iceepot.iceepotweb.model.Measurement;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pots")
public class PotsController {

	
	
	@RequestMapping(value="/{potId}/moisture", method=RequestMethod.GET)
	public HashMap<Long, Double> potMoisture(
			@PathVariable("potId") long potId,
			@RequestParam("timeFrom") long timeFrom,
			@RequestParam("timeTo") long timeTo){
	
		ArrayList<Measurement> measurements = new ArrayList<Measurement>();
		
		return Measurement.getHashMap(measurements);
		
	}

}
