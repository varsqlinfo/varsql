package com.varsql.web.app.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.app.manager.service.UserMgmtServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: UserMgmtController.java
* @DESC		: 사용자 관리.
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 8. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/manager/user")
public class UserMgmtController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(UserMgmtController.class);

	@Autowired
	private UserMgmtServiceImpl userMgmtServiceImpl;
	
	@GetMapping(value = {"", "/","/main"})
	public ModelAndView joinForm(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "userMgmt");
		return getModelAndView("/userMgmt", VIEW_PAGE.MANAGER,model);
	}

	@RequestMapping(value = "/userList", method=RequestMethod.POST)
	public @ResponseBody ResponseResult userList(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);

		return userMgmtServiceImpl.selectUserList(searchParameter);
	}

	/**
	 *
	 * @Method Name  : updAcceptYn
	 * @Method 설명 : 접근 권한 셋팅
	 * @작성자   : ytkim
	 * @작성일   : 2018. 12. 7.
	 * @변경이력  :
	 * @param acceptyn
	 * @param selectItem
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/acceptYn", method=RequestMethod.POST)
	public @ResponseBody ResponseResult updAcceptYn(@RequestParam(value = "acceptyn", required = true )  String acceptyn
			,@RequestParam(value = "selectItem", required = true )  String selectItem
			) throws Exception {

		return userMgmtServiceImpl.updateAccept(acceptyn ,selectItem);
	}

	/**
	 *
	 * @Method Name  : blockYn
	 * @Method 설명 : 사용자 차뎐 여부.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 12. 7.
	 * @변경이력  :
	 * @param acceptyn
	 * @param selectItem
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/blockYn", method=RequestMethod.POST)
	public @ResponseBody ResponseResult blockYn(@RequestParam(value = "viewid", required = true )  String viewid
			,@RequestParam(value = "blockYn", required = true )  String blockYn
			) throws Exception {
		return userMgmtServiceImpl.updateBlockYn(viewid, blockYn);
	}

	/**
	 * @method  : initPassword
	 * @desc : 패스워드 초기화
	 * @author   : ytkim
	 * @date   : 2020. 4. 30.
	 * @param viewid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/initPassword", method=RequestMethod.POST)
	public @ResponseBody ResponseResult initPassword(@RequestParam(value = "viewid", required = true )  String viewid) throws Exception {
		return userMgmtServiceImpl.initPassword(viewid);
	}

	/**
	 * @method  : userDetail
	 * @desc : 사용자 상세 보기.
	 * @author   : ytkim
	 * @date   : 2020. 4. 30.
	 * @param viewid
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userDetail", method=RequestMethod.POST)
	public @ResponseBody ResponseResult userDetail(@RequestParam(value = "viewid", required = true )  String viewid) throws Exception {
		return userMgmtServiceImpl.userDetail(viewid);
	}

	/**
	 *
	 * @Method Name  : removeAuth
	 * @Method 설명 : 사용자 db 권한 차단.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 30.
	 * @변경이력  :
	 * @param viewid
	 * @param vconnid
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dbBlockInfo", method=RequestMethod.POST)
	public @ResponseBody ResponseResult userDbBlock(@RequestParam(value = "viewid", required = true )  String viewid
			,@RequestParam(value = "vconnid", required = true )  String vconnid
			,@RequestParam(value = "mode", required = true )  String mode
			, HttpServletRequest req) throws Exception {
		return userMgmtServiceImpl.userDbBlock(viewid, vconnid, VarsqlUtils.getIncludeDefaultParam(req));
	}

	/**
	 *
	 * @Method Name  : removeDbGroup
	 * @Method 설명 : db group 제거.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 26.
	 * @변경이력  :
	 * @param viewid
	 * @param vconnid
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/removeDbGroup", method=RequestMethod.POST)
	public @ResponseBody ResponseResult removeDbGroup(@RequestParam(value = "viewid", required = true )  String viewid
			,@RequestParam(value = "groupId", required = true )  String groupId, HttpServletRequest req) throws Exception {

		return userMgmtServiceImpl.removeDbGroup(groupId, viewid);
	}
}
