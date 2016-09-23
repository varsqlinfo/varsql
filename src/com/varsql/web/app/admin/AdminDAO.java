package com.varsql.web.app.admin;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;


@Repository
public class AdminDAO extends BaseDAO{
	
	public List<Object> selectPageList(DataCommonVO paramMap) {
		return getSqlSession().selectList("selectPageList", paramMap);
	}

	public Object selectDetailObject(DataCommonVO paramMap) {
		return getSqlSession().selectOne("selectDetailObject", paramMap);
	}

	public String selectVtconnectionMaxVal() {
		return getSqlSession().selectOne("selectVtconnectionMaxVal");
	}
	
	public int insertVtconnectionInfo(DataCommonVO paramMap){
		return getSqlSession().insert("insertVtconnectionInfo", paramMap );
	}
	
	public int updateVtconnectionInfo(DataCommonVO paramMap){
		return getSqlSession().update("updateVtconnectionInfo", paramMap);
	}

	public int deleteVtconnectionInfo(DataCommonVO paramMap) {
		return getSqlSession().delete("deleteVtconnectionInfo", paramMap);
	}

	public int selectPageTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("selectPageTotalCnt", paramMap);
	}

	public List selectAllDbType() {
		return getSqlSession().selectList("selectAllDbType");
	}
}
