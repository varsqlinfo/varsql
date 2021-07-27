package com.varsql.web.app.database.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.web.app.database.service.DatabaseServiceImpl;
import com.varsql.web.app.database.service.PreferencesServiceImpl;
import com.varsql.web.common.cache.CacheInfo;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.constants.VarsqlParamConstants;
import com.varsql.web.dto.db.DBConnTabRequestDTO;
import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.HttpUtils;



/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DatabaseController.java
* @DESC		: database controller
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/database")
public class DatabaseController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(DatabaseController.class);

	@Autowired
	private DatabaseServiceImpl databaseServiceImpl;

	@Autowired
	private PreferencesServiceImpl preferencesServiceImpl;
	
	@Autowired
	@Qualifier(ResourceConfigConstants.CACHE_MANAGER)
	private CacheManager cacheManager;


	@RequestMapping(value={"/","/main"}, method = RequestMethod.GET)
	public ModelAndView mainpage(PreferencesRequestDTO preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();

		model.addAttribute(VarsqlParamConstants.SCREEN_CONFIG_INFO, databaseServiceImpl.schemas(preferencesInfo));
		model.addAttribute("vname", SecurityUtil.loginInfo(req).getDatabaseInfo().get(preferencesInfo.getConuid()).getName());

		databaseServiceImpl.insertDbConnectionHistory(preferencesInfo); // 접속 로그.
		
		model.addAttribute(VarsqlParamConstants.DATABASE_SCREEN_SETTING, VarsqlUtils.mapToJsonObjectString(preferencesServiceImpl.findMainSettingInfo(preferencesInfo)));

		return getModelAndView("/main",VIEW_PAGE.DATABASE , model);
	}

	/**
	 *
	 * @Method Name  : serviceMenu
	 * @Method 설명 : servicemenu
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 6.
	 * @변경이력  :
	 * @param databaseParamInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/serviceMenu", method = RequestMethod.POST)
	public @ResponseBody ResponseResult serviceMenu(DatabaseParamInfo databaseParamInfo, HttpServletRequest req) throws Exception {
		return databaseServiceImpl.serviceMenu(databaseParamInfo);
	}

	/**
	 *
	 * @Method Name  : dbObjectList
	 * @Method 설명 : db object list
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 6.
	 * @변경이력  :
	 * @param databaseParamInfo
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dbObjectList", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbObjectList(DatabaseParamInfo databaseParamInfo, HttpServletRequest req) throws Exception {
		
		cacheManager.getCache(CacheInfo.CacheType.TABLE_METADATA.getCacheName());
		
		if(databaseParamInfo.isRefresh()) {
			databaseServiceImpl.dbObjectListCacheEvict(databaseParamInfo);
		}
		
		return databaseServiceImpl.dbObjectList(databaseParamInfo);
	}

	/**
	 *
	 * @Method Name  : dbObjectMetadataList
	 * @Method 설명 : service menu 클릭했을때 그 메뉴에 대한 메타 데이타 가져오기.
	 * @작성일   : 2015. 4. 13.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param req
	 * @param vconnid
	 * @param schema
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dbObjectMetadataList", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbObjectMetadataList(DatabaseParamInfo databaseParamInfo, HttpServletRequest req) throws Exception {

		return databaseServiceImpl.dbObjectMetadataList(databaseParamInfo);
	}

	/**
	 *
	 * @Method Name  : createDDL
	 * @Method 설명 : 생성 스크립트.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 6.
	 * @변경이력  :
	 * @param databaseParamInfo
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/createDDL", method = RequestMethod.POST)
	public @ResponseBody ResponseResult createDDL(DatabaseParamInfo databaseParamInfo, HttpServletRequest req) throws Exception {

		return databaseServiceImpl.createDDL(databaseParamInfo);

	}

	/**
	 *
	 * @Method Name  : dbInfo
	 * @Method 설명 : db 정보
	 * @작성자   : ytkim
	 * @작성일   : 2018. 10. 8.
	 * @변경이력  :
	 * @param databaseParamInfo
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dbInfo", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbInfo(DatabaseParamInfo databaseParamInfo, HttpServletRequest req) throws Exception {
		return databaseServiceImpl.dbInfo(databaseParamInfo);
	}

	@RequestMapping(value = "/connTabInfo", method = RequestMethod.POST)
	public @ResponseBody ResponseResult connTabInfo(DBConnTabRequestDTO dbConnTabRequestDTO, HttpServletRequest req) throws Exception {
		dbConnTabRequestDTO.setCustom(HttpUtils.getServletRequestParam(req));
		return databaseServiceImpl.connTabInfo(dbConnTabRequestDTO);
	}
}
