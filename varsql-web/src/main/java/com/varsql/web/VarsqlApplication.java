package com.varsql.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
@ServletComponentScan
public class VarsqlApplication extends SpringBootServletInitializer {

	static {
		System.setProperty("com.varsql.install.root", "C:/zzz/resources/");
		System.setProperty("spring.devtools.restart.enabled", "true");
		System.setProperty("spring.devtools.livereload.enable", "true");
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
