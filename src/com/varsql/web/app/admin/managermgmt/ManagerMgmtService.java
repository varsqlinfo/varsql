package com.varsql.web.app.admin.managermgmt;
import com.varsql.web.common.vo.DataCommonVO;

public interface ManagerMgmtService  {
	
	/**
	 * 
	 * @param paramMap
	 * @return
	 */
	String selectRoleManagerList(DataCommonVO paramMap);
	
	String selectRoleUserList(DataCommonVO paramMap);
	
	String updateManagerRole(DataCommonVO paramMap);
	
	String selectDatabaseManager(DataCommonVO paramMap);

	String updateDbManager(DataCommonVO paramMap);
	
}