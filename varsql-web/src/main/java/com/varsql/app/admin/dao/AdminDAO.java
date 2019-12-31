package com.varsql.app.admin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.varsql.app.admin.beans.Vtconnection;
import com.varsql.app.admin.beans.VtconnectionOption;
import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.dao.BaseDAO;
import com.vartech.common.app.beans.SearchParameter;


@Repository
public class AdminDAO extends BaseDAO{
	
	/**
	 * 
	 * @Method Name  : selectDBTotalCnt
	 * @Method 설명 : db 목록 보기.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 22. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public int selectDBTotalCnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("adminMapper.selectDBTotalCnt", searchParameter);
	}
	
	public List<Object> selectDbList(SearchParameter searchParameter) {
		return getSqlSession().selectList("adminMapper.selectDbList", searchParameter);
	}

	public Object selectDetailObject(DataCommonVO paramMap) {
		return getSqlSession().selectOne("adminMapper.selectDetailObject", paramMap);
	}

	public String selectVtconnectionMaxVal() {
		return getSqlSession().selectOne("adminMapper.selectVtconnectionMaxVal");
	}
	
	public int insertVtconnectionInfo(Vtconnection vtConnection){
		return getSqlSession().insert("adminMapper.insertVtconnectionInfo", vtConnection );
	}
	
	public int updateVtconnectionInfo(Vtconnection vtConnection){
		return getSqlSession().update("adminMapper.updateVtconnectionInfo", vtConnection);
	}

	public int deleteVtconnectionInfo(DataCommonVO paramMap) {
		return getSqlSession().delete("adminMapper.deleteVtconnectionInfo", paramMap);
	}

	public List selectAllDbType() {
		return getSqlSession().selectList("adminMapper.selectAllDbType");
	}

	public List selectDbDriverList(DataCommonVO paramMap) {
		return getSqlSession().selectList("adminMapper.selectDbDriverList", paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : updateVtconnectionOptionInfo
	 * @Method 설명 : 옵션 정보 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 2. 19. 
	 * @변경이력  :
	 * @param vtconnectionOption
	 * @return
	 */
	public int updateVtconnectionOptionInfo(VtconnectionOption vtconnectionOption) {
		return getSqlSession().update("adminMapper.updateVtconnectionOptionInfo", vtconnectionOption);
	}
	
	/**
	 * 
	 * @Method Name  : selectDbInfo
	 * @Method 설명 : db 정보 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 3. 14. 
	 * @변경이력  :
	 * @param vtConnection
	 * @return
	 */
	public DataCommonVO selectDbInfo(Vtconnection vtConnection) {
		return getSqlSession().selectOne("adminMapper.selectDbInfo",vtConnection);
	}

	public DataCommonVO selectDbDriverInfo(Vtconnection vtConnection) {
		return getSqlSession().selectOne("adminMapper.selectDbDriverInfo",vtConnection);
	}
}
