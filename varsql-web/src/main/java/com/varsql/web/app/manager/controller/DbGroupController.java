package com.varsql.web.app.manager.controller;

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

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.app.manager.service.DbGroupServiceImpl;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.dto.db.DBGroupRequestDTO;
import com.varsql.web.util.VarsqlUtils;
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
	private DbGroupServiceImpl dbGroupServiceImpl;

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
	public @ResponseBody ResponseResult save(@Valid DBGroupRequestDTO dbGroupInfo, BindingResult result,HttpServletRequest req) throws Exception {
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
	public @ResponseBody ResponseResult delete(@RequestParam(value = "groupId", required = true) String groupId) throws Exception {
		return dbGroupServiceImpl.deleteDbGroupInfo(groupId);
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

		return dbGroupServiceImpl.groupNDbMappingList(groupId);
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


		return dbGroupServiceImpl.updateGroupNDbMappingInfo(selectItem ,groupId ,mode);
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
	@RequestMapping({"/dbGroupUserMappingList"})
	public @ResponseBody ResponseResult dbGroupUserMappingList(@RequestParam(value = "groupId", required = true) String groupId) throws Exception {
		return dbGroupServiceImpl.groupNUserMappingList(groupId);
	}

	/**
	 * @method  : addDbGroupUser
	 * @desc : db 그룹 사용자 추가 삭제.
	 * @author   : ytkim
	 * @date   : 2019. 8. 16.
	 * @param vconid
	 * @param selectItem
	 * @param mode
	 * @param req
	 * @return
	 */
	@RequestMapping({"/addDbGroupUser"})
	public @ResponseBody ResponseResult addDbGroupUser(@RequestParam(value = "groupId", required = true) String groupId
			,@RequestParam(value = "selectItem", required = true)  String selectItem
			,@RequestParam(value = "mode", required = true , defaultValue = "del") String mode
			) {

		return dbGroupServiceImpl.updateGroupNUserMappingInfo(selectItem, groupId, mode);
	}
}
