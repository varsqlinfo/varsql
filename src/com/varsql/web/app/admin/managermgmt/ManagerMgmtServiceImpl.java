package com.varsql.web.app.admin.managermgmt;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.common.util.SecurityUtil;
import com.varsql.common.util.StringUtil;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.util.VarsqlUtil;
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
public class ManagerMgmtServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(ManagerMgmtServiceImpl.class);
	
	@Autowired
	ManagerMgmtDAO managerMgmtDAO ;
	
	public Map selectRoleUserList(DataCommonVO paramMap) {
		
		int totalcnt = managerMgmtDAO.selectRoleUserTotalcnt(paramMap);
		
		Map json = new HashMap();
		if(totalcnt > 0){
			int page = paramMap.getInt("page",0);
			int rows = paramMap.getInt("rows",10);
			
			int first = (page-1)*rows ;
			
			paramMap.put("first", first);
			paramMap.put("rows", rows);
			
			json.put("paging", PagingUtil.getPageObject(totalcnt, page,rows));
			json.put("result", managerMgmtDAO.selectRoleUserList(paramMap));
		}
		
		return json;
		
	}
	
	public Map selectRoleManagerList(DataCommonVO paramMap) {
		
		int totalcnt = managerMgmtDAO.selectRoleManagerTotalcnt(paramMap);
		
		Map json = new HashMap();
		if(totalcnt > 0){
			int page = paramMap.getInt("page",0);
			int rows = paramMap.getInt("rows",10);
			
			int first = (page-1)*rows ;
			
			paramMap.put("first", first);
			paramMap.put("rows", rows);
			
			json.put("paging", PagingUtil.getPageObject(totalcnt, page,rows));
			json.put("result", managerMgmtDAO.selectRoleManagerList(paramMap));
		}
		
		return json;
	}
	
	public Map updateManagerRole(DataCommonVO paramMap) {
		
		SecurityUtil.setUserInfo(paramMap);
		
		Map json = new HashMap();
		json.put("result", managerMgmtDAO.updateManagerRole( paramMap));
		
		return json;
	}
	
	public Map selectDatabaseManager(DataCommonVO paramMap) {
		
		Map json = new HashMap();
			
		SecurityUtil.setUserInfo(paramMap);
		
		json.put("result", managerMgmtDAO.selectDatabaseManager(paramMap));
		
		return json;
	}

	public Map updateDbManager(DataCommonVO paramMap) {
		Map json = new HashMap();
		String[] viewidArr = StringUtil.split(paramMap.getString("selectItem"),",");
		try{
			json.put("result", managerMgmtDAO.updateDbManager( viewidArr, paramMap));
		}catch(Exception e){
			json.put("result", "error");
			json.put("resultMsg", e.getMessage());
		}
		return json;
	}
}