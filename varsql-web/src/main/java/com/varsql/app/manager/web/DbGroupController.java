package com.varsql.app.manager.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.web.AbstractController;
import com.varsql.app.manager.beans.DbGroupInfo;
import com.varsql.app.manager.service.DbGroupServiceImpl;
import com.varsql.app.util.VarsqlUtils;
import com.varsql.core.common.util.SecurityUtil;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.utils.HttpUtils;



/**
 * 
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DbGroupController.java
* @DESC		: db 그룹 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2019. 7. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/manager/dbGroup")
public class DbGroupController extends AbstractController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(DbGroupController.class);
	
	@Autowired
	DbGroupServiceImpl dbGroupServiceImpl;

	/**
	 * 
	 * @Method Name  : list
	 * @Method 설명 : 목록
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/list"})
	public @ResponseBody ResponseResult list(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		return dbGroupServiceImpl.selectDbGroupList(searchParameter);
	}
	
	/**
	 * 
	 * @Method Name  : save
	 * @Method 설명 : 추가.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/save"})
	public @ResponseBody ResponseResult save(@Valid DbGroupInfo dbGroupInfo, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  DbGroupController save check {}",errorVal.toString());
			}
			resultObject.setResultCode(ResultConst.CODE.DATA_NOT_VALID.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}else{
			resultObject = dbGroupServiceImpl.saveDbGroupInfo(dbGroupInfo);
		}
		
		return resultObject;
	}
	
	/**
	 * 
	 * @Method Name  : delete
	 * @Method 설명 : 삭제
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/delete"})
	public @ResponseBody ResponseResult delete(HttpServletRequest req) throws Exception {
		return dbGroupServiceImpl.deleteDbGroupInfo(VarsqlUtils.getIncludeDefaultParam(req));
	}
	
	/**
	 * 
	 * @Method Name  : dbGroupMappingList
	 * @Method 설명 : db 그룹 맵핑 목록. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 12. 
	 * @변경이력  :
	 * @param vconid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/dbGroupMappingList"})
	public @ResponseBody ResponseResult dbGroupMappingList(@RequestParam(value = "groupId", required = true) String groupId) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		paramMap.put("groupId", groupId);
		
		return dbGroupServiceImpl.selectDbGroupMappingList(paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : addDbGroupMappingInfo
	 * @Method 설명 : db 그룹에 db 맵핑 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16. 
	 * @변경이력  :
	 * @param selectItem
	 * @param groupId
	 * @param mode
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/addDbGroupMappingInfo"})
	public @ResponseBody ResponseResult addDbGroupMappingInfo(@RequestParam(value = "selectItem", required = true)  String selectItem
			,@RequestParam(value = "groupId", required = true) String groupId
			,@RequestParam(value = "mode", required = true , defaultValue = "del") String mode
			, HttpServletRequest req
			) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("selectItem", selectItem);
		paramMap.put("groupId", groupId);
		paramMap.put("mode", mode);
		
		return dbGroupServiceImpl.updateDbGroupMappingInfo(paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : dbGroupnuserMappingList
	 * @Method 설명 : db그룹  & 사용자 맵핑 목록.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16. 
	 * @변경이력  :
	 * @param vconid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/dbGroupnuserMappingList"})
	public @ResponseBody ResponseResult dbGroupnuserMappingList(@RequestParam(value = "groupId", required = true) String groupId) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		paramMap.put("groupId", groupId);
		
		return dbGroupServiceImpl.selectDbGroupUserMappingList(paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : addDbGroupUser
	 * @Method 설명 : db 그룹 사용자 추가 삭제. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16. 
	 * @변경이력  :
	 * @param selectItem
	 * @param vconid
	 * @param mode
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/addDbGroupUser"})
	public @ResponseBody ResponseResult addDbGroupUser(@RequestParam(value = "groupId", required = true) String vconid
			,@RequestParam(value = "selectItem", required = true)  String selectItem
			,@RequestParam(value = "mode", required = true , defaultValue = "del") String mode
			, HttpServletRequest req
			) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("selectItem", selectItem);
		paramMap.put("groupId", vconid);
		paramMap.put("uid", SecurityUtil.loginId(req));
		paramMap.put("mode", mode);
		
		return dbGroupServiceImpl.updateDbGroupUser(paramMap);
	}
}
