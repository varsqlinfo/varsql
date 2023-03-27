package com.varsql.web.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.core.common.util.SecurityUtil;
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
	public static Specification<SqlFileEntity> findVconnSqlFileName(String vconnid , String keyword) {
		return Specification.where(viewid()).and(vconnid(vconnid)).and(likeTitle(keyword));
	}

	public static Specification<SqlFileEntity> findVconnSqlFileNameOrCont(String vconnid , String keyword) {
		return Specification.where(viewid()).and(vconnid(vconnid)).and(likeTitleOrCont(keyword));
	}

	public static Specification<SqlFileEntity> detailSqlFile(String vconnid, String sqlId) {
		return Specification.where(viewid()).and(vconnid(vconnid)).and(sqlId(sqlId));
	}

    public static Specification<SqlFileEntity> findSqlFileNameOrCont(String keyword) {
        return Specification.where(viewid()).and(likeTitleOrCont(keyword));
    }

    public static Specification<SqlFileEntity> findSqlFile(String sqlId) {
        return Specification.where(viewid()).and(sqlId(sqlId));
    }

    private static Specification<SqlFileEntity> likeTitle(String keyword) {
    	return (root, query, criteriaBuilder) -> {
    		query.orderBy(criteriaBuilder.asc(root.get(SqlFileEntity.SQL_TITLE)), criteriaBuilder.desc(root.get(SqlFileEntity.REG_DT)));
            return criteriaBuilder.or(
	    		criteriaBuilder.like(root.get(SqlFileEntity.SQL_TITLE),contains(keyword))
	    	);
        };
	}

	// sql file queryTitle or queryCont search
    private static Specification<SqlFileEntity> likeTitleOrCont(String keyword) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
	    		criteriaBuilder.like(root.get(SqlFileEntity.SQL_TITLE),contains(keyword))
	    		,criteriaBuilder.like(root.get(SqlFileEntity.SQL_CONT),contains(keyword))
	    	);
        };
    }

    // viewid
    private static Specification<SqlFileEntity> viewid() {
    	return (root, query, cb) -> {
    		return cb.equal(root.get(SqlFileEntity.VIEWID), SecurityUtil.userViewId());
		};
    }

    private static Specification<SqlFileEntity> vconnid(String vconnid) {
    	return (root, query, cb) -> {
    		return cb.equal(root.get(SqlFileEntity.VCONNID), vconnid);
    	};
    }

    // sql id
    private static Specification<SqlFileEntity> sqlId(String sqlId) {
    	return (root, query, cb) -> {
    		return cb.equal(root.get(SqlFileEntity.SQL_ID), sqlId);
    	};
    }
}