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

import com.varsql.core.configuration.PreferencesDataFactory;
import com.varsql.web.app.database.service.PreferencesServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.HttpParamConstants;
import com.varsql.web.constants.PreferencesConstants;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;

import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class DatabasePreferencesController extends AbstractController  {

	private final Logger logger = LoggerFactory.getLogger(DatabasePreferencesController.class);

	private final PreferencesServiceImpl preferencesServiceImpl;

	@RequestMapping(value={"/main"}, method = RequestMethod.GET)
	public ModelAndView main(@RequestParam(value = "conuid", required = true, defaultValue = "" )  String conuid, ModelAndView mav) throws Exception {
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
	@RequestMapping(value="/generalSetting", method = RequestMethod.GET)
	public ModelAndView generalSetting(@RequestParam(value = "conuid", required = true, defaultValue = "" )  String conuid, ModelAndView mav) throws Exception {
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
	@RequestMapping(value="/keySetting", method = RequestMethod.GET)
	public ModelAndView keySetting(@RequestParam(value = "conuid", required = true, defaultValue = "" )  String conuid, ModelAndView mav) throws Exception {
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
	@RequestMapping(value="/sqlFormatSetting", method = RequestMethod.GET)
	public ModelAndView sqlFormatSetting(@RequestParam(value = "conuid", required = true, defaultValue = "" )  String conuid, ModelAndView mav) throws Exception {
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
	@RequestMapping(value="/codeEditerSetting", method = RequestMethod.GET)
	public ModelAndView codeEditerSetting(@RequestParam(value = "conuid", required = true, defaultValue = "" )  String conuid, ModelAndView mav) throws Exception {
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
	@RequestMapping(value="/exportSetting", method = RequestMethod.GET)
	public ModelAndView exportSetting(@RequestParam(value = "conuid", required = true, defaultValue = "" )  String conuid, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		return getModelAndView("/exportSetting", VIEW_PAGE.DATABASE_PREFERENCES, model);
	}

	@RequestMapping(value="/contextMenuSetting", method = RequestMethod.GET)
	public ModelAndView contextMenuSetting(PreferencesRequestDTO preferencesInfo, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();

		preferencesInfo.setPrefKey(PreferencesConstants.PREFKEY.CONTEXTMENU_SERVICEOBJECT.key());

		String prefVal = preferencesServiceImpl.selectPreferencesInfo(preferencesInfo);

		model.addAttribute(HttpParamConstants.SETTING_INFO, prefVal == null? PreferencesDataFactory.getInstance().getDefaultValue(PreferencesConstants.PREFKEY.CONTEXTMENU_SERVICEOBJECT.key()) :prefVal);
		return getModelAndView("/contextMenuSetting", VIEW_PAGE.DATABASE_PREFERENCES, model);
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
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseResult save(PreferencesRequestDTO preferencesInfo, HttpServletRequest req) throws Exception {
		return preferencesServiceImpl.savePreferencesInfo(preferencesInfo); // 설정 정보 저장.
	}

	
	@RequestMapping(value = { "/convertTextSetting" }, method = { RequestMethod.GET })
	@ResponseBody
	public ResponseResult convertTextSetting(PreferencesRequestDTO preferencesInfo) throws Exception {
		preferencesInfo.setPrefKey(PreferencesConstants.PREFKEY.CONVERT_TEXT.key());
		String prefVal = this.preferencesServiceImpl.selectPreferencesInfo(preferencesInfo);

		return VarsqlUtils.getResponseResultItemOne((prefVal == null)
				? PreferencesDataFactory.getInstance().getDefaultValue(PreferencesConstants.PREFKEY.CONVERT_TEXT.key())
				: prefVal);
	}
}
