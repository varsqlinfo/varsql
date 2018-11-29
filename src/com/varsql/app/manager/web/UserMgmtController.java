package com.varsql.app.manager.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.manager.service.UserMgmtServiceImpl;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.app.user.beans.UserForm;
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
	
	@RequestMapping({"/initPassword"})
	public @ResponseBody ResponseResult initPassword(@RequestParam(value = "VIEWID", required = true )  String viewid) throws Exception {
		
		PasswordForm userForm = new PasswordForm();
		
		userForm.setViewid(viewid);
		
		return userMgmtServiceImpl.initPassword(userForm);
	}
	
	@RequestMapping({"/userDetail"})
	public @ResponseBody ResponseResult userDetail(@RequestParam(value = "VIEWID", required = true )  String viewid) throws Exception {
		
		UserForm userForm = new UserForm();
		
		userForm.setViewid(viewid);
		
		return userMgmtServiceImpl.userDetail(userForm);
	}
}
