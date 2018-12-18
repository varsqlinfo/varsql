package com.varsql.app.common.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.varsql.app.common.beans.VtconnectionRVO;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: CommonDAO.java
* @DESC		: 공통 dao 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2018. 12. 18. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Repository
public class CommonDAO extends BaseDAO{
	
	public VtconnectionRVO selectDetailObject(Map paramMap) {
		return getSqlSession().selectOne("commonMapper.selectDetailObject", paramMap);
	}

}
