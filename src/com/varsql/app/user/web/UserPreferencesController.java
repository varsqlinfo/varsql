package com.varsql.app.user.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.VarsqlParamConstants;
import com.varsql.app.user.beans.MemoInfo;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.app.user.beans.QnAInfo;
import com.varsql.app.user.beans.UserForm;
import com.varsql.app.user.service.UserMainServiceImpl;
import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.beans.DatabaseInfo;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.utils.HttpUtils;



/**
 * 
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: PreferencesController.java
* @DESC		: 사용자 환경설정 컨트롤러. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 11. 28. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/user/preferences")
public class UserPreferencesController {

	private static final Logger logger = LoggerFactory.getLogger(UserPreferencesController.class);
	
	@Autowired
	UserMainServiceImpl userMainServiceImpl;
	
	/**
	 * 
	 * @Method Name  : preferencesMain
	 * @Method 설명 : 사용자 정보 환결 설정.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"","/","/main"})
	public ModelAndView preferencesMain(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		
		model.addAttribute("headerview", ("N".equals(req.getParameter("header"))?"N":""));
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		model.addAttribute("detailInfo" , userMainServiceImpl.selectUserDetail(SecurityUtil.loginId(req)));
		model.addAttribute("localeInfo" , LocaleConstants.values());
		return  new ModelAndView("/user/preferences/general", model);
	}
	
	/**
	 * 
	 * @Method Name  : userInfoSave
	 * @Method 설명 : 사용자 정보 업데이트.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param userForm
	 * @param result
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/userInfoSave"})
	public @ResponseBody ResponseResult userInfoSave(@Valid UserForm userForm, BindingResult result,HttpServletRequest req, HttpServletResponse res) throws Exception {
		ResponseResult resultObject = new ResponseResult();
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  UserMainController userInfoSave check {}",errorVal.toString());
			}
			resultObject.setResultCode(ResultConst.CODE.DATA_NOT_VALID.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}else{
			userForm.setViewid(SecurityUtil.loginId(req));
			resultObject.setItemOne(userMainServiceImpl.updateUserInfo(userForm,req,res));
		}
		
		return  resultObject;
	}
	
	/**
	 * 
	 * @Method Name  : preferencesPassword
	 * @Method 설명 : 패스워드 변경.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/password"})
	public ModelAndView preferencesPassword(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("headerview", ("N".equals(req.getParameter("header"))?"N":""));
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		
		return  new ModelAndView("/user/preferences/password", model);
	}
	
	/**
	 * 
	 * @Method Name  : passwordSave
	 * @Method 설명 : 비밀번호 변경.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param passwordForm
	 * @param result
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/passwordSave"})
	public @ResponseBody ResponseResult passwordSave(@Valid PasswordForm passwordForm, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  UserMainController passwordSave check {}",errorVal.toString());
			}
			resultObject.setResultCode(ResultConst.CODE.DATA_NOT_VALID.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}else{
			passwordForm.setViewid(SecurityUtil.loginId(req));
			resultObject = userMainServiceImpl.updatePasswordInfo(passwordForm, resultObject); 
		}
		
		return  resultObject;
	}
	
	/**
	 * @Method Name  : preferencesMessage
	 * @Method 설명 : 메시지 관리
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/message"})
	public ModelAndView preferencesMessage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("headerview", ("N".equals(req.getParameter("header"))?"N":""));
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		
		return  new ModelAndView("/user/preferences/memoMgmt", model);
	}
	
	/**
	 * 
	 * @Method Name  : qna
	 * @Method 설명 : qna 관리
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 4. 
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/qna"})
	public ModelAndView qna(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("headerview", ("N".equals(req.getParameter("header"))?"N":""));
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		
		return  new ModelAndView("/user/preferences/qna", model);
	}
	
	/**
	 * @Method Name  : preferenceslistMsg
	 * @Method 설명 : 메시지 목록보기
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/listMsg"})
	public @ResponseBody ResponseResult preferenceslistMsg(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		searchParameter.addCustomParam(VarsqlParamConstants.UID, SecurityUtil.loginId(req));
		
		return  userMainServiceImpl.selectUserMsg(searchParameter);
	}
	
	@RequestMapping({"/msgReplyList"})
	public @ResponseBody ResponseResult msgReplyList(HttpServletRequest req) throws Exception {
		ParamMap paramMap = HttpUtils.getServletRequestParam(req);
		return  userMainServiceImpl.selectUserMsgReply(paramMap);
	}
	
	/**
	 * @Method Name  : preferencesdeleteMsg
	 * @Method 설명 : 메시지 목록보기
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/deleteMsg"})
	public @ResponseBody ResponseResult preferencesdeleteMsg(HttpServletRequest req) throws Exception {
		ParamMap paramMap = HttpUtils.getServletRequestParam(req);
		paramMap.put(VarsqlParamConstants.UID, SecurityUtil.loginId(req));
		
		return  userMainServiceImpl.deleteUserMsg( paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : sqlfile
	 * @Method 설명 : sql file list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 30. 
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/sqlFile"})
	public ModelAndView sqlfile(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("headerview", ("N".equals(req.getParameter("header"))?"N":""));
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		
		return  new ModelAndView("/user/preferences/sqlFile", model);
	}
}
