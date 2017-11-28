package com.varsql.web.app.user;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.common.util.SecurityUtil;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.common.constants.UserConstants;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.utils.HttpUtils;



/**
 * 
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: UserMainController.java
* @DESC		: 사용자 관리 컨트롤러. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 11. 28. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/user")
public class UserMainController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(UserMainController.class);
	
	@Autowired
	UserMainServiceImpl userMainServiceImpl;
	
	@RequestMapping({""})
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("redirect:/user/");
	}
	
	@RequestMapping({"/","/main"})
	public ModelAndView mainpage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("role", SecurityUtil.loginRole(req));
		paramMap.put("uid", SecurityUtil.loginId(req));
		
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		model.addAttribute("dblist", SecurityUtil.loginInfo(req).getDatabaseInfo().values());
		
		return  new ModelAndView("/user/userMain",model);
	}
	
	@RequestMapping({"/preferences"})
	public ModelAndView preferencesMain(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		
		return  new ModelAndView("/user/preferences/general", model);
	}
	
	@RequestMapping({"/preferences/password"})
	public ModelAndView preferencesPassword(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		
		return  new ModelAndView("/user/preferences/password", model);
	}
	
	@RequestMapping({"/searchUserList"})
	public @ResponseBody Map searchUserList(HttpServletRequest req
			,HttpServletResponse response
			) throws Exception {	
		
		ParamMap paramMap = HttpUtils.getServletRequestParam(req);
		paramMap.put(UserConstants.UID, SecurityUtil.loginId(req));
		
		return userMainServiceImpl.selectSearchUserList(paramMap);
	}
	
	/**
	 * sql 정보 보내기.
	 * @param vconnid
	 * @param req
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/sendSql"})
	public @ResponseBody Map sendSql(HttpServletRequest req
			,HttpServletResponse response
			) throws Exception {
		
		ParamMap paramMap = HttpUtils.getServletRequestParam(req);
		paramMap.put(UserConstants.UID, SecurityUtil.loginId(req));
		
		return userMainServiceImpl.insertSendSqlInfo(paramMap);
	}

	@RequestMapping({"/message"})
	public @ResponseBody Map message(HttpServletRequest req
			,HttpServletResponse response
			) throws Exception {
		
		ParamMap paramMap = HttpUtils.getServletRequestParam(req);
		paramMap.put(UserConstants.UID, SecurityUtil.loginId(req));
		
		return userMainServiceImpl.selectMessageInfo(paramMap);
	}
	/**
	 * 메시지 본 날짜 업데이트
	 * @param req
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/updMsgViewDt"})
	public @ResponseBody Map updMsgViewDt(HttpServletRequest req
			,HttpServletResponse response
			) throws Exception {
		
		ParamMap paramMap = HttpUtils.getServletRequestParam(req);
		paramMap.put(UserConstants.UID, SecurityUtil.loginId(req));
		
		return userMainServiceImpl.updateMemoViewDate(paramMap);
	}
}
