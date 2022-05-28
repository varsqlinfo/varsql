package com.varsql.web.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.entity.db.DBConnTabEntity;

/**
 * -----------------------------------------------------------------------------
* @fileName		: DBConnTabSpec.java
* @desc		: db connection tab specification
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class DBConnTabSpec extends DefaultSpec{

	/**
	 * tab 정보보기
	 * @param viewid
	 * @return
	 */
    public static Specification<DBConnTabEntity> findTabs(String viewid) {
    	return (root, query, cb) -> {
        	query.orderBy(cb.asc(root.get(DBConnTabEntity.PREV_VCONNID)));

            return cb.or(
            	cb.equal(root.get(DBConnTabEntity.VIEWID), viewid)
	    	);
        };
    }
}