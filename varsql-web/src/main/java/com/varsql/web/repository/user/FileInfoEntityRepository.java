package com.varsql.web.repository.user;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface FileInfoEntityRepository extends DefaultJpaRepository, JpaRepository<FileInfoEntity, String> {
	@Transactional
	@Modifying
	@Query("delete from FileInfoEntity a where a.fileId in :ids")
	void deleteByIdInQuery(@Param("ids") List<String> ids);


	@Transactional
	@Modifying
	void deleteByFileContId(String id);

	FileInfoEntity findByFileId(String fileId);

	List<FileInfoEntity> findByFileContId(String contId);
}