package com.varsql.web.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.db.DBConnectionViewEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface DBConnectionViewEntityRepository extends DefaultJpaRepository, JpaRepository<DBConnectionViewEntity, String>, JpaSpecificationExecutor<DBConnectionViewEntity>{
	public DBConnectionViewEntity findByVconnid(String vconnid);
}
