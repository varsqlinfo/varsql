package com.varsql.app.admin.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.app.common.dao.BaseDAO;
import com.varsql.app.util.VarsqlUtil;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.VartechReflectionUtils;


@Repository
public class ErrorLogDAO extends BaseDAO{
	
	/**
	 * 
	 * @Method Name  : selectErrorList
	 * @Method 설명 : error error log list
	 * @작성자   : ytkim
	 * @작성일   : 2018. 1. 22. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public int selectErrorTotalCnt(SearchParameter searchParameter) {
		System.out.println("11111111111111111111111");
		System.out.println(VartechReflectionUtils.reflectionToString(searchParameter));
		System.out.println("11111111111111111111111");
		return getSqlSession().selectOne("adminMapper.selectErrorTotalCnt", searchParameter);
	}
	
	public List<Object> selectErrorList(SearchParameter searchParameter) {
		return getSqlSession().selectList("adminMapper.selectErrorList", searchParameter);
	}
	
	
}
