package com.varsql.web.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * The Class OutsideController.
 */
@Controller
public class VarsqlCommonController {

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory
			.getLogger(VarsqlCommonController.class);


	@RequestMapping(value = "/error401")
	public ModelAndView report(HttpServletRequest req, HttpServletResponse res,
			ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		return new ModelAndView("/error/error401", model);
	}

}