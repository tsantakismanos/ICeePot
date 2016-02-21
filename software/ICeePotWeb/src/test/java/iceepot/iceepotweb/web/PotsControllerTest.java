package iceepot.iceepotweb.web;

import iceepot.iceepotweb.config.WebConfig;

import java.util.Date;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ContextConfiguration(classes={WebConfig.class})
public class PotsControllerTest {

	@Test
	public void testPotsController() throws Exception{
		
		performPotTestUnit(new PotTestUnit(0, (new Date()).getTime() , (new Date()).getTime()));
		performPotTestUnit(new PotTestUnit(0, (new Date()).getTime() , (new Date()).getTime()));
		performPotTestUnit(new PotTestUnit(0, (new Date()).getTime() , (new Date()).getTime()));
		performPotTestUnit(new PotTestUnit(0, (new Date()).getTime() , (new Date()).getTime()));
	}
	
	
	private void performPotTestUnit(PotTestUnit potTestUnit) throws Exception{
		PotsController potsController = new PotsController();
		
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(potsController).build(); 
		
		mockMvc.perform(MockMvcRequestBuilders
						.get("/pots/" + String.valueOf(potTestUnit.getPotId()) + "/moisture")
						.param("timeFrom", String.valueOf(potTestUnit.getTimeFrom()))
						.param("timeTo",String.valueOf(potTestUnit.getTimeTo())))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	
	
	private class PotTestUnit{
		
		private int potId;
		private long timeFrom;
		private long timeTo;
		
		public PotTestUnit(int potId, long timeFrom, long timeTo) {
		
			this.potId = potId;
			this.timeFrom = timeFrom;
			this.timeTo = timeTo;
		}

		public int getPotId() {
			return potId;
		}

		public long getTimeFrom() {
			return timeFrom;
		}

		public long getTimeTo() {
			return timeTo;
		}
		
		
		
	}
	
}
