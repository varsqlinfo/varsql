package com.varsql.web.app.guest;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.PagingUtil;
import com.varsql.web.util.VarsqlUtil;

/**
 * 
 * @FileName  : AdminServiceImpl.java
 * @Date      : 2014. 8. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class GuestServiceImpl implements GuestService{
	private static final Logger logger = LoggerFactory.getLogger(GuestServiceImpl.class);
	
	@Autowired
	GuestDAO guestDAO ;

	public String selectQna(DataCommonVO paramMap) {
		
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
		
		return VarsqlUtil.objectToString(json);
	}

	public boolean insertQnaInfo(DataCommonVO paramMap) {
		String qnaid = guestDAO.selectQnaMaxVal();
		
		try{
			qnaid=String.format("%06d", Integer.parseInt(qnaid)+1);
		}catch(Exception e){
			qnaid=String.format("%06d", 1);
		}
		paramMap.put("qnaid", qnaid);
		
		return guestDAO.insertQnaInfo(paramMap) > 0;
	}

	public boolean deleteQnaInfo(DataCommonVO paramMap) {
		return guestDAO.deleteQnaInfo(paramMap) > 0;
	}

	public boolean updateQnaInfo(DataCommonVO paramMap) {
		return guestDAO.updateQnaInfo(paramMap) > 0;
	}

	public String selectDetailQna(DataCommonVO paramMap) {
		Map json = new HashMap();
			
		json.put("result", guestDAO.selectDetailQna(paramMap));
		
		return VarsqlUtil.objectToString(json);
	}
}