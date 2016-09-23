package com.varsql.web.app.user;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;


@Repository
public class UserMainDAO extends BaseDAO{
	
	public int selectdbListTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("selectdbListTotalCnt", paramMap);
	}
	
	public List<Object> selectdbList(DataCommonVO paramMap) {
		return getSqlSession().selectList("selectdbList", paramMap);
	}

	public int updateQnaAnswerContent(DataCommonVO paramMap){
		return getSqlSession().update("updateQnaAnswerContent", paramMap);
	}
	
	public int selectDbUserMappingListTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("selectDbUserMappingListTotalCnt", paramMap);
	}
	
	public List<Object>  selectDbUserMappingList(DataCommonVO paramMap){
		return getSqlSession().selectList("selectDbUserMappingList", paramMap);
	}
}
