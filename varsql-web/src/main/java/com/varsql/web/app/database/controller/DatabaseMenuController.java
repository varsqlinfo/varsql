package com.varsql.web.app.database.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
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
	@RequestMapping({"/fileImportExport"})
	public ModelAndView mainpage(PreferencesRequestDTO preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();

		if(VarsqlUtils.isAjaxRequest(req)) {
			return getDialogModelAndView("/file/fileImportExport",VIEW_PAGE.DATABASE_MENU , model);
		}else {
			return getPopupModelAndView("/file/fileImportExport",VIEW_PAGE.DATABASE_MENU , model);
		}
	}
}
