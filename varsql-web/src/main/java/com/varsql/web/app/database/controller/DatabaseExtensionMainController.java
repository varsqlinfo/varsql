package com.varsql.web.app.database.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;



/**
 * Database extension controller
* 
* @fileName	: DatabaseExtensionMainController.java
* @author	: ytkim
 */
@Controller
@RequestMapping("/database/extension")
public class DatabaseExtensionMainController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(DatabaseExtensionMainController.class);

	@RequestMapping(value={"/detailView"}, method = RequestMethod.GET)
	public ModelAndView mainpage() throws Exception {
		return getModelAndView("/detailView",VIEW_PAGE.DATABASE_EXTENSION );
	}

}
