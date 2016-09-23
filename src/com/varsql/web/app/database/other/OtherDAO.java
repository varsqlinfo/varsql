package com.varsql.web.app.database.other;

import org.springframework.stereotype.Repository;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;

@Repository
public class OtherDAO extends BaseDAO{
	
	public int selectQnaTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("selectQnaTotalCnt", paramMap);
	}
	
	public Object selectQna(DataCommonVO paramMap) {
		return getSqlSession().selectList("selectQna", paramMap);
	}
	
	public Object selectDetailQna(DataCommonVO paramMap) {
		return getSqlSession().selectOne("selectDetailQna", paramMap);
	}

	public String selectQnaMaxVal() {
		return getSqlSession().selectOne("selectQnaMaxVal");
	}
	
	public int insertQnaInfo(DataCommonVO paramMap){
		return getSqlSession().insert("insertQnaInfo", paramMap );
	}
	
	public int deleteQnaInfo(DataCommonVO paramMap){
		return getSqlSession().delete("deleteQnaInfo", paramMap );
	}
	
	public int updateQnaInfo(DataCommonVO paramMap){
		return getSqlSession().update("updateQnaInfo", paramMap );
	}
}
