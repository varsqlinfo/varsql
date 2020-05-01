package com.varsql.web.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.model.EntityFieldConstants;
import com.varsql.web.model.entity.db.DBGroupEntity;

/**
 * -----------------------------------------------------------------------------
* @fileName		: DBGroupSpec.java
* @desc		: db group 관리 spec 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 5. 1. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class DBGroupSpec extends DefaultSpec{
    public static Specification<DBGroupEntity> searchKeyWord(String name) {
        return Specification.where(adminChk()).and(likeNmOrDesc(name));
    }

	 // db groupName or  groupDesc search
    private static Specification<DBGroupEntity> likeNmOrDesc(String keyword) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
	    		criteriaBuilder.like(root.get(DBGroupEntity.GROUP_NAME),contains(keyword))
	    		,criteriaBuilder.like(root.get(DBGroupEntity.GROUP_DESC),contains(keyword))
	    	);
        };
    }

    // manager 권한을 가진 db
    private static Specification<DBGroupEntity> adminChk() {
    	return (root, query, cb) -> {
    		if(SecurityUtil.isAdmin()) {
    			return cb.equal(cb.literal(1), 1);
    		}else {
	    		return cb.equal(root.get(EntityFieldConstants.REG_ID), SecurityUtil.userViewId());
    		}
    	};
    }
}