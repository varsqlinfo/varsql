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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.app.manager.service.QnaServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VarsqlParamConstants;
import com.varsql.web.dto.user.QnARequesetDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
 * -----------------------------------------------------------------------------
* @fileName		: QnaController.java
* @desc		: Qna 관리
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 30. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/manager")
public class QnaController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(QnaController.class);

	@Autowired
	private QnaServiceImpl qnaServiceImpl;

	/**
	 * @method  : qnaMgmtList
	 * @desc : qna 매니저 목록.
	 * @author   : ytkim
	 * @date   : 2020. 4. 30.
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/qnaMgmtList", method = RequestMethod.POST)
	public @ResponseBody ResponseResult qnaMgmtList(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);

		searchParameter.addCustomParam(VarsqlParamConstants.ROLE, SecurityUtil.loginRole(req));
		searchParameter.addCustomParam(VarsqlParamConstants.UID, SecurityUtil.userViewId(req));

		return qnaServiceImpl.selectQnaMgmtList(searchParameter);
	}

	/**
	 * @method  : qnaUpdate
	 * @desc : qna update
	 * @author   : ytkim
	 * @date   : 2020. 4. 30.
	 * @param qnaInfo
	 * @param result
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updQna", method = RequestMethod.POST)
	public @ResponseBody ResponseResult qnaUpdate(@Valid QnARequesetDTO qnaInfo, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();

		if(result.hasErrors()){
			for(ObjectError errorVal : result.getAllErrors()){
				logger.info("###  QnaController qna qnaUpdate {}",errorVal.toString());
			}
			resultObject = VarsqlUtils.getResponseResultValidItem(resultObject, result);
		}else{
			resultObject = qnaServiceImpl.updateQnaAnswerContent(qnaInfo);
		}
		return resultObject;
	}
}
