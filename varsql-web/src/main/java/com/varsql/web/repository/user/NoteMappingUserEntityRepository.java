package com.varsql.web.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.app.NoteMappingUserEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface NoteMappingUserEntityRepository extends DefaultJpaRepository, JpaRepository<NoteMappingUserEntity, String>, JpaSpecificationExecutor<NoteMappingUserEntity>  {

	NoteMappingUserEntity findByNoteId(String noteId);
	
}
