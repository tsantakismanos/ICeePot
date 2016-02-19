package iceepot.iceepotweb.web;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class PotsControllerTest {

	@Test
	public void testPotsController() throws Exception{
		
		PotsController potsController = new PotsController();
		
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(potsController).build(); 
				
		mockMvc.perform(MockMvcRequestBuilders.get("/pots/0"));
	}
	
}
