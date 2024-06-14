package com.varsql.web.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.entity.sql.SqlFileTabEntity;
import com.varsql.web.util.SecurityUtil;

/**
 * -----------------------------------------------------------------------------
* @fileName		: SqlFileSpec.java
* @desc		: sql file 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 5. 1. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class SqlFileTabSpec extends DefaultSpec{
    public static Specification<SqlFileTabEntity> findSqlTabInfo(String vconnid) {
        return Specification.where(vconnid(vconnid)).and(viewid()).and(viewyn());
    }

    // viewid
    private static Specification<SqlFileTabEntity> viewid() {
    	return (root, query, cb) -> {
    		return cb.equal(root.get(SqlFileTabEntity.VIEWID), SecurityUtil.userViewId());
		};
    }
    
    // vconnid
    private static Specification<SqlFileTabEntity> vconnid(String vconnid) {
    	return (root, query, cb) -> {
    		return cb.equal(root.get(SqlFileTabEntity.VCONNID), vconnid);
    	};
    }
    
    // viewyn
    private static Specification<SqlFileTabEntity> viewyn() {
    	return (root, query, cb) -> {
    		return cb.equal(root.get(SqlFileTabEntity.VIEW_YN),"Y");
    	};
    }
}