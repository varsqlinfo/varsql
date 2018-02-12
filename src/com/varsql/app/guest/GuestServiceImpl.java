package com.varsql.app.guest;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.util.VarsqlUtil;
import com.vartech.common.utils.PagingUtil;

/**
 * 
 * @FileName  : AdminServiceImpl.java
 * @Date      : 2014. 8. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class GuestServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(GuestServiceImpl.class);
	
	@Autowired
	GuestDAO guestDAO ;

	public Map selectQna(DataCommonVO paramMap) {
		
		int totalcnt = guestDAO.selectQnaTotalCnt(paramMap);
		
		Map json = new HashMap();
		if(totalcnt > 0){
			int page = paramMap.getInt("page",0);
			int rows = paramMap.getInt("rows",10);
			
			int first = (page-1)*rows ;
			
			paramMap.put("first", first);
			paramMap.put("rows", rows);
			
			json.put("paging", PagingUtil.getPageObject(totalcnt, page,rows));
			json.put("result", guestDAO.selectQna(paramMap));
		}
		
		return json;
	}
	/**
	 * 
	 * @Method Name  : insertQnaInfo
	 * @Method 설명 : qna 등록.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public boolean insertQnaInfo(DataCommonVO paramMap) {
		String qnaid = guestDAO.selectQnaMaxVal();
		qnaid=VarsqlUtil.generateUUID();
		paramMap.put("qnaid", qnaid);
		
		return guestDAO.insertQnaInfo(paramMap) > 0;
	}
	
	public boolean deleteQnaInfo(DataCommonVO paramMap) {
		return guestDAO.deleteQnaInfo(paramMap) > 0;
	}

	public boolean updateQnaInfo(DataCommonVO paramMap) {
		return guestDAO.updateQnaInfo(paramMap) > 0;
	}

	public Map selectDetailQna(DataCommonVO paramMap) {
		Map json = new HashMap();
			
		json.put("result", guestDAO.selectDetailQna(paramMap));
		
		return json;
	}
}