package com.varsql.web.app.manager.dbnuser;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.common.util.StringUtil;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.PagingUtil;
import com.varsql.web.util.VarsqlUtil;

@Service
public class DbnUserServiceImpl{
	
	@Autowired
	DbnUserDAO dbnUserDAO;

	public Map selectdbList(DataCommonVO paramMap) {
		
		int totalcnt = dbnUserDAO.selectdbListTotalCnt(paramMap);
		
		Map json = new HashMap();
		if(totalcnt > 0){
			paramMap=VarsqlUtil.setPagingParam(paramMap);
			json.put("paging", PagingUtil.getPageObject(totalcnt, paramMap));
			json.put("result", dbnUserDAO.selectdbList(paramMap));
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