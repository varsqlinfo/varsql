package com.varsql.web.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.constants.VIEW_PAGE;
import com.vartech.common.app.beans.ResponseResult;



/**
 * app setup controller
 * 
 * @author ytkim
 *
 */
@Controller
public class AppSetupController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(AppSetupController.class);
	

	@RequestMapping(value = "/setup", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getModelAndView("/setup", VIEW_PAGE.SETUP);
	}

//	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
//	@ResponseBody
//	public ResponseResult resetPasswordPost(@RequestParam(value = "token", required = true) String token
//			,@RequestParam(value = "upw", required = true) String upw, @RequestParam(value = "confirmUpw", required = true) String confirmUpw,
//			HttpServletRequest req) throws Exception {
//		
//		
//		return userCommonService.resetPassword(token, upw, confirmUpw);
//		
//	}
	
}
