package com.varsql.web.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.web.model.entity.db.DBTypeDriverProviderEntity;
import com.vartech.common.app.beans.SearchParameter;

public class DBTypeDriverProviderSpec extends DefaultSpec {
	public static Specification<DBTypeDriverProviderEntity> searchField(SearchParameter param) {
		String keyword = param.getKeyword();
		
		return (root, query, cb) -> 
			cb.or(cb.like(root.get(DBTypeDriverProviderEntity.PROVIDER_NAME), contains(keyword)),
			cb.like(root.get(DBTypeDriverProviderEntity.DRIVER_CLASS), contains(keyword)));
	}
}
