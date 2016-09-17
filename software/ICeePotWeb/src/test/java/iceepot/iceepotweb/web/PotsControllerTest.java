package iceepot.iceepotweb.web;

import iceepot.iceepotweb.config.RootConfig;
import iceepot.iceepotweb.config.WebConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
public class PotsControllerTest {
	
	@Autowired
	PotsController potsController;

	@Test
	public void JanuaryToFebruaryTest() throws Exception{
		performSuccessfulPotTestUnit(new PotTestUnit(0, 1, 2014, 2, 2014));
	}
	
	@Test
	public void JanuaryTest() throws Exception{
		performSuccessfulPotTestUnit(new PotTestUnit(0, 1, 2014, Integer.parseInt(PotsController.EMPTY_PARAMETER), Integer.parseInt(PotsController.EMPTY_PARAMETER)));
	}
	
	@Test
	public void JanuaryOnlyTest() throws Exception{
		performSuccessfulPotTestUnit(new PotTestUnit(0, 1, 2014, 1, 2014));
	}
	
	@Test
	public void DecemberOnlyTest() throws Exception{
		performSuccessfulPotTestUnit(new PotTestUnit(0, 12, 2014, 12, 2014));
	}
	
	@Test
	public void MarchToMayTest() throws Exception{
		performSuccessfulPotTestUnit(new PotTestUnit(0, 3, 2014, 5, 2014));
	}
	
	@Test
	public void AprilToAugustTest() throws Exception{
		performSuccessfulPotTestUnit(new PotTestUnit(0, 4, 2014, 8, 2014));
	}
	
	@Test
	public void InvalidMonthFromTest() throws Exception{
		performFailedPotTestUnit(new PotTestUnit(0, 0, 2014, 8, 2014));
	}
	
	@Test
	public void InvalidMonthToTest() throws Exception{
		performFailedPotTestUnit(new PotTestUnit(0, 4, 2014, 13, 2014));
	}
	
	@Test
	public void InvalidYearFromTest() throws Exception{
		performFailedPotTestUnit(new PotTestUnit(0, 4, 2000, 5, 2014));
	}
	
	@Test
	public void InvalidYearToTest() throws Exception{
		performFailedPotTestUnit(new PotTestUnit(0, 4, 2014, 5, 2000));
	}
	
	@Test
	public void InvalidMonthFromMonthToTest() throws Exception{
		performFailedPotTestUnit(new PotTestUnit(0, 4, 2014, 2, 2014));
	}
	
	@Test
	public void InvalidYearFromYearToTest() throws Exception{
		performFailedPotTestUnit(new PotTestUnit(0, 4, 2014, 2, 2013));
	}
	
	@Test
	public void TooBigRangeTest() throws Exception{
		performFailedPotTestUnit(new PotTestUnit(0, 4, 2014, 10, 2014));
	}
	
	public void CurrentMoistureTest() throws Exception{
		performSuccessfulPotTestUnit(new PotTestUnit(0, Integer.parseInt(PotsController.EMPTY_PARAMETER),Integer.parseInt(PotsController.EMPTY_PARAMETER),Integer.parseInt(PotsController.EMPTY_PARAMETER),Integer.parseInt(PotsController.EMPTY_PARAMETER)));
	}
	
	private void performFailedPotTestUnit(PotTestUnit potTestUnit) throws Exception{
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(potsController).build();
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/pots/" + String.valueOf(potTestUnit.getPotId()) + "/moisture")
				.param("monthFrom", String.valueOf(potTestUnit.getMonthFrom()))
				.param("yearFrom", String.valueOf(potTestUnit.getYearFrom()))
				.param("monthTo",String.valueOf(potTestUnit.getMonthTo()))
				.param("yearTo",String.valueOf(potTestUnit.getYearTo())))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	private void performSuccessfulPotTestUnit(PotTestUnit potTestUnit) throws Exception{
		
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
	
}
