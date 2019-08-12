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
import com.varsql.app.manager.service.DbnUserServiceImpl;
import com.varsql.app.manager.service.UserMgmtServiceImpl;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.core.common.util.SecurityUtil;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.DateUtils;
import com.vartech.common.utils.HttpUtils;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/manager/comm")
public class ManagerCommonController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ManagerCommonController.class);
	
	@Autowired
	DbnUserServiceImpl dbnUserServiceImpl;
	
	@RequestMapping({"/dbList"})
	public @ResponseBody ResponseResult dbList(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		
		searchParameter.addCustomParam(UserConstants.ROLE, SecurityUtil.loginRole(req));
		searchParameter.addCustomParam(UserConstants.UID, SecurityUtil.loginId(req));
		return dbnUserServiceImpl.selectdbList(searchParameter);
	}
}
