package com.varsql.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@ServletComponentScan
public class VarsqlApplication extends SpringBootServletInitializer {

	static {
		String catalinaHome = System.getProperty("catalina.home" ,"");

		if("".equals(catalinaHome)) {
			System.setProperty("varsql.runtime", "local");
			System.setProperty("com.varsql.install.root", "C:/zzz/resources/");
			System.setProperty("spring.devtools.restart.enabled", "true");
			System.setProperty("spring.devtools.livereload.enable", "true");
		}
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
