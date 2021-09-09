package com.varsql.web.app.database.controller;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.valueobject.BaseObjectInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.exception.BlockingUserException;
import com.varsql.core.exception.VarsqlAccessDeniedException;
import com.varsql.web.app.database.service.DatabaseServiceImpl;
import com.varsql.web.app.database.service.PreferencesServiceImpl;
import com.varsql.web.common.cache.CacheInfo;
import com.varsql.web.common.cache.CacheUtils;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.constants.VarsqlParamConstants;
import com.varsql.web.dto.db.DBConnTabRequestDTO;
import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.varsql.web.dto.user.UserPermissionInfoDTO;
import com.varsql.web.exception.DatabaseBlockingException;
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

		String vname = SecurityUtil.loginInfo(req).getDatabaseInfo().get(preferencesInfo.getConuid()).getName();
		if(!SecurityUtil.isAdmin()) {
			List<UserPermissionInfoDTO> permissionList	=databaseServiceImpl.findUserPermission();

			if(permissionList != null) {
				UserPermissionInfoDTO dto = permissionList.get(0);

				if(!dto.isAcceptYn()) { // 접근 수락 되지 않은 사용자 체크
					logger.warn("## Access denied user : {}", SecurityUtil.loginName());
					throw new VarsqlAccessDeniedException("access denied");
				}else if(dto.isBlockYn()) { // 차단된 사용자 체크
					logger.warn("## block user : {}", SecurityUtil.loginName());
					throw new BlockingUserException("block user");
				}

				// db 차단 체크
				permissionList.stream().filter(item->{
					if(preferencesInfo.getVconnid().equals(item.getVconnid())) {
						logger.warn("## database blocking user : {}, db : {}", SecurityUtil.loginName(), vname);
						throw new DatabaseBlockingException("database blocking");
					}
					return false;
				}).findAny().orElse(null);
			}
		}

		databaseServiceImpl.insertDbConnectionHistory(preferencesInfo); // 접속 로그.
		model.addAttribute(VarsqlParamConstants.SCREEN_CONFIG_INFO, databaseServiceImpl.schemas(preferencesInfo));
		model.addAttribute("vname", vname);

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

		if(databaseParamInfo.isRefresh()) {
			String cacheKey = CacheUtils.getObjectTypeKey(databaseParamInfo);
			Cache tableMetaCache = cacheManager.getCache(CacheInfo.CacheType.OBJECT_TYPE_METADATA.getCacheName());
			if(databaseParamInfo.getObjectNames() != null && databaseParamInfo.getObjectNames().length > 0) {

				ValueWrapper value = tableMetaCache.get(cacheKey);

				ResponseResult objValue = (ResponseResult)value.get();

				ResponseResult result = databaseServiceImpl.dbObjectList(databaseParamInfo);

				List<BaseObjectInfo> resultItems =result.getItems();

				HashMap<String , BaseObjectInfo> newItemInfos = new HashMap<String,BaseObjectInfo>();
				resultItems.stream().forEach(item ->{
					newItemInfos.put(item.getName(), item);
				});

				List<BaseObjectInfo> cacheItems = objValue.getItems();
				cacheItems = cacheItems.stream().map(item ->{
					if(newItemInfos.containsKey(item.getName())) {
						return newItemInfos.get(item.getName());
					}
					return item;
				}).collect(Collectors.toList());

				objValue.setItemList(cacheItems);;

				tableMetaCache.put(cacheKey, objValue);

				return result;
			}else {
				tableMetaCache.evict(cacheKey);
			}
		}

		return databaseServiceImpl.dbObjectList(databaseParamInfo) ;
	}

	/**
	 *
	 * @Method Name  : dbObjectMetadataList
	 * @Method 설명 : service menu 클릭했을때 그 메뉴에 대한 메타 데이터 가져오기.
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
