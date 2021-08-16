package com.varsql.web.app.database.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.user.PreferencesRequestDTO;



/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DatabaseExtensionMainController.java
* @DESC		: Database extension main controller
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/database/extension")
public class DatabaseExtensionMainController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(DatabaseExtensionMainController.class);

	@RequestMapping(value={"/detailView"}, method = RequestMethod.GET)
	public ModelAndView mainpage(PreferencesRequestDTO preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		return getModelAndView("/detailView",VIEW_PAGE.DATABASE_EXTENSION );
	}

}
