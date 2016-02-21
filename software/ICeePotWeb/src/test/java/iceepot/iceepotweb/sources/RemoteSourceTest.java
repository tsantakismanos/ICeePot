package iceepot.iceepotweb.sources;

import static org.junit.Assert.*;
import iceepot.iceepotweb.config.RootConfig;
import iceepot.iceepotweb.sources.RemoteSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootConfig.class)
public class RemoteSourceTest {

	@Autowired
	RemoteSource remoteSource;
	
	@Test
	public void shouldNotBeNull(){
		assertNotNull(remoteSource);
	}
	
	@Test
	public void shouldMatchValues(){
		assertEquals(RootConfig.REMOTE_SOURCE_HOST, remoteSource.getHost());
		assertEquals(RootConfig.REMOTE_SOURCE_PORT, remoteSource.getPort());
		assertEquals(RootConfig.REMOTE_SOURCE_TIMEOUT, remoteSource.getTimeout());
	}
	
	@Test
	public void shouldReturnByMonth(){
		
		try {
			assertTrue(remoteSource.getByMonthNYear(1, 2014, 0).size() != 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void shouldReturnByRange(){
		
		try {
			assertTrue(remoteSource.getByRange(1,2014, 2, 2014, 0).size() != 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
