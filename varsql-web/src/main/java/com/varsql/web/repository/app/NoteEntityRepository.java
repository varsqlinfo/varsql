package com.varsql.web.repository.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.app.NoteEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface NoteEntityRepository extends DefaultJpaRepository, JpaRepository<NoteEntity, String>, JpaSpecificationExecutor<NoteEntity>  {
	
	public NoteEntity findByNoteId(String noteId);
	
	@Modifying
	@Query("update NoteEntity set DEL_YN = 'Y' where noteId in :noteIds")
	void saveAllMsgDelYn(@Param("noteIds") String[] noteIds);
	
	
}
