package com.varsql.web.app.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.app.manager.service.DbGroupServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.db.DBGroupRequestDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
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

	private final Logger logger = LoggerFactory.getLogger(DbGroupController.class);

	@Autowired
	private DbGroupServiceImpl dbGroupServiceImpl;
	
	/**
	 *
	 * @Method Name  : dbGroupMgmt
	 * @Method 설명 : db 그룹 관리
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 9.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = {"", "/","/main"})
	public ModelAndView dbGroupMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "dbGroupMgmt");
		return getModelAndView("/dbGroupMgmt", VIEW_PAGE.MANAGER_GROUP,model);
	}

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
	@RequestMapping(value = "/list", method = RequestMethod.POST)
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
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseResult save(@Valid DBGroupRequestDTO dbGroupInfo, BindingResult result,HttpServletRequest req) throws Exception {
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  DbGroupController save check {}",errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}
		
		return dbGroupServiceImpl.saveDbGroupInfo(dbGroupInfo);
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
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
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
	@RequestMapping(value = "/dbGroupMappingList", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbGroupMappingList(@RequestParam(value = "groupId", required = true) String groupId, HttpServletRequest req) throws Exception {

		return dbGroupServiceImpl.groupNDbMappingList(groupId, HttpUtils.getSearchParameter(req));
	}

	/**
	 *
	 * @Method Name  : groupDbMapping
	 * @Method 설명 : 그룹에 db 맵핑
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
	@RequestMapping(value = "/groupDbMapping", method = RequestMethod.POST)
	public @ResponseBody ResponseResult groupDbMapping(@RequestParam(value = "selectItem", required = true)  String selectItem
			,@RequestParam(value = "groupId", required = true) String groupId
			,@RequestParam(value = "mode", required = true , defaultValue = "del") String mode
			, HttpServletRequest req
			) throws Exception {
		
		ResponseResult result = dbGroupServiceImpl.updateGroupNDbMappingInfo(selectItem ,groupId ,mode);
		
		boolean reusltFlag = result.getItem(); 
		if(!reusltFlag) return result; 
		
		return dbGroupServiceImpl.groupNDbMappingList(groupId, HttpUtils.getSearchParameter(req));
	}
	
	/**
	 *
	 * @Method Name  : dbUserMgmt
	 * @Method 설명 : db 그룹 사용자 관리.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/groupUser")
	public ModelAndView dbUserMgmt(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "dbGroupMgmt");
		return getModelAndView("/dbGroupUserMgmt", VIEW_PAGE.MANAGER_GROUP,model);
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
	@RequestMapping(value = "/dbGroupUserMappingList", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbGroupUserMappingList(@RequestParam(value = "groupId", required = true) String groupId, HttpServletRequest req) throws Exception {
		return dbGroupServiceImpl.groupNUserMappingList(groupId, HttpUtils.getSearchParameter(req));
	}

	/**
	 * @method  : dbGroupUserMapping
	 * @desc : db 그룹 사용자 추가 삭제.
	 * @author   : ytkim
	 * @date   : 2019. 8. 16.
	 * @param vconid
	 * @param selectItem
	 * @param mode
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/dbGroupUserMapping", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbGroupUserMapping(@RequestParam(value = "groupId", required = true) String groupId
			,@RequestParam(value = "selectItem", required = true)  String selectItem
			,@RequestParam(value = "mode", required = true , defaultValue = "del") String mode
			,HttpServletRequest req
			) {
		ResponseResult result = dbGroupServiceImpl.updateGroupNUserMappingInfo(selectItem, groupId, mode);
		
		boolean reusltFlag = result.getItem(); 
		if(!reusltFlag) return result; 
		
		return dbGroupServiceImpl.groupNUserMappingList(groupId, HttpUtils.getSearchParameter(req));
		
	}
}
