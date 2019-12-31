package com.varsql.app.database.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.common.enums.VIEW_PAGE;
import com.varsql.app.common.web.AbstractController;
import com.varsql.app.database.beans.PreferencesInfo;
import com.varsql.app.database.service.PreferencesServiceImpl;
import com.vartech.common.app.beans.ResponseResult;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DatabasePreferencesController.java
* @DESC		: 환경설정 정보. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/database/preferences")
public class DatabasePreferencesController extends AbstractController  {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(DatabasePreferencesController.class);
	
	@Autowired
	private PreferencesServiceImpl preferencesServiceImpl;

	@RequestMapping({"/main"})
	public ModelAndView main(@RequestParam(value = "vconnid", required = true, defaultValue = "" )  String vconnid, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		return getModelAndView("/preferencesMain", VIEW_PAGE.DATABASE, model);
	}
	
	/**
	* @Method	: generalSetting
	* @Method설명	: 일반 설정. 
	* @작성일		: 2017. 1. 9.
	* @AUTHOR	: ytkim
	* @변경이력	: 
	* @param vconnid
	* @param mav
	* @return
	* @throws Exception
	 */
	@RequestMapping("/generalSetting")
	public ModelAndView generalSetting(@RequestParam(value = "vconnid", required = true, defaultValue = "" )  String vconnid, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		return getModelAndView("/generalSetting", VIEW_PAGE.DATABASE_PREFERENCES, model);
	}
	
	/**
	* @Method	: keySetting
	* @Method설명	: 단축키 설정.  
	* @작성일		: 2017. 1. 16.
	* @AUTHOR	: ytkim
	* @변경이력	: 
	* @param vconnid
	* @param mav
	* @return
	* @throws Exception
	 */
	@RequestMapping("/keySetting")
	public ModelAndView keySetting(@RequestParam(value = "vconnid", required = true, defaultValue = "" )  String vconnid, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		return getModelAndView("/keySetting", VIEW_PAGE.DATABASE_PREFERENCES, model);
	}
	
	
	
	/**
	* @Method	: sqlFormatSetting
	* @Method설명	: sql 자동생성 포켓 설정. 
	* @작성일		: 2017. 1. 9.
	* @AUTHOR	: ytkim
	* @변경이력	: 
	* @param vconnid
	* @param mav
	* @return
	* @throws Exception
	 */
	@RequestMapping("/sqlFormatSetting")
	public ModelAndView sqlFormatSetting(@RequestParam(value = "vconnid", required = true, defaultValue = "" )  String vconnid, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		return getModelAndView("/sqlFormatSetting", VIEW_PAGE.DATABASE_PREFERENCES, model);
	}
	
	/**
	* @Method	: codeEditerSetting
	* @Method설명	:  에디터 설정. 
	* @작성일		: 2017. 1. 16.
	* @AUTHOR	: ytkim
	* @변경이력	: 
	* @param vconnid
	* @param mav
	* @return
	* @throws Exception
	 */
	@RequestMapping("/codeEditerSetting")
	public ModelAndView codeEditerSetting(@RequestParam(value = "vconnid", required = true, defaultValue = "" )  String vconnid, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		return getModelAndView("/codeEditerSetting", VIEW_PAGE.DATABASE_PREFERENCES, model);
	}
	
	/**
	* @Method	: exportSetting
	* @Method설명	: 내보내기 설정.  
	* @작성일		: 2017. 1. 16.
	* @AUTHOR	: ytkim
	* @변경이력	: 
	* @param vconnid
	* @param mav
	* @return
	* @throws Exception
	 */
	@RequestMapping("/exportSetting")
	public ModelAndView exportSetting(@RequestParam(value = "vconnid", required = true, defaultValue = "" )  String vconnid, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		return getModelAndView("/exportSetting", VIEW_PAGE.DATABASE_PREFERENCES, model);
	}
	
	/**
	 * 
	 * @Method Name  : save
	 * @Method 설명 : 설정 정보 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 3. 16. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save")
	public @ResponseBody ResponseResult save(PreferencesInfo preferencesInfo, HttpServletRequest req) throws Exception {
		return preferencesServiceImpl.savePreferencesInfo(preferencesInfo); // 설정 정보 저장.
	}
	
}
