package com.varsql.web.repository.app;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface FileInfoEntityRepository extends DefaultJpaRepository, JpaRepository<FileInfoEntity, String>, JpaSpecificationExecutor<FileInfoEntity> {
	@Transactional
	@Modifying
	@Query("delete from FileInfoEntity a where a.fileId in :ids")
	void deleteByIdInQuery(@Param("ids") List<String> ids);


	@Transactional
	@Modifying
	void deleteByFileContId(String id);

	FileInfoEntity findByFileId(String fileId);

	List<FileInfoEntity> findByFileContId(String contId);

	@Modifying
	@Query("delete from FileInfoEntity c where c.regId = :viewid and c.fileId in :fileIdArr")
	void deleteFiles(@Param("viewid")String viewid, @Param("fileIdArr") String[] fileIdArr);
}