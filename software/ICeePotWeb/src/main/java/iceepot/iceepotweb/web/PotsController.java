package iceepot.iceepotweb.web;

import iceepot.iceepotweb.model.Measurement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/pots")
public class PotsController {

	
	
	@RequestMapping(value="/{potId}", 
					method=RequestMethod.GET,
					produces="application/json")
	public @ResponseBody List<Measurement> pot(
			@PathVariable("potId") long potId,
			@RequestParam("timeFrom") long timeFrom,
			@RequestParam("timeTo") long timeTo
			){
	
		List<Measurement> measurements = new ArrayList<Measurement>();
		
		
		
		return measurements;
		
	}
}
