package com.varsql.web.app.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.app.manager.service.QnaServiceImpl;
import com.varsql.web.app.user.beans.QnAInfo;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VarsqlParamConstants;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.utils.HttpUtils;



/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: QnaController.java
* @DESC		: Qna 관리
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 8. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/manager")
public class QnaController extends AbstractController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(QnaController.class);

	@Autowired
	QnaServiceImpl qnaServiceImpl;

	/**
	 *
	 * @Method Name  : qnaMgmtList
	 * @Method 설명 : qna 매니저 목록.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 10.
	 * @변경이력  :
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/qnaMgmtList"})
	public @ResponseBody ResponseResult qnaMgmtList(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);

		searchParameter.addCustomParam(VarsqlParamConstants.ROLE, SecurityUtil.loginRole(req));
		searchParameter.addCustomParam(VarsqlParamConstants.UID, SecurityUtil.loginId(req));

		return qnaServiceImpl.selectQnaMgmtList(searchParameter);
	}


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
			resultObject = qnaServiceImpl.updateQnaAnswerContent(qnaInfo);
		}
		return resultObject;
	}
}
