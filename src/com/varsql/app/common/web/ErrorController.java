package com.varsql.app.common.web;

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
		ModelAndView model = new ModelAndView();
		model.setViewName("/commonPage/error/error403");
		return model;
	}
	
	@RequestMapping(value = "/page403csrf")
	public ModelAndView page403csrf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName("/commonPage/error/page403csrf");
		return model;
	}
	
	@RequestMapping(value = "/error404")
	public ModelAndView error404(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName("/commonPage/error/error404");
		return model;
	}
	
	@RequestMapping(value = "/error500")
	public ModelAndView error500(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName("/commonPage/error/error500");
		return model;
	}
	
	@RequestMapping(value = "/connError")
	public ModelAndView connError(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName("/commonPage/error/connError");
		return model;
	}
	
	@RequestMapping(value = "/connCreateError")
	public ModelAndView connCreateError(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName("/commonPage/error/connCreateError");
		return model;
	}
	
	@RequestMapping(value = "/blockUser")
	public ModelAndView blockUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName("/commonPage/error/blockUser");
		return model;
	}
}
