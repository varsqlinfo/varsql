package com.varsql.web.app.manager.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.web.app.manager.beans.GlossaryInfo;
import com.varsql.web.common.dao.BaseDAO;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.SearchParameter;


/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: GlossaryDAO.java
* @DESC		: 용어집  
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Repository
public class GlossaryDAO extends BaseDAO{
	
	/**
	 * 
	 * @Method Name  : selectGlossaryTotalcnt
	 * @Method 설명 : 용어 총 카운트
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public int selectGlossaryTotalcnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("glossaryMapper.selectGlossaryTotalcnt", searchParameter);
	}
	
	/**
	 * 
	 * @Method Name  : selectGlossaryList
	 * @Method 설명 : 용어 목록. 
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public List<Object> selectGlossaryList(SearchParameter searchParameter) {
		return getSqlSession().selectList("glossaryMapper.selectGlossaryList", searchParameter);
	}

	/**
	 * 
	 * @Method Name  : insertGlossaryInfo
	 * @Method 설명 : 등록
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param glossaryInfo
	 * @return
	 */
	public Object insertGlossaryInfo(GlossaryInfo glossaryInfo) {
		return getSqlSession().insert("glossaryMapper.insertGlossaryInfo",glossaryInfo);
	}
	
	/**
	 * 
	 * @Method Name  : updateGlossaryInfo
	 * @Method 설명 : 수정
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param glossaryInfo
	 * @return
	 */
	public Object updateGlossaryInfo(GlossaryInfo glossaryInfo) {
		return getSqlSession().update("glossaryMapper.updateGlossaryInfo",glossaryInfo);
	}
	
	/**
	 * 
	 * @Method Name  : deleteGlossaryInfo
	 * @Method 설명 : 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 19. 
	 * @변경이력  :
	 * @param parameter
	 * @return
	 */
	public Object deleteGlossaryInfo(ParamMap parameter) {
		return getSqlSession().delete("glossaryMapper.deleteGlossaryInfo", parameter);
	}
}
