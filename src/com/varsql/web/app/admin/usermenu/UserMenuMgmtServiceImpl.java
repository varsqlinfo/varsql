package com.varsql.web.app.admin.usermenu;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.common.util.StringUtil;
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
public class UserMenuMgmtServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(UserMenuMgmtServiceImpl.class);
	
	@Autowired
	UserMenuMgmtDAO userMenuMgmtDAO ;
	
	public Map listDbMenu(DataCommonVO paramMap) {
		int totalcnt = userMenuMgmtDAO.listDbMenuTotalcnt(paramMap);
		
		Map json = new HashMap();
		if(totalcnt > 0){
			int page = paramMap.getInt("page",0);
			int rows = paramMap.getInt("rows",10);
			
			int first = (page-1)*rows ;
			
			paramMap.put("first", first);
			paramMap.put("rows", rows);
			
			json.put("paging", PagingUtil.getPageObject(totalcnt, page,rows));
			json.put("result", userMenuMgmtDAO.listDbMenu(paramMap));
		}
		
		return json;
	}

	public boolean moodifyDbMenu(DataCommonVO paramMap) {
		return userMenuMgmtDAO.moodifyDbMenu( paramMap) > 0;
	}

	public boolean addDbMenu(DataCommonVO paramMap) {
		return userMenuMgmtDAO.addDbMenu( paramMap) > 0;
	}

	public boolean deleteDbMenu(DataCommonVO paramMap) {
		return userMenuMgmtDAO.deleteDbMenu( paramMap) > 0;
	}
}