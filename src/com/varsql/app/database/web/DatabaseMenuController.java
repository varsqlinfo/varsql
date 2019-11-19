package com.varsql.app.database.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.common.enums.VIEW_PAGE;
import com.varsql.app.common.web.AbstractController;
import com.varsql.app.database.beans.PreferencesInfo;

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
	@RequestMapping({"/fileImportExport"})
	public ModelAndView mainpage(PreferencesInfo preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();
		
		return getDialogModelAndView("/fileImportExport",VIEW_PAGE.DATABASE , model);
	}
}
