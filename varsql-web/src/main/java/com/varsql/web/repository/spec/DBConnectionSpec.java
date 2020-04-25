package com.varsql.web.repository.spec;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.entity.db.DBConnectionEntity;

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
public class DBConnectionSpec extends DefaultSpec{

	public static Specification<DBConnectionEntity> getDelYn() {
    	return (root, query, criteriaBuilder) -> {
    		Predicate predicate = criteriaBuilder.equal(root.get(DBConnectionEntity.DEL_YN), "N");
    		return predicate;
    	};
    }

    public static Specification<DBConnectionEntity> getVnameOrUrl(String keyword) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
	    		criteriaBuilder.like(root.get(DBConnectionEntity.VNAME),contains(keyword))
	    		,criteriaBuilder.like(root.get(DBConnectionEntity.VURL),contains(keyword))
	    	);
        };
    }


    public static Specification<DBConnectionEntity> vconnid(String vconnid) {
    	return (root, query, criteriaBuilder) -> {
    		Predicate predicate = criteriaBuilder.equal(root.get(DBConnectionEntity.VCONNID), vconnid);
    		return predicate;
    	};
    }

    public static Specification<DBConnectionEntity> getVnameOrVurl(String name) {
        return Specification.where(getDelYn()).and(getVnameOrUrl(name));
    }

    public static Specification<DBConnectionEntity> detailInfo(String vconnid) {
    	return Specification.where(getDelYn()).and(vconnid(vconnid));
    }
}