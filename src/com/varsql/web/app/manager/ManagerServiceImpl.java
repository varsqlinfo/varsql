package com.varsql.web.app.manager;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.auth.Authority;
import com.varsql.common.util.StringUtil;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.util.VarsqlUtil;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.PagingUtil;

@Service
public class ManagerServiceImpl{
	
	@Autowired
	ManagerDAO manageDAO;

	public Map selectUserList(SearchParameter searchParameter) {
		int totalcnt = manageDAO.selectUserTotalcnt(searchParameter);
		
		Map json = new HashMap();
		if(totalcnt > 0){
			json.put("paging", PagingUtil.getPageObject(totalcnt, searchParameter));
			json.put("result", manageDAO.selectUserList(searchParameter));
		}
		
		return json;
	}

	public Map updateAccept(DataCommonVO paramMap) {
		
		String[] viewidArr = StringUtil.split(paramMap.getString("selectItem"),",");
		String role = paramMap.getString("acceptyn").equals("Y")?Authority.USER.name():Authority.GUEST.name();
		
		paramMap.put("role", role);
		
		Map json = new HashMap();
		json.put("result", manageDAO.updateAccept(viewidArr, paramMap));
		
		return json;
	}
	
}