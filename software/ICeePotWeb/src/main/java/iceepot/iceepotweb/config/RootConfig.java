package iceepot.iceepotweb.config;

import iceepot.iceepotweb.sources.MeasurementsSource;
import iceepot.iceepotweb.sources.RemoteSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages={"iceepot.iceepotweb.sources"}, excludeFilters={@Filter(type=FilterType.ANNOTATION, value=EnableWebMvc.class)})
public class RootConfig {
	
	
	@Bean
	@Profile("dev")
	public MeasurementsSource getDevRemoteSource(){
		return new RemoteSource("homeplants.ddns.net", 3629, 30);
	}

	@Bean
	@Profile("prod")
	public MeasurementsSource getProdRemoteSource(){
		return new RemoteSource("homeplants.ddns.net", 3629, 30);
	}
	
	@Bean
	@Profile("test")
	public MeasurementsSource getTestRemoteSource(){
		return new RemoteSource("homeplants.ddns.net", 3629, 30);
	}
	
	@Bean
	@Profile("error")
	public MeasurementsSource getErrorRemoteSource(){
		return new RemoteSource("homeplants.ddns.net", 3622, 30);
	}
}
