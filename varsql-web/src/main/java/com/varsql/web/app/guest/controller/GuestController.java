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
import com.varsql.web.app.user.service.UserPreferencesServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.constants.VarsqlParamConstants;
import com.varsql.web.dto.user.QnARequesetDTO;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.utils.HttpUtils;

/**
 * -----------------------------------------------------------------------------
* @fileName		: GuestController.java
* @desc		:  guest controller
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 27. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/guest")
public class GuestController extends AbstractController  {

	private static final Logger logger = LoggerFactory.getLogger(GuestController.class);

	@Autowired
	private UserPreferencesServiceImpl userPreferencesServiceImpl;

	@RequestMapping(value = {"","/","/main"})
	public ModelAndView mainpage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		return getModelAndView("/guestMain" , VIEW_PAGE.GUEST);
	}

	/**
	 * @method  : qnalist
	 * @desc : qna list
	 * @author   : ytkim
	 * @date   : 2020. 4. 27. 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/qnaList")
	public @ResponseBody ResponseResult qnalist(HttpServletRequest req) throws Exception {
		return userPreferencesServiceImpl.searchQna(HttpUtils.getSearchParameter(req));
	}

	/**
	 * @method  : qna
	 * @desc : qna insert
	 * @author   : ytkim
	 * @date   : 2020. 4. 27. 
	 * @param qnaInfo
	 * @param result
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/insQna")
	public @ResponseBody ResponseResult qna(@Valid QnARequesetDTO qnaInfo, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();


		if(result.hasErrors()){
			for(ObjectError errorVal : result.getAllErrors()){
				logger.warn("###  GuestController qna check {}",errorVal.toString());
			}
			resultObject.setResultCode(ResultConst.CODE.DATA_NOT_VALID.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}else{
			resultObject = userPreferencesServiceImpl.saveQnaInfo(qnaInfo, true);
		}

		return resultObject;
	}

	/**
	 * @method  : qnaDelete
	 * @desc : qna delete
	 * @author   : ytkim
	 * @date   : 2020. 4. 27. 
	 * @param qnaid
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delQna")
	public @ResponseBody ResponseResult qnaDelete(@RequestParam(value = "qnaid" , required=true) String qnaid) throws Exception {
		return userPreferencesServiceImpl.deleteQnaInfo(qnaid);
	}

	/**
	 * @method  : qnaUpdate
	 * @desc : qna update
	 * @author   : ytkim
	 * @date   : 2020. 4. 27. 
	 * @param qnaInfo
	 * @param result
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updQna")
	public @ResponseBody ResponseResult qnaUpdate(@Valid QnARequesetDTO qnaInfo, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();

		if(result.hasErrors()){
			for(ObjectError errorVal : result.getAllErrors()){
				logger.warn("###  GuestController qna check {}",errorVal.toString());
			}
			resultObject.setResultCode(ResultConst.CODE.DATA_NOT_VALID.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}else{
			resultObject = userPreferencesServiceImpl.saveQnaInfo(qnaInfo, false);
		}

		return resultObject;
	}
}
