package iceepot.iceepotweb.web;

import iceepot.iceepotweb.model.Measurement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pots")
public class PotsController {

	
	
	@RequestMapping(value="/{potId}", method=RequestMethod.GET)
	public List<Measurement> pot(
			@PathVariable("potId") long potId,
			@RequestParam("timeFrom") long timeFrom,
			@RequestParam("timeTo") long timeTo){
	
		List<Measurement> measurements = new ArrayList<Measurement>();
		
		return measurements;
		
	}

}
