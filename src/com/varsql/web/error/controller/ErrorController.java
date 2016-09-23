package com.varsql.web.error.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/error")
public class ErrorController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

	@RequestMapping(value = "/error403")
	public ModelAndView ModelAndView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("===========error403============");
		System.out.println("===========error403============");
		ModelAndView model = new ModelAndView();
		
		model.setViewName("/error/error403");
		return model;
	}
	
	@RequestMapping(value = "/error404")
	public String error404(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("===========error404============");
		System.out.println("===========error404============");
		return "/error/error404";
	}
	
	@RequestMapping(value = "/error500")
	public String error500(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("===========error500============");
		System.out.println("===========error500============");
		return "/error/error500";
	}
}
