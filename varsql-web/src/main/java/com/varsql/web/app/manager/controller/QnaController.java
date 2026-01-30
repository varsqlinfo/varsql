package com.varsql.web.app.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.app.manager.service.QnaServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.HttpParamConstants;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.user.QnARequesetDTO;
import com.varsql.web.util.SecurityUtil;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;

import lombok.RequiredArgsConstructor;



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
@RequiredArgsConstructor
public class QnaController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(QnaController.class);

	private final QnaServiceImpl qnaServiceImpl;
	
	/**
	 *
	 * @Method Name  : qnaMgmtList
	 * @Method 설명 : Q & A
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 9.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/qnaMgmt")
	public ModelAndView qnaMgmtList(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "qnaMgmt");
		return getModelAndView("/qnaMgmt", VIEW_PAGE.MANAGER,model);
	}

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

		searchParameter.addCustomParam(HttpParamConstants.ROLE, SecurityUtil.loginRole(req));
		searchParameter.addCustomParam(HttpParamConstants.UID, SecurityUtil.userViewId(req));

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

		if(result.hasErrors()){
			for(ObjectError errorVal : result.getAllErrors()){
				logger.info("###  QnaController qna qnaUpdate {}",errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}
		
		return qnaServiceImpl.updateQnaAnswerContent(qnaInfo);
	}
}
