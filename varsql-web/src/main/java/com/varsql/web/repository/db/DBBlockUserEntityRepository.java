package com.varsql.web.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.db.DBBlockUserEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface DBBlockUserEntityRepository extends DefaultJpaRepository, JpaRepository<DBBlockUserEntity, Long> {

}