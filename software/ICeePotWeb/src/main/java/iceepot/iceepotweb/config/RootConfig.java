package iceepot.iceepotweb.config;

import iceepot.iceepotweb.sources.MeasurementsSource;
import iceepot.iceepotweb.sources.RemoteSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages={"iceepot.iceepotweb.sources"}, excludeFilters={@Filter(type=FilterType.ANNOTATION, value=EnableWebMvc.class)})
public class RootConfig {
	
	public static String REMOTE_SOURCE_HOST = "homeplants.ddns.net";
	public static int REMOTE_SOURCE_PORT = 3629;
	public static int REMOTE_SOURCE_TIMEOUT = 30;
	
	@Bean
	public MeasurementsSource getRemoteSource(){
		return new RemoteSource(REMOTE_SOURCE_HOST, REMOTE_SOURCE_PORT, REMOTE_SOURCE_TIMEOUT);
	}

	
}
