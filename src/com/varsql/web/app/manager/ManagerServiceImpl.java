package com.varsql.web.app.manager;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.auth.Authority;
import com.varsql.common.util.StringUtil;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.PagingUtil;
import com.varsql.web.util.VarsqlUtil;

@Service
public class ManagerServiceImpl{
	
	@Autowired
	ManagerDAO manageDAO;

	public Map selectUserList(DataCommonVO paramMap) {
		int totalcnt = manageDAO.selectUserTotalcnt(paramMap);
		
		int page = paramMap.getInt("page",0);
		int rows = paramMap.getInt("rows",10);
		
		int first = (page-1)*rows ;
		
		paramMap.put("first", first);
		paramMap.put("rows", rows);
		
		Map json = new HashMap();
		if(totalcnt > 0){
			json.put("paging", PagingUtil.getPageObject(totalcnt, page, rows));
			json.put("result", manageDAO.selectUserList(paramMap));
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