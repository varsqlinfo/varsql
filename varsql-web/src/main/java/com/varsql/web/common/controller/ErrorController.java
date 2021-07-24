package com.varsql.web.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.constants.VIEW_PAGE;
import com.vartech.common.app.beans.ResponseResult;



/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ErrorController.java
* @DESC		: error controller
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2015. 4. 18. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/error")
public class ErrorController extends AbstractController {

	/** The Constant logger. */
	private final Logger logger = LoggerFactory.getLogger(ErrorController.class);

	@RequestMapping(value = {"", "/"}, method ={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView errorMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getModelAndView("/error/defaultErrorPage", VIEW_PAGE.COMMONPAGE);
	}

	@RequestMapping(value = "/error403", method ={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView error403(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getModelAndView("/error/error403", VIEW_PAGE.COMMONPAGE);
	}

	@RequestMapping(value = "/page403csrf", method ={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView page403csrf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getModelAndView("/error/page403csrf", VIEW_PAGE.COMMONPAGE);
	}

	@RequestMapping(value = "/error404", method ={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView error404(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getModelAndView("/error/error404", VIEW_PAGE.COMMONPAGE);
	}

	@RequestMapping(value = "/error500", method ={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView error500(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getModelAndView("/error/error500", VIEW_PAGE.COMMONPAGE);
	}
	/**
	 *
	 * @Method Name  : connError
	 * @Method 설명 : db 커넥션 에러
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 1.
	 * @변경이력  :
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/connError", method ={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView connError(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getModelAndView("/error/connError", VIEW_PAGE.COMMONPAGE);
	}

	/**
	 *
	 * @Method Name  : connCreateError
	 * @Method 설명 : db 커넥션 생성 에러
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 1.
	 * @변경이력  :
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/connCreateError", method ={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView connCreateError(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getModelAndView("/error/connCreateError", VIEW_PAGE.COMMONPAGE);
	}

	/**
	 *
	 * @Method Name  : blockUser
	 * @Method 설명 : 차단된 사용자
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 1.
	 * @변경이력  :
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/blockUser", method ={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView blockUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getModelAndView("/error/blockUser", VIEW_PAGE.COMMONPAGE);
	}

	/**
	 *
	 * @Method Name  : invalidDatabase
	 * @Method 설명 : 유효하지않은 database ajax
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 1.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/invalidDatabase", method ={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody ResponseResult invalidDatabase(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ResponseResult result = new ResponseResult();
		result.setStatus(500);
		result.setResultCode(2000);
		result.setMessage("invalidDatabase");

		return result;
	}

	/**
	 *
	 * @Method Name  : invalidDatabasePage
	 * @Method 설명 : 유효하지않은 database
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 1.
	 * @변경이력  :
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/invalidDatabasePage", method ={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView invalidDatabasePage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getModelAndView("/error/invalidDatabase", VIEW_PAGE.COMMONPAGE);
	}
	
	/**
	 *
	 * @Method Name  : dataDownloadError
	 * @Method 설명 : data download exception
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 1.
	 * @변경이력  :
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dataDownloadError", method ={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView dataDownloadError(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getModelAndView("/error/dataDownloadError", VIEW_PAGE.COMMONPAGE);
	}
}
