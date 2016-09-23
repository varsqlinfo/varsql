package com.varsql.web.app.manager.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.app.admin.AdminService;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.HttpUtil;
import com.varsql.web.util.SecurityUtil;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/manager")
public class QnaController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(QnaController.class);
	
	@Autowired
	QnaService qnaService;
	
	@RequestMapping({"/qnaMgmtList"})
	public @ResponseBody String qnaMgmtList(@RequestParam(value = VarsqlParamConstants.SEARCHVAL, required = false, defaultValue = "" )  String searchval
			,@RequestParam(value = VarsqlParamConstants.SEARCH_NO, required = false, defaultValue = "1" )  int page
			,@RequestParam(value = VarsqlParamConstants.SEARCH_ROW, required = false, defaultValue = "10" )  int rows
			,@RequestParam(value = "answerYn", required = false, defaultValue = "Y" )  String answerYn
		) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put(VarsqlParamConstants.SEARCH_NO, page);
		paramMap.put(VarsqlParamConstants.SEARCH_ROW, rows);
		paramMap.put(VarsqlParamConstants.SEARCHVAL, searchval);
		paramMap.put("answerYn", answerYn);
		
		return qnaService.selectQnaMgmtList(paramMap);
	}
	
	
	@RequestMapping(value="/updQna")
	public @ResponseBody String qnaUpdate(HttpServletRequest req 
			,@RequestParam(value = "qnaid")  String qnaid
			,@RequestParam(value = "answer", required = true )  String answer) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("qnaid", qnaid);
		paramMap.put("answer", answer);
		paramMap.put("cre_id", SecurityUtil.loginId(req));
		
		return qnaService.updateQnaAnswerContent(paramMap);
		
	}

}
