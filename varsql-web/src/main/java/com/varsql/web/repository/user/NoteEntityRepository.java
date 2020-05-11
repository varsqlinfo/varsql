package com.varsql.web.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.app.NoteEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface NoteEntityRepository extends DefaultJpaRepository, JpaRepository<NoteEntity, String>, JpaSpecificationExecutor<NoteEntity>  {
	
	public NoteEntity findByNoteId(String noteId);
}
