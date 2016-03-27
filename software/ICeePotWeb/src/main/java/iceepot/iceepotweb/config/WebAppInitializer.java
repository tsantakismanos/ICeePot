package iceepot.iceepotweb.config;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends
		AbstractAnnotationConfigDispatcherServletInitializer {

	
	
	@Override
	protected WebApplicationContext createRootApplicationContext() {
		
		WebApplicationContext context = super.createRootApplicationContext();
		
		((ConfigurableEnvironment)context.getEnvironment()).setActiveProfiles("prod");
		
		return context;
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		
		return new Class<?>[] {RootConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		
		return new Class<?>[] {WebConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		
		return new String[]{"/"};
	}

}
