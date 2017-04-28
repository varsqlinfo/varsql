package com.varsql.web.app.manager.dbnuser;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.common.util.SecurityUtil;
import com.varsql.common.util.StringUtil;
import com.varsql.web.common.constants.UserConstants;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.VarsqlUtil;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.PagingUtil;

@Service
public class DbnUserServiceImpl{
	
	@Autowired
	DbnUserDAO dbnUserDAO;

	public Map selectdbList(SearchParameter searchParameter) {
		
		int totalcnt = dbnUserDAO.selectdbListTotalCnt(searchParameter);
		
		Map json = new HashMap();
		if(totalcnt > 0){
			json.put("paging", PagingUtil.getPageObject(totalcnt, searchParameter));
			json.put("result", dbnUserDAO.selectdbList(searchParameter));
		}
		
		return json;
	}

	public Map selectDbUserMappingList(DataCommonVO paramMap) {
		
		paramMap=VarsqlUtil.setPagingParam(paramMap);
		
		Map json = new HashMap();
		json.put("result", dbnUserDAO.selectDbUserMappingList(paramMap));
	
		return json;
	}

	public Map updateDbUser(DataCommonVO paramMap) {
		Map json = new HashMap();
		String[] viewidArr = StringUtil.split(paramMap.getString("selectItem"),",");
		try{
			json.put("result", dbnUserDAO.updateDbUser( viewidArr, paramMap));
		}catch(Exception e){
			json.put("result", "error");
			json.put("resultMsg", e.getMessage());
		}
		return json;
	}

}