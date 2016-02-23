package iceepot.iceepotweb.web;

import iceepot.iceepotweb.config.RootConfig;
import iceepot.iceepotweb.config.WebConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={WebConfig.class, RootConfig.class})
@WebAppConfiguration
public class PotsControllerTest {
	
	@Autowired
	PotsController potsController;

	@Test
	public void JanuaryToFebruaryTest() throws Exception{
		performPotTestUnit(new PotTestUnit(0, 1, 2014, 2, 2014));
	}
	
	@Test
	public void JanuaryOnlyTest() throws Exception{
		performPotTestUnit(new PotTestUnit(0, 1, 2014, 1, 2014));
	}
	
	@Test
	public void MarchToMayTest() throws Exception{
		performPotTestUnit(new PotTestUnit(0, 3, 2014, 5, 2014));
	}
	
	@Test
	public void AprilToAugustTest() throws Exception{
		performPotTestUnit(new PotTestUnit(0, 4, 2014, 8, 2014));
	}
	
	
	private void performPotTestUnit(PotTestUnit potTestUnit) throws Exception{
		
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(potsController).build(); 
		
		mockMvc.perform(MockMvcRequestBuilders
						.get("/pots/" + String.valueOf(potTestUnit.getPotId()) + "/moisture")
						.param("monthFrom", String.valueOf(potTestUnit.getMonthFrom()))
						.param("yearFrom", String.valueOf(potTestUnit.getYearFrom()))
						.param("monthTo",String.valueOf(potTestUnit.getMonthTo()))
						.param("yearTo",String.valueOf(potTestUnit.getYearTo())))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	private class PotTestUnit{
		
		private int potId;
		private int monthFrom;
		private int yearFrom;
		private int monthTo;
		private int yearTo;
		
		public PotTestUnit(int potId, int monthFrom, int yearFrom, int monthTo,
				int yearTo) {
			this.potId = potId;
			this.monthFrom = monthFrom;
			this.yearFrom = yearFrom;
			this.monthTo = monthTo;
			this.yearTo = yearTo;
		}

		public int getPotId() {
			return potId;
		}

		public int getMonthFrom() {
			return monthFrom;
		}

		public int getYearFrom() {
			return yearFrom;
		}

		public int getMonthTo() {
			return monthTo;
		}

		public int getYearTo() {
			return yearTo;
		}
		
	}
	
}
