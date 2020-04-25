package com.varsql.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class VarsqlApplication extends SpringBootServletInitializer {

	static {
		System.setProperty("com.varsql.install.root", "C:/zzz/resources/");
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
}
