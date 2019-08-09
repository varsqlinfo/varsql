package com.varsql.app.manager.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.app.common.dao.BaseDAO;
import com.varsql.app.manager.beans.DbGroupInfo;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.SearchParameter;


/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DbGroupDAO.java
* @DESC		: 용어집 dao 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Repository
public class DbGroupDAO extends BaseDAO{
	
	/**
	 * 
	 * @Method Name  : selectDbGroupTotalcnt
	 * @Method 설명 : 그룹 총 카운트
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public int selectDbGroupTotalcnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("manageMapper.selectDbGroupTotalcnt", searchParameter);
	}
	
	/**
	 * 
	 * @Method Name  : selectDbGroupList
	 * @Method 설명 : 목록. 
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public List<Object> selectDbGroupList(SearchParameter searchParameter) {
		return getSqlSession().selectList("manageMapper.selectDbGroupList", searchParameter);
	}

	/**
	 * 
	 * @Method Name  : insertDbGroupInfo
	 * @Method 설명 : 등록
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param dbGroupInfo
	 * @return
	 */
	public Object insertDbGroupInfo(DbGroupInfo dbGroupInfo) {
		return getSqlSession().insert("manageMapper.insertDbGroupInfo",dbGroupInfo);
	}
	
	/**
	 * 
	 * @Method Name  : updateDbGroupInfo
	 * @Method 설명 : 수정
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param dbGroupInfo
	 * @return
	 */
	public Object updateDbGroupInfo(DbGroupInfo dbGroupInfo) {
		return getSqlSession().update("manageMapper.updateDbGroupInfo",dbGroupInfo);
	}
	
	/**
	 * 
	 * @Method Name  : deleteDbGroupInfo
	 * @Method 설명 : 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param parameter
	 * @return
	 */
	public Object deleteDbGroupInfo(ParamMap parameter) {
		return getSqlSession().delete("manageMapper.deleteDbGroupInfo", parameter);
	}
}
