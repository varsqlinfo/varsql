package com.varsql.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.varsql.core.configuration.ConfigurationFilePath;
import com.varsql.web.configuration.ShutdownHookConfiguration;


@SpringBootApplication 
@ServletComponentScan
public class VarsqlApplication extends SpringBootServletInitializer {
	
	static {
		ConfigurationFilePath.getInstance().setSystemProperties();
	}

	@Override
    public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext); 
    }

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(VarsqlApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(VarsqlApplication.class, args);
	}
	
	@Bean(destroyMethod = "destroy")
    public ShutdownHookConfiguration shutdownHookConfiguration() {
        return new ShutdownHookConfiguration();
    }
}

