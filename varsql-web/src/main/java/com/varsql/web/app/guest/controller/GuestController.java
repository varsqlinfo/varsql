package com.varsql.web.app.guest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.app.user.beans.QnAInfo;
import com.varsql.web.app.user.service.UserPreferencesServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.constants.VarsqlParamConstants;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.utils.HttpUtils;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: GuestController.java
* @DESC		: guest ?Ç¨?ö©?ûê
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 1. 4. 			ytkim			ÏµúÏ¥à?ûë?Ñ±

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/guest")
public class GuestController extends AbstractController  {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(GuestController.class);

	@Autowired
	private UserPreferencesServiceImpl userPreferencesServiceImpl;

	@RequestMapping({"","/","/main"})
	public ModelAndView mainpage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		return getModelAndView("guestMain" , VIEW_PAGE.GUEST);
	}

	/**
	 *
	 * @Method Name  : qnalist
	 * @Method ?Ñ§Î™? : qna Î™©Î°ù
	 * @?ûë?Ñ±?ûê   : ytkim
	 * @?ûë?Ñ±?ùº   : 2019. 11. 1.
	 * @Î≥?Í≤ΩÏù¥?†•  :
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/qnaList")
	public @ResponseBody ResponseResult qnalist(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		searchParameter.addCustomParam(VarsqlParamConstants.UID, SecurityUtil.loginId(req));

		return userPreferencesServiceImpl.selectQna(searchParameter);
	}

	/**
	 *
	 * @Method Name  : qna
	 * @Method ?Ñ§Î™? : qna ?ì±Î°?.
	 * @?ûë?Ñ±?ûê   : ytkim
	 * @?ûë?Ñ±?ùº   : 2019. 11. 1.
	 * @Î≥?Í≤ΩÏù¥?†•  :
	 * @param qnaInfo
	 * @param result
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/insQna")
	public @ResponseBody ResponseResult qna(@Valid QnAInfo qnaInfo, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();


		if(result.hasErrors()){
			for(ObjectError errorVal : result.getAllErrors()){
				logger.warn("###  GuestController qna check {}",errorVal.toString());
			}
			resultObject.setResultCode(ResultConst.CODE.DATA_NOT_VALID.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}else{
			qnaInfo.setUserid(SecurityUtil.loginId());
			resultObject = userPreferencesServiceImpl.saveQnaInfo(qnaInfo, true);
		}

		return resultObject;
	}

	/**
	 *
	 * @Method Name  : qnaDelete
	 * @Method ?Ñ§Î™? : qna ?Ç≠?†ú.
	 * @?ûë?Ñ±?ûê   : ytkim
	 * @?ûë?Ñ±?ùº   : 2019. 11. 1.
	 * @Î≥?Í≤ΩÏù¥?†•  :
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

		return userPreferencesServiceImpl.deleteQnaInfo(qnaInfo);
	}

	/**
	 *
	 * @Method Name  : qnaUpdate
	 * @Method ?Ñ§Î™? : qna ?óÖ?ç∞?ù¥?ä∏.
	 * @?ûë?Ñ±?ûê   : ytkim
	 * @?ûë?Ñ±?ùº   : 2019. 11. 1.
	 * @Î≥?Í≤ΩÏù¥?†•  :
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
			resultObject.setResultCode(ResultConst.CODE.DATA_NOT_VALID.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}else{
			qnaInfo.setUserid(SecurityUtil.loginId());
			resultObject = userPreferencesServiceImpl.saveQnaInfo(qnaInfo, false);
		}

		return resultObject;
	}
}
