package com.varsql.web.app.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;



/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ManagerController.java
* @DESC		: 매니저 main
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 8. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/manager")
public class ManagerController extends AbstractController{

	private final Logger logger = LoggerFactory.getLogger(ManagerController.class);
	
	@GetMapping(value = {"", "/","/main"})
	public ModelAndView joinForm(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		return  getRedirectModelAndView(getViewPage(VIEW_PAGE.MANAGER,"/user"));
	}
}
