package iceepot.iceepotweb.sources;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import iceepot.iceepotweb.config.RootConfig;
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
	MeasurementsSource remoteSource;
	
	@Test
	public void shouldNotBeNull(){
		assertNotNull(remoteSource);
	}
		
	@Test
	public void shouldReturnMoistureByMonth(){
		
		try {
			assertTrue(remoteSource.getByMonthNYear(1, 2014, 0, MeasurementType.MOISTURE).size() != 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void shouldReturnMoistureByRange(){
		
		try {
			assertTrue(remoteSource.getByRange(1,2014, 2, 2014, 0, MeasurementType.MOISTURE).size() != 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
