package com.varsql.app.plugin.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.app.common.dao.BaseDAO;
import com.vartech.common.app.beans.ParamMap;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: PluginDAO.java
* @DESC		: 플러그인 dao 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Repository
public class PluginDAO extends BaseDAO{
	
	/**
	 * 
	 * @Method Name  : selectGlossarySearch
	 * @Method 설명 : 용어 검색.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 24. 
	 * @변경이력  :
	 * @param param
	 * @return
	 */
	public List selectGlossarySearch(ParamMap param) {
		return  getSqlSession().selectList("glossaryMapper.selectGlossarySearchList", param);
	}
	
	
}
