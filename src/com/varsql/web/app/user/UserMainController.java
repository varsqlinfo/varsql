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

import com.varsql.web.common.constants.UserConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.HttpUtil;
import com.varsql.web.util.SecurityUtil;



/**
 * The Class OutsideController.
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
		model.addAttribute("originalURL", HttpUtil.getOriginatingRequestUri(req));
		model.addAttribute("dblist", SecurityUtil.loginInfo(req).getDatabaseInfo().values());
		
		return  new ModelAndView("/user/userMain",model);
	}
	
	@RequestMapping({"/searchUserList"})
	public @ResponseBody Map searchUserList(HttpServletRequest req
			,HttpServletResponse response
			) throws Exception {	
		
		DataCommonVO paramMap = HttpUtil.getAllParameter(req);
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
		
		DataCommonVO paramMap = HttpUtil.getAllParameter(req);
		paramMap.put(UserConstants.UID, SecurityUtil.loginId(req));
		
		return userMainServiceImpl.insertSendSqlInfo(paramMap);
	}

	@RequestMapping({"/message"})
	public @ResponseBody Map message(HttpServletRequest req
			,HttpServletResponse response
			) throws Exception {
		
		DataCommonVO paramMap = HttpUtil.getAllParameter(req);
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
		
		DataCommonVO paramMap = HttpUtil.getAllParameter(req);
		paramMap.put(UserConstants.UID, SecurityUtil.loginId(req));
		
		return userMainServiceImpl.updateMemoViewDate(paramMap);
	}
}
