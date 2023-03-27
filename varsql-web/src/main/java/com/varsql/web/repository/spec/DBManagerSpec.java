package com.varsql.web.repository.spec;
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

	public static Specification<DBManagerEntity> findVconnidManagerCheck(String vconnid, String viewid) {
    	return Specification.where(vconnid(vconnid)).and(viewid(viewid));
    }
	
	public static Specification<DBManagerEntity> findAllVconnidManager(String vconnid) {
    	return Specification.where(joinViewid()).and(vconnid(vconnid));
    }
	
	public static Specification<DBManagerEntity> findViewid(String viewid) {
		return Specification.where(viewid(viewid));
	}
	
	private static Specification<DBManagerEntity> viewid(String viewid) {
		return (root, query, cb) -> {
    		return cb.equal(root.get(DBManagerEntity.VIEWID), viewid);
    	};
	}
	
	private static Specification<DBManagerEntity> vconnid(String vconnid) {
		return (root, query, cb) -> {
    		return cb.equal(root.get(DBManagerEntity.VCONNID), vconnid);
    	};
	}
	
	private static Specification<DBManagerEntity> joinViewid() {
    	return (root, query, cb) -> {
    		root.fetch(DBManagerEntity.JOIN_USERINFO);
    		return cb.equal(cb.literal(1), 1);
    	};
    }
	
	

}