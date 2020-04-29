package com.varsql.web.repository.spec;
import java.util.Arrays;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.entity.app.ExceptionLogEntity;
import com.vartech.common.app.beans.SearchParameter;

/**
 * -----------------------------------------------------------------------------
* @fileName		: DBConnectionSpec.java
* @desc		: db connection 쿼리 specification
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class ExceptionLogSpec extends DefaultSpec{


    public static Specification<ExceptionLogEntity> searchField(SearchParameter param) {
    	String keyword = param.getKeyword();

        return (root, query, criteriaBuilder) -> {
        	switch (SCH_CATG.findCode(param.getCategory())) {
	            case TYPE:
	            	return criteriaBuilder.like(root.get(ExceptionLogEntity.EXCP_TYPE),contains(keyword));
	            case CONT:
	            	return criteriaBuilder.like(root.get(ExceptionLogEntity.EXCP_CONT),contains(keyword));
	            case SERVER:
	            	return criteriaBuilder.like(root.get(ExceptionLogEntity.SERVER_ID),contains(keyword));
	            default :
	            	return criteriaBuilder.like(root.get(ExceptionLogEntity.EXCP_TITLE),contains(keyword));
	        }
        };
    }
    
    // 검색 카테고리.
 	public static enum SCH_CATG{
 		TITLE, TYPE, CONT, SERVER; 
 		
 		public static SCH_CATG findCode(String catg) {
 			return Arrays.stream(values())
 		            .filter(item -> item.name().equals(catg.toUpperCase()))
 		            .findAny()
 		            .orElse(TITLE);
 		}
 	}
}