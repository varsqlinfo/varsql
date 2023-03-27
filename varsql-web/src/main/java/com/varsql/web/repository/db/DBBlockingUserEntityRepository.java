package com.varsql.web.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.db.DBBlockingUserEntity;
import com.varsql.web.model.id.DBVconnidNViewIdID;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface DBBlockingUserEntityRepository extends DefaultJpaRepository, JpaRepository<DBBlockingUserEntity, DBVconnidNViewIdID> {
	
	void deleteByVconnidAndViewid(String vconnid, String viewid);
}