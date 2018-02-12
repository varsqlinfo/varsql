package com.varsql.app.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.ResultConst;

/**
 * The Class OutsideController.
 */
@Controller
public class VarsqlCommonController {

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory
			.getLogger(VarsqlCommonController.class);
	
	@RequestMapping({"","/"})
	public ModelAndView welcome(HttpServletRequest req, HttpServletResponse res,
			ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		return new ModelAndView("redirect:/login", model);
	}
	


	@RequestMapping(value = "/error401")
	public ModelAndView report(HttpServletRequest req, HttpServletResponse res,
			ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		return new ModelAndView("/error/error401", model);
	}
	
	@RequestMapping(value = "/invalidDatabase")
	public @ResponseBody ResponseResult InvalidDatabase(HttpServletRequest req, HttpServletResponse res,
			ModelAndView mav) throws Exception {
		ResponseResult result = new ResponseResult();
		result.setStatus(500);
		result.setResultCode(2000);
		result.setMessage("invalidDatabase");
		
		return result; 
	}
	
	@RequestMapping(value = "/invalidLogin")
	public @ResponseBody ResponseResult invalidLogin(HttpServletRequest req, HttpServletResponse res,
			ModelAndView mav) throws Exception {
		ResponseResult result = new ResponseResult();
		result.setStatus(500);
		result.setResultCode(1000);
		result.setMessage("login");
		return result; 
	}

}