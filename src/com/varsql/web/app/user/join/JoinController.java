package com.varsql.web.app.user.join;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.HttpUtil;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/join")
public class JoinController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(JoinController.class);
	
	@Autowired
	JoinService joinService;
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public ModelAndView joinForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("/join/joinForm");
	}
	
	@RequestMapping(value="/join",method=RequestMethod.POST)
	public ModelAndView insertUserInfo(@RequestParam(value = "uid", required = true, defaultValue = "")  String uid
			,@RequestParam(value = "uemail", required = false )  String email
			,@RequestParam(value = "uname")  String uname
			,@RequestParam(value = "udept")  String udept
			,@RequestParam(value = "upw")  String upw
			,@RequestParam(value = "upw2")  String upw2
			)throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("uid", uid);
		paramMap.put("uemail", "".equals(email) || null == email?uid:email);
		paramMap.put("uname", uname);
		paramMap.put("dept_nm", udept);
		paramMap.put("upw", upw);
		paramMap.put("org_nm", udept);
		
		boolean result = joinService.insertUserInfo(paramMap);
		
		ModelAndView mav = null;
		if(result){
			mav = new ModelAndView("forward:/join/joinLogin.do");
		}else{
			mav=new ModelAndView("/join/");
		}
		
		return mav;
	}
	
	@RequestMapping(value = "/joinLogin")
	public ModelAndView joinLogin(@RequestParam(value = "uid")  String uid
			,@RequestParam(value = "upw")  String password) throws Exception {
		
		
		ModelMap model = new ModelMap();
		model.addAttribute("userjoinid",uid);
		model.addAttribute("userjoinpassword",password);
		
		return new ModelAndView("/join/joinLogin", model);
	}
	
	@RequestMapping(value = "/idCheck")
	public @ResponseBody String idCheck(@RequestParam(value = "uid")  String uid) throws Exception {
		
		DataCommonVO dcv = new DataCommonVO();
		dcv.put("uid", uid);
		
		return joinService.selectIdCheck(dcv);
	}
}
