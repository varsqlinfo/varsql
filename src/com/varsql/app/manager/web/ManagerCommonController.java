package com.varsql.app.manager.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.UserConstants;
import com.varsql.app.manager.service.ManagerCommonServiceImpl;
import com.varsql.app.manager.service.UserMgmtServiceImpl;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.core.common.util.SecurityUtil;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.DateUtils;
import com.vartech.common.utils.HttpUtils;



/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ManagerCommonController.java
* @DESC		: 매니저 공통 컨트롤러 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 8. 16. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/manager/comm")
public class ManagerCommonController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ManagerCommonController.class);
	
	@Autowired
	ManagerCommonServiceImpl managerCommonServiceImpl;
	
	/**
	 * 
	 * @Method Name  : dbList
	 * @Method 설명 : 권한 있는 db list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16. 
	 * @변경이력  :
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/dbList"})
	public @ResponseBody ResponseResult dbList(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		
		searchParameter.addCustomParam(UserConstants.ROLE, SecurityUtil.loginRole(req));
		searchParameter.addCustomParam(UserConstants.UID, SecurityUtil.loginId(req));
		return managerCommonServiceImpl.selectdbList(searchParameter);
	}
	
	@RequestMapping({"/userList"})
	public @ResponseBody ResponseResult userList(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		
		searchParameter.addCustomParam(UserConstants.ROLE, SecurityUtil.loginRole(req));
		searchParameter.addCustomParam(UserConstants.UID, SecurityUtil.loginId(req));
		return managerCommonServiceImpl.selectUserList(searchParameter);
	}
}
