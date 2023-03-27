package com.varsql.web.app.database.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.sql.SQLTemplateCode;
import com.varsql.core.sql.template.SQLTemplateFactory;
import com.varsql.web.app.database.service.PreferencesServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.common.service.UserCommonService;
import com.varsql.web.constants.PreferencesConstants;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.constants.VarsqlParamConstants;
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
	
	@Autowired
	private UserCommonService userCommonService;
	
	@Autowired
	private PreferencesServiceImpl preferencesServiceImpl;

	/**
	 * excel -> ddl 변환
	 *
	 * @method : genTable
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/genTable", method = RequestMethod.GET)
	public ModelAndView genTable(ModelAndView mav, HttpServletRequest req) throws Exception {
		return getModelAndView("/genTable", VIEW_PAGE.DATABASE_UTILS, mav.getModelMap());
	}

	/**
	 * get sql template
	 *
	 * @method : sqlTemplate
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
		
		return VarsqlUtils.getResponseResultItemOne(SQLTemplateFactory.getInstance().getTemplate(DBVenderType.getDBType(dbType), SQLTemplateCode.TABLE.create));
	}
	
	/**
	 * 다중 db sql 실행. 
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/multiDbSqlExecute", method = RequestMethod.GET)
	public ModelAndView multiDbSqlExecute(PreferencesRequestDTO preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("dblist", userCommonService.reloadDatabaseList());
		preferencesInfo.setPrefKey(PreferencesConstants.PREFKEY.MULTIPLE_DATABASE_SQLEXECUTE.key());
		model.addAttribute("settingInfo", preferencesServiceImpl.selectPreferencesInfo(preferencesInfo,true));
		
		return getModelAndView("/multiDbSqlExecute", VIEW_PAGE.DATABASE_UTILS, model);
	}
}
