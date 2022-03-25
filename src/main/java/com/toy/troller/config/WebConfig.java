package com.toy.troller.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String os = System.getProperty("os.name").toLowerCase();
		
		if(!os.contains("win")) {
			registry.addResourceHandler("/resources/**")
	        .addResourceLocations("file:///resources/");
		}
	    
    }

}
