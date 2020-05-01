package com.varsql.web.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.db.DBManagerEntity;
import com.varsql.web.model.id.DBVconnidNViewIdID;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface DBManagerEntityRepository extends DefaultJpaRepository, JpaRepository<DBManagerEntity, DBVconnidNViewIdID>, JpaSpecificationExecutor<DBManagerEntity>  {

	public DBManagerEntity findByVconnid(String vconnid);

	public void deleteByViewid(String id);
}