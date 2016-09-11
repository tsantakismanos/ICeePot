package iceepot.iceepotweb.config;

import iceepot.iceepotweb.sources.Cache;
import iceepot.iceepotweb.sources.Source;
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
	public Source getDevRemoteSource(){
		return new RemoteSource("homeplants.ddns.net", 3629, 30);
	}

	@Bean
	@Profile("prod")
	public Source getProdRemoteSource(){
		return new RemoteSource("homeplants.ddns.net", 3629, 30);
	}
	
	@Bean
	@Profile("test")
	public Source getTestRemoteSource(){
		return new RemoteSource("homeplants.ddns.net", 3629, 30);
	}
	
	@Bean
	@Profile("error")
	public Source getErrorRemoteSource(){
		return new RemoteSource("homeplants.ddns.net", 3622, 30);
	}
	
	@Bean
	public Cache getTestCache(){
		return new Cache();
	}
	
}
