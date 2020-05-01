package com.varsql.web.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.db.DBGroupEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface DBGroupEntityRepository extends DefaultJpaRepository, JpaRepository<DBGroupEntity, String> , JpaSpecificationExecutor<DBGroupEntity>  {

}