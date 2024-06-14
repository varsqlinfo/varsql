package com.varsql.web.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.entity.sql.SqlHistoryEntity;
import com.varsql.web.util.SecurityUtil;
import com.vartech.common.app.beans.SearchParameter;

public class SqlHistorySpec extends DefaultSpec {
	
	public static Specification<SqlHistoryEntity> userHisotryearch(String vconnid, SearchParameter param) {
        return Specification.where(vconnid(vconnid)).and(viewid()).and(logSqlSearch(param));
    }
	public static Specification<SqlHistoryEntity> logSqlSearch(String vconnid, SearchParameter param) {
		return Specification.where(vconnid(vconnid)).and(logSqlSearch(param));
	}
	
	 // vconnid
    private static Specification<SqlHistoryEntity> vconnid(String vconnid) {
    	return (root, query, cb) -> {
    		return cb.equal(root.get(SqlHistoryEntity.VCONNID), vconnid);
    	};
    }

    // viewid
    private static Specification<SqlHistoryEntity> viewid() {
    	return (root, query, cb) -> {
    		return cb.equal(root.get(SqlHistoryEntity.VIEWID), SecurityUtil.userViewId());
		};
    }
    
    private static Specification<SqlHistoryEntity> logSqlSearch(SearchParameter param) {
    	return (root, query, cb) -> {
    		return cb.like(root.get(SqlHistoryEntity.LOG_SQL), contains(param.getKeyword()));
    	};
    }
}