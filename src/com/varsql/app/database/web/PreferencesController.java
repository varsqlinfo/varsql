package com.varsql.app.database.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.database.service.PreferencesServiceImpl;

@Controller
@RequestMapping("/database/tools/preferences")
public class PreferencesController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(PreferencesController.class);
	
	@Autowired
	private PreferencesServiceImpl preferencesServiceImpl;

	@RequestMapping("/main")
	public ModelAndView main(@RequestParam(value = "vconnid", required = true, defaultValue = "" )  String vconnid, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		return  new ModelAndView("/database/tools/preferencesMain",model);
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
		return  new ModelAndView("/database/tools/preferences/generalSetting",model);
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
		return  new ModelAndView("/database/tools/preferences/keySetting",model);
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
		return  new ModelAndView("/database/tools/preferences/sqlFormatSetting",model);
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
		return  new ModelAndView("/database/tools/preferences/codeEditerSetting",model);
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
		return  new ModelAndView("/database/tools/preferences/exportSetting",model);
	}
}
