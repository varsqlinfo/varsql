package com.varsql.web.repository.spec;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.model.EntityValueConstants;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.DBManagerEntity;

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

    // db name or  jdbc url search
    public static Specification<DBConnectionEntity> getVnameOrVurl(String name) {
        return Specification.where(getDelYn()).and(getVnameOrUrl(name));
    }
    
    // db 상세보기
    public static Specification<DBConnectionEntity> detailInfo(String vconnid) {
    	return Specification.where(getDelYn()).and(vconnid(vconnid));
    }
    
    private static Specification<DBConnectionEntity> getDelYn() {
    	return (root, query, criteriaBuilder) -> {
    		Predicate predicate = criteriaBuilder.notEqual(root.get(DBConnectionEntity.DEL_YN), true);
    		return predicate;
    	};
    }
	
	private static Specification<DBConnectionEntity> getUseYn() {
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.equal(root.get(DBConnectionEntity.USE_YN), EntityValueConstants.YN.Y.name());
			return predicate;
		};
	}

    private static Specification<DBConnectionEntity> getVnameOrUrl(String keyword) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
	    		criteriaBuilder.like(root.get(DBConnectionEntity.VNAME),contains(keyword))
	    		,criteriaBuilder.like(root.get(DBConnectionEntity.VURL),contains(keyword))
	    	);
        };
    }

    private static Specification<DBConnectionEntity> vconnid(String vconnid) {
    	return (root, query, criteriaBuilder) -> {
    		Predicate predicate = criteriaBuilder.equal(root.get(DBConnectionEntity.VCONNID), vconnid);
    		return predicate;
    	};
    }
    
    // 권한 있는 db connection 정보 보기.
    public static Specification<DBConnectionEntity> mgmtDbList(String viewid, String keyword) {
    	return Specification.where(managetAuthDbList(viewid)).and(getDelYn()).and(getUseYn()).and(getVnameOrUrl(keyword));
    }
    
    // manager 권한을 가진 db
    private static Specification<DBConnectionEntity> managetAuthDbList(String viewid) {
    	return (root, query, cb) -> {
    		if(SecurityUtil.isAdmin()) {
    			return cb.equal(cb.literal(1), 1);
    		}else {
	    		Join<DBConnectionEntity, DBManagerEntity> join = root.join(DBConnectionEntity.JOIN_MANAGER_LIST);
	    		return cb.equal(join.get(DBManagerEntity.VIEWID), viewid);
    		}
    	};
    }
    
    
}