package com.varsql.web.app.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.app.manager.service.UserMgmtServiceImpl;
import com.varsql.web.app.user.beans.PasswordForm;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ParamMap;
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

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(UserMgmtController.class);

	@Autowired
	UserMgmtServiceImpl userMgmtServiceImpl;

	@RequestMapping({"/userList"})
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
		DataCommonVO paramMap = new DataCommonVO();

		paramMap.put("acceptyn", acceptyn);
		paramMap.put("selectItem", selectItem);

		return userMgmtServiceImpl.updateAccept(paramMap);
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
	public @ResponseBody ResponseResult blockYn(@RequestParam(value = "userid", required = true )  String userid
			,@RequestParam(value = "blockYn", required = true )  String blockYn
			) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();

		VarsqlUtils.setDefaultParam(paramMap);
		paramMap.put("userid", userid);
		paramMap.put("blockYn", "N".equals(blockYn)?"N":"Y");

		return userMgmtServiceImpl.updateBlockYn(paramMap);
	}

	/**
	 *
	 * @Method Name  : initPassword
	 * @Method 설명 : 패스워드 초기화
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 30.
	 * @변경이력  :
	 * @param viewid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/initPassword"})
	public @ResponseBody ResponseResult initPassword(@RequestParam(value = "VIEWID", required = true )  String viewid) throws Exception {

		PasswordForm userForm = new PasswordForm();

		userForm.setViewid(viewid);

		return userMgmtServiceImpl.initPassword(userForm);
	}

	/**
	 *
	 * @Method Name  : userDetail
	 * @Method 설명 : 사용자 상세 보기.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 30.
	 * @변경이력  :
	 * @param viewid
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userDetail", method=RequestMethod.POST)
	public @ResponseBody ResponseResult userDetail(@RequestParam(value = "VIEWID", required = true )  String viewid , HttpServletRequest req) throws Exception {
		ParamMap param = VarsqlUtils.getIncludeDefaultParam(req);

		param.put("viewid", viewid);

		return userMgmtServiceImpl.userDetail(param);
	}

	/**
	 *
	 * @Method Name  : removeAuth
	 * @Method 설명 : db 권한 삭제.
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
	public @ResponseBody ResponseResult removeAuth(@RequestParam(value = "VIEWID", required = true )  String viewid ,@RequestParam(value = "VCONNID", required = true )  String vconnid, HttpServletRequest req) throws Exception {
		ParamMap param = VarsqlUtils.getIncludeDefaultParam(req);
		param.put("viewid", viewid);
		param.put("vconnid", vconnid);

		return userMgmtServiceImpl.removeAuth(param);
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
	public @ResponseBody ResponseResult removeDbGroup(@RequestParam(value = "VIEWID", required = true )  String viewid
			,@RequestParam(value = "GROUP_ID", required = true )  String groupId, HttpServletRequest req) throws Exception {
		ParamMap param = VarsqlUtils.getIncludeDefaultParam(req);
		param.put("viewid", viewid);
		param.put("groupId", groupId);

		return userMgmtServiceImpl.removeDbGroup(param);
	}
}
