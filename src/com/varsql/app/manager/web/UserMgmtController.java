package com.varsql.app.manager.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.manager.service.UserMgmtServiceImpl;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.core.common.util.SecurityUtil;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/manager/user")
public class UserMgmtController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(UserMgmtController.class);
	
	@Autowired
	UserMgmtServiceImpl userMgmtServiceImpl;
	
	@RequestMapping({"/userList"})
	public @ResponseBody ResponseResult userList(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		
		return userMgmtServiceImpl.selectUserList(searchParameter);
	}
	
	@RequestMapping({"/acceptYn"})
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
		ParamMap param = HttpUtils.getServletRequestParam(req);
		
		param.put("viewid", viewid);
		param.put("userId", SecurityUtil.loginInfo().getUid());
		
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
	@RequestMapping(value = "/removeAuth", method=RequestMethod.POST)
	public @ResponseBody ResponseResult removeAuth(@RequestParam(value = "VIEWID", required = true )  String viewid ,@RequestParam(value = "VCONNID", required = true )  String vconnid, HttpServletRequest req) throws Exception {
		ParamMap param = HttpUtils.getServletRequestParam(req);
		param.put("viewid", viewid);
		param.put("vconnid", vconnid);
		
		return userMgmtServiceImpl.removeAuth(param);
	}
}
