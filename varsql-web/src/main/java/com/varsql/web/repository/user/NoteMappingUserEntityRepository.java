package com.varsql.web.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.app.NoteMappingUserEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface NoteMappingUserEntityRepository extends DefaultJpaRepository, JpaRepository<NoteMappingUserEntity, String>, JpaSpecificationExecutor<NoteMappingUserEntity>  {

	NoteMappingUserEntity findByNoteId(String noteId);
	
	@Query("delete from NoteMappingUserEntity c where c.noteId in :noteIds and c.sendId = :userViewId and c.viewDt is null")
	void deleteSendMsgInfo(@Param("noteIds") String[] noteIds, @Param("userViewId") String userViewId);
	
	@Query("delete from NoteMappingUserEntity c where c.noteId in :noteIds and c.recvId = :userViewId")
	void deleteRecvMsgInfo(@Param("noteIds") String[] noteIds, @Param("userViewId") String userViewId);
	
}
