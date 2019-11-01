package com.varsql.app.common.web;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.common.enums.ViewPage;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: AbstractController.java
* @DESC		: base controller
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 10. 31. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public abstract class AbstractController {
	public static final String REDIRECT_PREFIX = "redirect:";
	public static final String FORWARD_PREFIX = "forward:";
	
	/**
	 * 
	 * @Method Name  : getRedirectUrl
	 * @Method 설명 : get redirect url
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31. 
	 * @변경이력  :
	 * @param url
	 * @return
	 */
	private static String getRedirectUrl (String url) {
		return String.format("%s%s", REDIRECT_PREFIX, url);
	}
	
	/**
	 * 
	 * @Method Name  : getForwardUrl
	 * @Method 설명 : get forward url
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31. 
	 * @변경이력  :
	 * @param url
	 * @return
	 */
	private static String getForwardUrl (String url) {
		return String.format("%s%s", FORWARD_PREFIX, url);
	}
	
	public ModelAndView getRedirectModelAndView(String viewPath) {
		return getModelAndView(getRedirectUrl(viewPath) ,null);
	}
	
	public ModelAndView getForwardModelAndView(String viewPath) {
		return getModelAndView(getForwardUrl(viewPath) ,null);
	}
	
	public ModelAndView getModelAndView(String viewPath, ViewPage pageType) {
		return getModelAndView(viewPath, pageType,null);
	}
	
	public ModelAndView getModelAndView(String viewPath, ViewPage pageType, ModelMap model) {
		ModelAndView mav = null;
		
		String viewName = viewPath;
		if(pageType != null) {
			viewName = pageType.getPagePath(viewPath);
		}
		
		if(model != null) {
			mav = new ModelAndView(viewName ,model);
		}else {
			mav = new ModelAndView(viewName);
		}
		
		return mav; 
	}
	
	public String getPagePath(ViewPage pageType,String viewPath) {
		return pageType.getPagePath(viewPath);
	}
	
	
}
