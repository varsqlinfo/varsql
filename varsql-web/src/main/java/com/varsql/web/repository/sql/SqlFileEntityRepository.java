package com.varsql.web.repository.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.sql.SqlFileEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface SqlFileEntityRepository extends DefaultJpaRepository, JpaRepository<SqlFileEntity, String>, JpaSpecificationExecutor<SqlFileEntity>  {

	@Query("delete from SqlFileEntity c where c.viewid = :viewid and c.sqlId in :sqlIds")
	void deleteSqlFiles(@Param("viewid") String viewid ,@Param("sqlIds") String [] sqlIds);

	void findSqlFile();

	@Query("delete from SqlFileEntity c where c.vconnid = :vconnid and c.sqlId = :sqlId")
	void deleteSqlFileInfo(@Param("vconnid") String vconnid,@Param("sqlId") String sqlId);

}