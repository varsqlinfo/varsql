package com.varsql.web.repository.db;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.db.DBTypeDriverEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface DBTypeDriverEntityRepository extends DefaultJpaRepository, JpaRepository<DBTypeDriverEntity, Long>{
	public List<DBTypeDriverEntity> findByDbtype(String dbType);

	public DBTypeDriverEntity findByDriverId(String driverId);

	public List<?> findByDbtype(String dbtype, Sort sort);
}