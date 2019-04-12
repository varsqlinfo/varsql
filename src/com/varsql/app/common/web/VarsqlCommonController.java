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
 * 
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: VarsqlCommonController.java
* @DESC		: 공통 컨트롤러 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 2. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
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

	/**
	 * 
	 * @Method Name  : invalidLogin
	 * @Method 설명 : 로그인 유효하지 않을때.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 2. 28. 
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/invalidLogin")
	public @ResponseBody ResponseResult invalidLogin(HttpServletRequest req, HttpServletResponse res,
			ModelAndView mav) throws Exception {
		ResponseResult result = new ResponseResult();
		result.setStatus(500);
		result.setMessage("invalidLogin");
		result.setResultCode(ResultConst.CODE.LOGIN_INVALID.toInt());
		return result; 
	}
	
	/**
	 * 
	 * @Method Name  : helpPage
	 * @Method 설명 : 도움말
	 * @작성자   : ytkim
	 * @작성일   : 2018. 2. 28. 
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/helpPage")
	public ModelAndView helpPage(HttpServletRequest req, HttpServletResponse res,
			ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		return new ModelAndView("/commonPage/help/mainHelp", model);
	}

}