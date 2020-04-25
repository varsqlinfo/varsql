package com.varsql.web.app.manager.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.common.dao.BaseDAO;
import com.vartech.common.app.beans.SearchParameter;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ManagerCommonDAO.java
* @DESC		: 매니저 공통 처리. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 8. 16. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Repository
public class ManagerCommonDAO extends BaseDAO{
	
	public int selectdbListTotalCnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("manageMapper.selectdbListTotalCnt", searchParameter);
	}
	
	public List<Object> selectdbList(SearchParameter searchParameter) {
		return getSqlSession().selectList("manageMapper.selectdbList", searchParameter);
	}
	
	/**
	 * 
	 * @Method Name  : selectUserListTotalCnt
	 * @Method 설명 : 사용자 목록 카운트
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public int selectUserListTotalCnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("commonMapper.selectUserListTotalCnt", searchParameter);
	}
	
	/**
	 * 
	 * @Method Name  : selectUserList
	 * @Method 설명 : 사용자 목록. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public List selectUserList(SearchParameter searchParameter) {
		return getSqlSession().selectList("commonMapper.selectUserList", searchParameter);
	}

}
