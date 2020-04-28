package com.varsql.web.repository.spec;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.entity.app.ExceptionLogEntity;
import com.varsql.web.model.entity.db.DBConnectionEntity;
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
        	switch (param.getCategory()) {
	            case "0":
	            	return criteriaBuilder.like(root.get(ExceptionLogEntity.EXCP_TYPE),contains(keyword));
	            case "1":
	            	return criteriaBuilder.like(root.get(ExceptionLogEntity.EXCP_CONT),contains(keyword));
	            case "2":
	            	return criteriaBuilder.like(root.get(ExceptionLogEntity.SERVER_ID),contains(keyword));
	            default :
	            	return criteriaBuilder.like(root.get(ExceptionLogEntity.EXCP_TITLE),contains(keyword));
	        }
        };

    }

}