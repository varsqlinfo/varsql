package com.varsql.web.repository.db;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.db.DBTypeDriverFileEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface DBTypeDriverFileEntityRepository extends DefaultJpaRepository, JpaRepository<DBTypeDriverFileEntity, String> {

	@Transactional
	@Modifying
	@Query("delete from DBTypeDriverFileEntity a where a.fileId in :ids")
	void deleteByIdInQuery(@Param("ids") List<String> ids);
	
	
	List<DBTypeDriverFileEntity> findByFileContId(String contId);
	
	DBTypeDriverFileEntity findByFileId(String fileId);
}