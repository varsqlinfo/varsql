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
import com.varsql.app.common.constants.UserConstants;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.app.user.beans.QnAInfo;
import com.varsql.app.user.beans.UserForm;
import com.varsql.app.user.service.UserMainServiceImpl;
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
		SecurityUtil.reloadUserDatabaseInfo();
		model.addAttribute("dblist", SecurityUtil.loginInfo(req).getDatabaseInfo().values());
		
		return  new ModelAndView("/user/userMain",model);
	}
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
	@RequestMapping({"/preferences"})
	public ModelAndView preferencesMain(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		
		model.addAttribute("headerview", ("N".equals(req.getParameter("header"))?"N":""));
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		model.addAttribute("detailInfo" , userMainServiceImpl.selectUserDetail(SecurityUtil.loginId(req)));
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
	@RequestMapping({"/preferences/userInfoSave"})
	public @ResponseBody ResponseResult userInfoSave(@Valid UserForm userForm, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  UserMainController validation check {}",errorVal.toString());
			}
			resultObject.setResultCode(500);
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}else{
			userForm.setViewid(SecurityUtil.loginId(req));
			resultObject.setItemOne(userMainServiceImpl.updateUserInfo(userForm));
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
	@RequestMapping({"/preferences/password"})
	public ModelAndView preferencesPassword(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("headerview", ("N".equals(req.getParameter("header"))?"N":""));
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		
		return  new ModelAndView("/user/preferences/password", model);
	}
	
	/**
	 * 
	 * @Method Name  : userInfoSave
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
	@RequestMapping({"/preferences/passwordSave"})
	public @ResponseBody ResponseResult userInfoSave(@Valid PasswordForm passwordForm, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  UserMainController userInfoSave check {}",errorVal.toString());
			}
			resultObject.setResultCode(ResultConst.CODE.ERROR.toInt());
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
	@RequestMapping({"/preferences/message"})
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
	@RequestMapping({"/preferences/qna"})
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
	@RequestMapping({"/preferences/listMsg"})
	public @ResponseBody ResponseResult preferenceslistMsg(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		searchParameter.addCustomParam(UserConstants.UID, SecurityUtil.loginId(req));
		
		return  userMainServiceImpl.selectUserMsg(searchParameter);
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
	@RequestMapping({"/preferences/deleteMsg"})
	public @ResponseBody ResponseResult preferencesdeleteMsg(HttpServletRequest req) throws Exception {
		ParamMap paramMap = HttpUtils.getServletRequestParam(req);
		paramMap.put(UserConstants.UID, SecurityUtil.loginId(req));
		
		return  userMainServiceImpl.deleteUserMsg( paramMap);
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
	public @ResponseBody ResponseResult sendSql(HttpServletRequest req
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
	
	/**
	 * 
	 * @Method Name  : qnalist
	 * @Method 설명 : qna list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 10. 
	 * @변경이력  :
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/qnaList")
	public @ResponseBody ResponseResult qnalist(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		searchParameter.addCustomParam(UserConstants.UID, SecurityUtil.loginId(req));
	
		return userMainServiceImpl.selectQna(searchParameter);
	}
	
	/**
	 * 
	 * @Method Name  : qna
	 * @Method 설명 : qna 정보 등록. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 10. 
	 * @변경이력  :
	 * @param qnaInfo
	 * @param result
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/insQna")
	public @ResponseBody ResponseResult insQna(@Valid QnAInfo qnaInfo, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();
		
		if(result.hasErrors()){
			for(ObjectError errorVal : result.getAllErrors()){
				logger.warn("###  GuestController qna check {}",errorVal.toString());
			}
			resultObject.setResultCode(ResultConst.CODE.ERROR.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}else{
			qnaInfo.setUserid(SecurityUtil.loginId());
			resultObject = userMainServiceImpl.saveQnaInfo(qnaInfo, true);
		}
		
		return resultObject; 
	}
	
	/**
	 * 
	 * @Method Name  : qnaDelete
	 * @Method 설명 : qna  삭제
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 10. 
	 * @변경이력  :
	 * @param qnaid
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delQna")
	public @ResponseBody ResponseResult qnaDelete(@RequestParam(value = "qnaid" , required=true)  String qnaid,HttpServletRequest req) throws Exception {
		
		QnAInfo qnaInfo = new QnAInfo();
		qnaInfo.setQnaid(qnaid);
		
		qnaInfo.setUserid(SecurityUtil.loginId());
		
		return userMainServiceImpl.deleteQnaInfo(qnaInfo);
	}
	
	/**
	 * 
	 * @Method Name  : qnaUpdate
	 * @Method 설명 : qna 수정. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 10. 
	 * @변경이력  :
	 * @param qnaInfo
	 * @param result
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updQna")
	public @ResponseBody ResponseResult qnaUpdate(@Valid QnAInfo qnaInfo, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();
		
		if(result.hasErrors()){
			for(ObjectError errorVal : result.getAllErrors()){
				logger.warn("###  GuestController qna check {}",errorVal.toString());
			}
			resultObject.setResultCode(ResultConst.CODE.ERROR.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}else{
			qnaInfo.setUserid(SecurityUtil.loginId());
			resultObject = userMainServiceImpl.saveQnaInfo(qnaInfo, false);
		}
		
		return resultObject; 
	}
	
	@RequestMapping(value="/connectionInfo")
	public @ResponseBody ResponseResult connectionInfo(HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();
		
		SecurityUtil.reloadUserDatabaseInfo();
		
		
		Collection<DatabaseInfo> dataBaseInfo = SecurityUtil.loginInfo(req).getDatabaseInfo().values();
		
		List databaseList =new ArrayList();
		dataBaseInfo.forEach(item -> {
			Map addMap = new HashMap();
			
			addMap.put("uuid", item.getConnUUID());
			addMap.put("type", item.getType());
			addMap.put("name", item.getName());
			databaseList.add(addMap);
		});
		
		resultObject.setItemList(databaseList);
		
		return resultObject; 
	}
}
