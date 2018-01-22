package com.varsql.web.app.admin;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.dao.BaseDAO;
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
	
	public int insertVtconnectionInfo(DataCommonVO paramMap){
		return getSqlSession().insert("adminMapper.insertVtconnectionInfo", paramMap );
	}
	
	public int updateVtconnectionInfo(DataCommonVO paramMap){
		return getSqlSession().update("adminMapper.updateVtconnectionInfo", paramMap);
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
}
