package iceepot.iceepotweb.sources;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import iceepot.iceepotweb.config.RootConfig;
import iceepot.iceepotweb.model.Date;
import iceepot.iceepotweb.model.MeasurementType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootConfig.class)
@ActiveProfiles("test")
public class RemoteSourceTest {

	@Autowired
	Source remoteSource;
	
	@Test
	public void shouldNotBeNull(){
		assertNotNull(remoteSource);
	}
		
	@Test
	public void shouldReturnMoistureByMonth(){
		
		try {
			assertTrue(remoteSource.listPotMeasurementByTypeDate(0,new Date(1,2014),MeasurementType.MOISTURE).size() != 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
