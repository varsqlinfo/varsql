package com.varsql.app.manager.service;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.manager.dao.QnaDAO;
import com.varsql.app.util.VarsqlUtil;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.PagingUtil;

@Service
public class QnaServiceImpl{
	
	@Autowired
	QnaDAO qnaDAO;

	public Map selectQnaMgmtList(SearchParameter searchParameter) {
		
		//System.out.println("searchParameter : "+ searchParameter);
		int totalcnt = qnaDAO.selectQnaMgmtTotalCnt(searchParameter);
		
		Map json = new HashMap();
		if(totalcnt > 0){
			json.put("paging", PagingUtil.getPageObject(totalcnt, searchParameter));
			json.put("result", qnaDAO.selectQnaMgmtList(searchParameter));
		}
		
		return json;
	}

	public Map updateQnaAnswerContent(DataCommonVO paramMap) {
		Map json = new HashMap();
		
		json.put("result", qnaDAO.updateQnaAnswerContent(paramMap) > 0);
		
		return json;
		
	}

}