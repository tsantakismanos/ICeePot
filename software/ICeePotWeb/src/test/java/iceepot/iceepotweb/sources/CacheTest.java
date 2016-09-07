package iceepot.iceepotweb.sources;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import iceepot.iceepotweb.config.RootConfig;
import iceepot.iceepotweb.model.Date;
import iceepot.iceepotweb.model.Measurement;
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
public class CacheTest {

	@Autowired
	Cache cache;
	
	@Test
	public void shouldNotBeNull(){
		assertNotNull(cache);
	}
		
	@Test
	public void TestNonEmptyCache(){
		
		int potId=1;
		MeasurementType type = MeasurementType.MOISTURE;
		
		List<Date> dates = initializeCache(3, 1000, potId, type);
		
		for(Date date : dates){
			assertTrue(cache.find(potId, date, type).size() == 1000);
		}
		
	}
	
	@Test
	public void TestEmptyCache(){
		
		int potId=1;
		MeasurementType type = MeasurementType.MOISTURE;
		
		List<Date> dates = initializeCache(0, 1000, potId, type);
		
		for(Date date : dates){
			assertTrue(cache.find(potId, date, type).size() == 0 );
		}
		
	}
	
	private List<Date> initializeCache(int numberOfDates, int numberOfMeasurements, int potId, MeasurementType type){
		
		//Test dates
		List<Date> dates = new ArrayList<Date>();
						
		for(int i=0;i<numberOfDates;i++){
			dates.add(new Date(i,2013+i));
		
		
			//Test measurements
			List<Measurement> measurements = new ArrayList<Measurement>();
			for(int j=0;j<numberOfMeasurements;j++)
				measurements.add(new Measurement(new java.util.Date().getTime(), j*numberOfMeasurements));
			
			//save to cache
			cache.store(potId, dates.get(i), type, measurements);
		}		
		
		return dates;
	}
	
}
