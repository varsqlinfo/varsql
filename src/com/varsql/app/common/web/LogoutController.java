package com.varsql.app.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.common.enums.ViewPage;



/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: LogoutController.java
* @DESC		: 로그아웃 컨트롤러 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 11. 1. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
public class LogoutController extends AbstractController {

	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getModelAndView("/loginForm",ViewPage.LOGIN);
	}

}
