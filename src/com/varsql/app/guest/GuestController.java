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
import com.varsql.common.util.SecurityUtil;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/guest")
public class GuestController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(GuestController.class);
	
	@Autowired
	private GuestServiceImpl guestServiceImpl;

	@RequestMapping({""})
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("redirect:/guest/");
	}
	
	@RequestMapping({"/","/main"})
	public ModelAndView mainpage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		return  new ModelAndView("/guest/guestMain");
	}
	
	@RequestMapping(value = "/qnaList")
	public @ResponseBody Map qnalist(HttpServletRequest req 
			,@RequestParam(value = "searchVal", required = false, defaultValue = "" )  String searchVal
			,@RequestParam(value = "page", required = false, defaultValue = "1" )  int page
			,@RequestParam(value = "rows", required = false, defaultValue = "10" )  int rows
		) throws Exception {
	
		DataCommonVO paramMap = new DataCommonVO();
		paramMap.put("page", page);
		paramMap.put("rows", rows);
		paramMap.put("searchVal", searchVal);
	
		paramMap.put("uid", SecurityUtil.loginId(req));
		
		return guestServiceImpl.selectQna(paramMap);
	}
	
	@RequestMapping(value="/insQna")
	public ModelAndView qna(HttpServletRequest req 
			,@RequestParam(value = "title", required = true)  String title
			,@RequestParam(value = "question", required = false )  String question) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("title", title);
		paramMap.put("question", question);
		paramMap.put("cre_id", SecurityUtil.loginId(req));
		
		guestServiceImpl.insertQnaInfo(paramMap);
		
		return  new ModelAndView("redirect:/guest/");
	}
	
	@RequestMapping(value="/delQna")
	public ModelAndView qnaDelete(@RequestParam(value = "qnaid")  String qnaid) throws Exception {
		
		DataCommonVO dcv = new DataCommonVO();
		
		dcv.put("qnaid", qnaid);
		
		guestServiceImpl.deleteQnaInfo(dcv);
		
		return new ModelAndView("redirect:/guest/");
	}
	
	@RequestMapping(value="/updQna")
	public ModelAndView qnaUpdate(HttpServletRequest req 
			,@RequestParam(value = "qnaid")  String qnaid
			,@RequestParam(value = "title", required = true)  String title
			,@RequestParam(value = "question", required = false )  String question) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("qnaid", qnaid);
		paramMap.put("title", title);
		paramMap.put("question", question);
		paramMap.put("cre_id", SecurityUtil.loginId(req));
		
		guestServiceImpl.updateQnaInfo(paramMap);
		
		return new ModelAndView("redirect:/guest/");
	}
	
	@RequestMapping(value = "/detailQna")
	public @ResponseBody Map dbDetail(@RequestParam(value = "qnaid") String qnaid) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("qnaid", qnaid);
		
		return guestServiceImpl.selectDetailQna(paramMap);
	}
	
	
}
