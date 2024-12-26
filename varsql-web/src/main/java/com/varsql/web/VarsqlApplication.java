package com.varsql.web;

import java.io.File;

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
//		try {
//			int newSetup = 2; 
//			String configPath = "c:/zzz/aavarsql"; 
//			
//			if(newSetup < 1 && new File(configPath).exists()) {
//				for(File file : new File(configPath).listFiles()) {
//					if(!"logs".equals(file.getName())) {
//						if(file.isDirectory()) {
//							org.apache.commons.io.FileUtils.deleteDirectory(file);
//						}else {
//							file.delete();
//						}
//					}
//				}
//			}
//			System.setProperty("com.varsql.resource.root", configPath);
//			
//		} catch (java.io.IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
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

