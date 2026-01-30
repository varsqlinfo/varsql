package com.varsql.web.app.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.app.user.service.UserPreferencesServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.common.service.UserCommonService;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.user.PasswordRequestDTO;
import com.varsql.web.dto.user.QnARequesetDTO;
import com.varsql.web.dto.user.UserModReqeustDTO;
import com.varsql.web.util.SecurityUtil;
import com.varsql.web.util.ValidateUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.HttpUtils;

import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class UserPreferencesController extends AbstractController{

	private final Logger logger = LoggerFactory.getLogger(UserPreferencesController.class);

	private final UserPreferencesServiceImpl userPreferencesServiceImpl;
	
	private final UserCommonService userCommonService;

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
	@RequestMapping(value={"","/","/main"}, method =RequestMethod.GET)
	public ModelAndView preferencesMain(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();

		setModelDefaultValue(req , model);
		model.addAttribute("detailInfo" , userPreferencesServiceImpl.findUserInfo(SecurityUtil.userViewId(req)));
		return getModelAndView("/general", VIEW_PAGE.USER_PREFERENCES, model);
	}

	private void setModelDefaultValue(HttpServletRequest req, ModelMap model) {
		model.addAttribute("headerview", ("N".equals(req.getParameter("header"))?"N":""));
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
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
	@RequestMapping(value={"/userInfoSave"}, method = RequestMethod.POST)
	public @ResponseBody ResponseResult userInfoSave(@Valid UserModReqeustDTO userForm, BindingResult result,HttpServletRequest req, HttpServletResponse res) throws Exception {
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  UserMainController userInfoSave check {}",errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}
		return VarsqlUtils.getResponseResultItemOne(userPreferencesServiceImpl.updateUserInfo(userForm,req,res));
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
	@RequestMapping(value={"/password"}, method =RequestMethod.GET)
	public ModelAndView preferencesPassword(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		setModelDefaultValue(req , model);

		return getModelAndView("/password", VIEW_PAGE.USER_PREFERENCES, model);
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
	@RequestMapping(value="/passwordSave", method = RequestMethod.POST)
	public @ResponseBody ResponseResult passwordSave(@Valid PasswordRequestDTO passwordForm, BindingResult result,HttpServletRequest req) throws Exception {
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  UserMainController passwordSave check {}",errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}
		
		passwordForm.setViewid(SecurityUtil.userViewId(req));
		return userPreferencesServiceImpl.updatePasswordInfo(passwordForm);
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
	@RequestMapping(value="/message", method = RequestMethod.GET)
	public ModelAndView preferencesMessage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		setModelDefaultValue(req , model);

		return getModelAndView("/noteMgmt", VIEW_PAGE.USER_PREFERENCES, model);
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
	@RequestMapping(value= "/qna", method = RequestMethod.GET)
	public ModelAndView qna(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		setModelDefaultValue(req , model);

		return getModelAndView("/qna", VIEW_PAGE.USER_PREFERENCES, model);
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
	@RequestMapping(value = "/qnaList", method = RequestMethod.POST)
	public @ResponseBody ResponseResult qnalist(HttpServletRequest req) throws Exception {
		return userPreferencesServiceImpl.searchQna(HttpUtils.getSearchParameter(req));
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
	@RequestMapping(value="/insQna", method = RequestMethod.POST)
	public @ResponseBody ResponseResult insQna(@Valid QnARequesetDTO qnaInfo, BindingResult result,HttpServletRequest req) throws Exception {

		if(result.hasErrors()){
			for(ObjectError errorVal : result.getAllErrors()){
				logger.warn("###  GuestController qna check {}",errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}

		if(ValidateUtils.isEmpty(qnaInfo.getQnaid())) {
			return userPreferencesServiceImpl.saveQnaInfo(qnaInfo, true);
		}
		
		return userPreferencesServiceImpl.saveQnaInfo(qnaInfo, false);
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
	@RequestMapping(value="/delQna", method = RequestMethod.POST)
	public @ResponseBody ResponseResult qnaDelete(@RequestParam(value = "qnaid" , required=true) String qnaid) throws Exception {
		return userPreferencesServiceImpl.deleteQnaInfo(qnaid);
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
	@RequestMapping(value="/updQna", method = RequestMethod.POST)
	public @ResponseBody ResponseResult qnaUpdate(@Valid QnARequesetDTO qnaInfo, BindingResult result,HttpServletRequest req) throws Exception {
		if(result.hasErrors()){
			for(ObjectError errorVal : result.getAllErrors()){
				logger.warn("###  GuestController qna check {}",errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}
		
		return userPreferencesServiceImpl.saveQnaInfo(qnaInfo, false);
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
	@RequestMapping(value={"/sqlFile"}, method =RequestMethod.GET)
	public ModelAndView sqlFile(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		setModelDefaultValue(req , model);
		
		model.addAttribute("dblist", userCommonService.databaseList());
		return getModelAndView("/sqlFile", VIEW_PAGE.USER_PREFERENCES, model);
	}
	
	@RequestMapping(value = { "/fileImportList" }, method = { RequestMethod.GET })
	public ModelAndView fileImportList(HttpServletRequest req, HttpServletResponse res, ModelAndView mav)
			throws Exception {
		ModelMap model = mav.getModelMap();
		setModelDefaultValue(req, model);
		
		model.addAttribute("selectMenu", "fileList");
		model.addAttribute("dblist", userCommonService.databaseList());
		return getModelAndView("/fileImportList", VIEW_PAGE.USER_PREFERENCES, model);
	}

	@RequestMapping(value = { "/fileExportList" }, method = { RequestMethod.GET })
	public ModelAndView fileExportList(HttpServletRequest req, HttpServletResponse res, ModelAndView mav)
			throws Exception {
		ModelMap model = mav.getModelMap();
		setModelDefaultValue(req, model);
		
		model.addAttribute("selectMenu", "fileList");
		model.addAttribute("dblist", userCommonService.databaseList());
		return getModelAndView("/fileExportList", VIEW_PAGE.USER_PREFERENCES, model);
	}

}
