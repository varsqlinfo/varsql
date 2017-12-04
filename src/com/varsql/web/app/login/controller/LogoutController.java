package com.varsql.web.app.login.controller;

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
public class LogoutController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(LogoutController.class);

	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("/login/loginForm");
	}

}
