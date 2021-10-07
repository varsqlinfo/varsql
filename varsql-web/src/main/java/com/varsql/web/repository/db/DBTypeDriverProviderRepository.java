package com.varsql.web.repository.db;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.db.DBTypeDriverProviderEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface DBTypeDriverProviderRepository extends DefaultJpaRepository, JpaRepository<DBTypeDriverProviderEntity, Long>, JpaSpecificationExecutor<DBTypeDriverProviderEntity>{
	List<DBTypeDriverProviderEntity> findByDbType(String dbtype);

	DBTypeDriverProviderEntity findByDriverProviderId(String driverProviderId);

	Page<DBTypeDriverProviderEntity> findByDbTypeOrDriverClass(String dbType, String driverClass, Pageable convertSearchInfoToPage);
}
