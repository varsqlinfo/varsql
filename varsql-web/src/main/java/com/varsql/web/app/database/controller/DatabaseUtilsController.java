package com.varsql.web.app.database.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.varsql.core.connection.ConnectionInfoManager;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.sql.DDLTemplateCode;
import com.varsql.core.sql.template.DDLTemplateFactory;
import com.varsql.web.app.database.service.DatabaseSourceGenImpl;
import com.varsql.web.app.database.service.PreferencesServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.common.service.UserCommonService;
import com.varsql.web.constants.PreferencesConstants;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.varsql.web.util.SecurityUtil;
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
		
		return VarsqlUtils.getResponseResultItemOne(DDLTemplateFactory.getInstance().getTemplate(DBVenderType.getDBType(dbType), DDLTemplateCode.TABLE.create));
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
	
	/**
	 * 테이블 DDL 변경
	 * @param preferencesInfo
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/tableDDLConvert", method = RequestMethod.GET)
	public ModelAndView tableDDLConvert(PreferencesRequestDTO preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();
		
		DatabaseParamInfo dpi = new DatabaseParamInfo(SecurityUtil.userDBInfo(preferencesInfo.getConuid()));

		model.addAttribute("currentSchemaName", ConnectionInfoManager.getInstance().getConnectionInfo(dpi.getVconnid()).getSchema());
		model.addAttribute("dbTypeList", DBVenderType.values());

		if(SecurityUtil.isSchemaView(dpi)) {
			MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dpi.getDbType());
			
			DBVenderType venderType = DBVenderType.getDBType(dpi.getType());

			if(venderType.isUseDatabaseName()) {
				model.addAttribute("schemaList", dbMetaEnum.getDatabases(dpi));
			}else {
				model.addAttribute("schemaList", dbMetaEnum.getSchemas(dpi));
			}
		}else {
			model.addAttribute("schemaInfo", "");
		}
		
		return getModelAndView("/tableDDLConvert", VIEW_PAGE.DATABASE_UTILS, model);
	}
	
	/**
	 * 
	 * @method  : tableColumnSearch
	 * @desc : 테이블 컬럼 검색
	 * @author      : ytkim
	 * @param preferencesInfo
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/tableColumnSearch", method = RequestMethod.GET)
	public ModelAndView tableColumnSearch(PreferencesRequestDTO preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();
		
		DatabaseParamInfo dpi = new DatabaseParamInfo(SecurityUtil.userDBInfo(preferencesInfo.getConuid()));
		
		model.addAttribute("currentSchemaName", ConnectionInfoManager.getInstance().getConnectionInfo(dpi.getVconnid()).getSchema());
		
		if(SecurityUtil.isSchemaView(dpi)) {
			MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dpi.getDbType());
			
			DBVenderType venderType = DBVenderType.getDBType(dpi.getType());
			
			if(venderType.isUseDatabaseName()) {
				model.addAttribute("schemaList", dbMetaEnum.getDatabases(dpi));
			}else {
				model.addAttribute("schemaList", dbMetaEnum.getSchemas(dpi));
			}
		}else {
			model.addAttribute("schemaInfo", "");
		}
		
		return getModelAndView("/tableColumnSearch", VIEW_PAGE.DATABASE_UTILS, model);
	}
	
	
	/**
	 * excel -> ddl 변환
	 *
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/diff", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView diff(ModelAndView mav, HttpServletRequest req) throws Exception {
		return getModelAndView("/diff", VIEW_PAGE.DATABASE_UTILS, mav.getModelMap());
	}
}
