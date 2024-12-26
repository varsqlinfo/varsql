package com.varsql.web.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.db.DBTypeEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface DBTypeEntityRepository extends DefaultJpaRepository, JpaRepository<DBTypeEntity, String> {
	
}