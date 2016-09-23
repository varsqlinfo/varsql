package com.varsql.web.app.manager.qna;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.PagingUtil;
import com.varsql.web.util.VarsqlUtil;

@Service
public class QnaServiceImpl implements QnaService{
	
	@Autowired
	QnaDAO qnaDAO;

	public String selectQnaMgmtList(DataCommonVO paramMap) {
		int totalcnt = qnaDAO.selectQnaMgmtTotalCnt(paramMap);
		
		paramMap = VarsqlUtil.setPagingParam(paramMap);
		
		Map json = new HashMap();
		if(totalcnt > 0){
			json.put("paging", PagingUtil.getPageObject(totalcnt, paramMap));
			json.put("result", qnaDAO.selectQnaMgmtList(paramMap));
		}
		
		return VarsqlUtil.objectToString(json);
	}

	public String updateQnaAnswerContent(DataCommonVO paramMap) {
		Map json = new HashMap();
		
		json.put("result", qnaDAO.updateQnaAnswerContent(paramMap) > 0);
		
		return VarsqlUtil.objectToString(json);
		
	}

}