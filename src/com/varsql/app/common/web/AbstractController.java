package com.varsql.app.common.web;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.common.constants.ViewPageConstants;
import com.varsql.app.common.enums.VIEW_PAGE;

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
public abstract class AbstractController implements Controller{
	
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
		return String.format("%s%s", ViewPageConstants.REDIRECT_PREFIX, url);
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
		return String.format("%s%s", ViewPageConstants.FORWARD_PREFIX, url);
	}
	
	protected ModelAndView getRedirectModelAndView(String viewPath) {
		return getModelAndView(getRedirectUrl(viewPath) ,null);
	}
	
	protected ModelAndView getForwardModelAndView(String viewPath) {
		return getModelAndView(getForwardUrl(viewPath) ,null);
	}
	
	/**
	 * 
	 * @Method Name  : getModelAndView
	 * @Method 설명 : 일반 mav
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 18. 
	 * @변경이력  :
	 * @param viewPath
	 * @param pageType
	 * @return
	 */
	protected ModelAndView getModelAndView(String viewPath, VIEW_PAGE pageType) {
		return getModelAndView(viewPath, pageType,null);
	}
	
	protected ModelAndView getModelAndView(String viewPath, VIEW_PAGE pageType, ModelMap model) {
		return modelAndView(viewPath,pageType, model); 
	}
	
	/**
	 * 
	 * @Method Name  : getDialogModelAndView
	 * @Method 설명 : 다이얼로그 mav
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 18. 
	 * @변경이력  :
	 * @param viewPath
	 * @param pageType
	 * @return
	 */
	protected ModelAndView getDialogModelAndView(String viewPath, VIEW_PAGE pageType) {
		return getDialogModelAndView(viewPath, pageType,null);
	}
	
	protected ModelAndView getDialogModelAndView(String viewPath, VIEW_PAGE pageType, ModelMap model) {
		return modelAndView(String.format("%s%s",viewPath,ViewPageConstants.DIALOG_SUFFIX),pageType, model); 
	}
	
	/**
	 * 
	 * @Method Name  : getPopupModelAndView
	 * @Method 설명 : 팝업 mav
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 18. 
	 * @변경이력  :
	 * @param viewPath
	 * @param pageType
	 * @return
	 */
	protected ModelAndView getPopupModelAndView(String viewPath, VIEW_PAGE pageType) {
		return getDialogModelAndView(viewPath, pageType,null);
	}
	protected ModelAndView getPopupModelAndView(String viewPath, VIEW_PAGE pageType, ModelMap model) {
		return modelAndView(String.format("%s%s",viewPath ,ViewPageConstants.POPUP_SUFFIX),pageType, model); 
	}
	
	private ModelAndView modelAndView(String viewPath, VIEW_PAGE pageType, ModelMap model) {
		ModelAndView mav = null;
		
		String viewName = viewPath;
		if(pageType != null) {
			viewName = pageType.getViewPage(viewPath);
		}
		
		if(model != null) {
			mav = new ModelAndView(viewName ,model);
		}else {
			mav = new ModelAndView(viewName);
		}
		
		return mav; 
	}
	
	protected String getViewPage(VIEW_PAGE pageType,String viewPath) {
		return viewPage(pageType,viewPath);
	}
	
	protected String getDialogViewPage(VIEW_PAGE pageType,String viewPath) {
		return viewPage(pageType,String.format("%s%s", viewPath ,pageType));
	}
	
	protected String getPopupViewPage(VIEW_PAGE pageType,String viewPath) {
		return viewPage(pageType,String.format("%s%s", viewPath ,ViewPageConstants.POPUP_SUFFIX));
	}
	
	private String viewPage(VIEW_PAGE pageType,String viewPath) {
		return pageType.getViewPage(viewPath);
	}
	
}
