package com.varsql.web.app.database.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.db.DBType;
import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.sql.SQL;
import com.varsql.core.sql.template.SQLTemplateFactory;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DatabaseUtilsController.java
* @DESC		: database util controller.
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

	private final Logger logger = LoggerFactory.getLogger(DatabaseUtilsController.class);

	/**
	 *
	 * @Method Name  : genExcelToDDL
	 * @Method 설명 : excel -> ddl 변환
	 * @작성자   : ytkim
	 * @작성일   : 2018. 8. 24.
	 * @변경이력  :
	 * @param databaseParamInfo
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/genExcelToDDL", method = RequestMethod.GET)
	public ModelAndView genExcelToDDL(PreferencesRequestDTO preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		return getModelAndView("/genExcelToDDL", VIEW_PAGE.DATABASE_UTILS, mav.getModelMap());
	}

	/**
	 * @method  : sqlTemplate
	 * @desc : get sql template
	 * @author   : ytkim
	 * @date   : 2021. 3. 6.
	 * @param dbType
	 * @param templateType
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sqlTemplate", method = RequestMethod.POST)
	public @ResponseBody ResponseResult sqlTemplate(@RequestParam(value = "dbType", required = true) String dbType
			, @RequestParam(value = "templateType", required = true) String templateType
			, HttpServletRequest req) throws Exception {

		return VarsqlUtils.getResponseResultItemOne(SQLTemplateFactory.getInstance().getTemplate(DBType.getDBType(dbType), SQL.CREATE.getTemplateId(ObjectType.getDBObjectType(templateType))));
	}

}
