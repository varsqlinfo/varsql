package com.varsql.web.repository.spec;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.entity.sql.SqlHistoryEntity;
import com.vartech.common.app.beans.SearchParameter;

public class SqlHistorySpec extends DefaultSpec {

	public static Specification<SqlHistoryEntity> logSqlSearch(String vconnid ,SearchParameter param) {
		
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			
			predicates.add(cb.equal(root.get(SqlHistoryEntity.VCONNID), vconnid));
			
			predicates.add(cb.like(root.get(SqlHistoryEntity.LOG_SQL), contains(param.getKeyword())));
			
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}	
}