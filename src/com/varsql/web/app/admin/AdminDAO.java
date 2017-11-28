package com.varsql.web.app.admin;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.dao.BaseDAO;


@Repository
public class AdminDAO extends BaseDAO{
	
	public List<Object> selectPageList(DataCommonVO paramMap) {
		return getSqlSession().selectList("adminMapper.selectPageList", paramMap);
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

	public int selectPageTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("adminMapper.selectPageTotalCnt", paramMap);
	}

	public List selectAllDbType() {
		return getSqlSession().selectList("adminMapper.selectAllDbType");
	}

	public List selectDbDriverList(DataCommonVO paramMap) {
		return getSqlSession().selectList("adminMapper.selectDbDriverList", paramMap);
	}
}
