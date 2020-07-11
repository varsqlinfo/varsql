package com.varsql.web.app.database.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.web.app.database.service.ExportServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.PreferencesConstants;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.HttpUtils;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ExportController.java
* @DESC		: export 처리.
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 11. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/database/utils")
public class DatabaseUtilsController extends AbstractController  {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(DatabaseUtilsController.class);

	/**
	 *
	 * @Method Name  : specMain
	 * @Method 설명 : 명세서
	 * @작성자   : ytkim
	 * @작성일   : 2018. 8. 24.
	 * @변경이력  :
	 * @param databaseParamInfo
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/genExcelToDDL")
	public ModelAndView specMain(PreferencesRequestDTO preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {

		ModelMap model = mav.getModelMap();

		logger.debug("export specMain : {} ", preferencesInfo);

		return getModelAndView("/genExcelToDDL", VIEW_PAGE.DATABASE_UTILS, model);
	}
}
