package com.varsql.web.app.manager.dbnuser;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.dao.BaseDAO;
import com.vartech.common.app.beans.SearchParameter;


@Repository
public class DbnUserDAO extends BaseDAO{
	
	public int selectdbListTotalCnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("manageMapper.selectdbListTotalCnt", searchParameter);
	}
	
	public List<Object> selectdbList(SearchParameter searchParameter) {
		return getSqlSession().selectList("manageMapper.selectdbList", searchParameter);
	}

	public int updateQnaAnswerContent(DataCommonVO paramMap){
		return getSqlSession().update("manageMapper.updateQnaAnswerContent", paramMap);
	}
	
	public int selectDbUserMappingListTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("selectDbUserMappingListTotalCnt", paramMap);
	}
	
	public List<Object>  selectDbUserMappingList(DataCommonVO paramMap){
		return getSqlSession().selectList("manageMapper.selectDbUserMappingList", paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : updateDbUser
	 * @Method 설명 : db 사용자 등록.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public int updateDbUser( DataCommonVO paramMap){
		return getSqlSession().update("manageMapper.updateDbUser", paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : deleteDbUser
	 * @Method 설명 : db 사용자 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 23. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public int deleteDbUser( DataCommonVO paramMap){
		return getSqlSession().delete("manageMapper.deleteDbUser",paramMap);
	}
}
