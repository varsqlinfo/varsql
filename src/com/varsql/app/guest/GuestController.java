package com.varsql.app.guest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.UserConstants;
import com.varsql.app.user.service.UserMainServiceImpl;
import com.varsql.core.common.util.SecurityUtil;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/guest")
public class GuestController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(GuestController.class);
	
	@Autowired
	private UserMainServiceImpl userMainServiceImpl;

	@RequestMapping({""})
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("redirect:/guest/");
	}
	
	@RequestMapping({"/","/main"})
	public ModelAndView mainpage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		return  new ModelAndView("/guest/guestMain");
	}
	
	@RequestMapping(value = "/qnaList")
	public @ResponseBody ResponseResult qnalist(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		searchParameter.addCustomParam(UserConstants.UID, SecurityUtil.loginId(req));
	
		return userMainServiceImpl.selectQna(searchParameter);
	}
	
	@RequestMapping(value="/insQna")
	public @ResponseBody ResponseResult qna(HttpServletRequest req 
			,@RequestParam(value = "title", required = true)  String title
			,@RequestParam(value = "question", required = false )  String question) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("title", title);
		paramMap.put("question", question);
		paramMap.put("cre_id", SecurityUtil.loginId(req));
		
		return userMainServiceImpl.saveQnaInfo(paramMap, true);
	}
	
	@RequestMapping(value="/delQna")
	public @ResponseBody ResponseResult qnaDelete(@RequestParam(value = "qnaid")  String qnaid) throws Exception {
		
		DataCommonVO dcv = new DataCommonVO();
		dcv.put("qnaid", qnaid);
		
		return userMainServiceImpl.deleteQnaInfo(dcv);
	}
	
	@RequestMapping(value="/updQna")
	public @ResponseBody ResponseResult qnaUpdate(HttpServletRequest req 
			,@RequestParam(value = "qnaid")  String qnaid
			,@RequestParam(value = "title", required = true)  String title
			,@RequestParam(value = "question", required = false )  String question) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("qnaid", qnaid);
		paramMap.put("title", title);
		paramMap.put("question", question);
		paramMap.put("cre_id", SecurityUtil.loginId(req));
		
		return userMainServiceImpl.saveQnaInfo(paramMap, false);
	}
	
	@RequestMapping(value = "/detailQna")
	public @ResponseBody ResponseResult dbDetail(@RequestParam(value = "qnaid") String qnaid) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("qnaid", qnaid);
		
		return userMainServiceImpl.selectDetailQna(paramMap);
	}
}
