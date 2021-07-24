package com.varsql.web.app.database.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.varsql.web.util.VarsqlUtils;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: MenuController.java
* @DESC		: db 메뉴 컨트롤.
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 11. 18. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/database/menu")
public class DatabaseMenuController extends AbstractController  {
	
	@RequestMapping(value="/fileImport" , method = {RequestMethod.GET , RequestMethod.POST})
	public ModelAndView fileImport(PreferencesRequestDTO preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();

		if(VarsqlUtils.isAjaxRequest(req)) {
			return getDialogModelAndView("/file/fileImport",VIEW_PAGE.DATABASE_MENU , model);
		}else {
			return getPopupModelAndView("/file/fileImport",VIEW_PAGE.DATABASE_MENU , model);
		}
	}
	
	@RequestMapping(value="/fileExport" , method = {RequestMethod.GET , RequestMethod.POST})
	public ModelAndView fileExport(PreferencesRequestDTO preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();
		
		if(VarsqlUtils.isAjaxRequest(req)) {
			return getDialogModelAndView("/file/fileExport",VIEW_PAGE.DATABASE_MENU , model);
		}else {
			return getPopupModelAndView("/file/fileExport",VIEW_PAGE.DATABASE_MENU , model);
		}
	}
}
