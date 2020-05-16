package com.varsql.web.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.model.EntityFieldConstants;
import com.varsql.web.model.entity.db.DBGroupEntity;
import com.varsql.web.model.entity.sql.SqlFileEntity;

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
public class SqlFileSpec extends DefaultSpec{
    public static Specification<SqlFileEntity> searchSqlFile(String keyword) {
        return Specification.where(viewid()).and(likeTitleOrCont(keyword));
    }
    
    public static Specification<SqlFileEntity> findSqlFile(String sqlId) {
        return Specification.where(viewid()).and(sqlId(sqlId));
    }

	 // sql file queryTitle or queryCont search
    private static Specification<SqlFileEntity> likeTitleOrCont(String keyword) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
	    		criteriaBuilder.like(root.get(SqlFileEntity.GUERY_TITLE),contains(keyword))
	    		,criteriaBuilder.like(root.get(SqlFileEntity.QUERY_CONT),contains(keyword))
	    	);
        };
    }

    // viewid
    private static Specification<SqlFileEntity> viewid() {
    	return (root, query, cb) -> {
    		return cb.equal(root.get(SqlFileEntity.VIEWID), SecurityUtil.userViewId());
		};
    }
    
    // sql id 
    private static Specification<SqlFileEntity> sqlId(String sqlId) {
    	return (root, query, cb) -> {
    		return cb.equal(root.get(SqlFileEntity.SQL_ID), sqlId);
    	};
    }
}