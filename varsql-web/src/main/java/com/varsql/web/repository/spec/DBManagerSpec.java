package com.varsql.web.repository.spec;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.entity.db.DBManagerEntity;

/**
 * -----------------------------------------------------------------------------
* @fileName		: DBManagerSpec.java
* @desc		: db manager model specification
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class DBManagerSpec extends DefaultSpec{

	public static Specification<DBManagerEntity> vconnid(String vconnid) {
    	return (root, query, criteriaBuilder) -> {
    		Predicate predicate = criteriaBuilder.equal(root.get(DBManagerEntity.VCONNID), vconnid);
    		return predicate;
    	};
    }

	public static Specification<DBManagerEntity> viewid(String viewid) {
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.equal(root.get(DBManagerEntity.VIEWID), viewid);
			return predicate;
		};
	}

}