package com.varsql.web.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface DBConnectionEntityRepository extends DefaultJpaRepository, JpaRepository<DBConnectionEntity, String>, JpaSpecificationExecutor<DBConnectionEntity>  {
	public DBConnectionEntity findByVconnid(String vconnid);
}