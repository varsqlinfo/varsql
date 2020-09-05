package com.varsql.web.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.db.DBGroupMappingUserEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface DBGroupMappingUserEntityRepository extends DefaultJpaRepository, JpaRepository<DBGroupMappingUserEntity, Long> , JpaSpecificationExecutor<DBGroupMappingUserEntity>  {
	
	void deleteByGroupIdAndViewid(String groupId, String viewid);
	
}