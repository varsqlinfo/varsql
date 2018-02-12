package com.varsql.app.manager.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.UserConstants;
import com.varsql.app.common.constants.VarsqlParamConstants;
import com.varsql.app.manager.service.QnaServiceImpl;
import com.varsql.core.common.util.SecurityUtil;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/manager")
public class QnaController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(QnaController.class);
	
	@Autowired
	QnaServiceImpl qnaServiceImpl;
	
	@RequestMapping({"/qnaMgmtList"})
	public @ResponseBody Map qnaMgmtList(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		
		searchParameter.addCustomParam(UserConstants.ROLE, SecurityUtil.loginRole(req));
		searchParameter.addCustomParam(UserConstants.UID, SecurityUtil.loginId(req));
		
		return qnaServiceImpl.selectQnaMgmtList(searchParameter);
	}
	
	
	@RequestMapping(value="/updQna")
	public @ResponseBody Map qnaUpdate(HttpServletRequest req 
			,@RequestParam(value = "qnaid")  String qnaid
			,@RequestParam(value = "answer", required = true )  String answer) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("qnaid", qnaid);
		paramMap.put("answer", answer);
		paramMap.put("cre_id", SecurityUtil.loginId(req));
		
		return qnaServiceImpl.updateQnaAnswerContent(paramMap);
		
	}

}
