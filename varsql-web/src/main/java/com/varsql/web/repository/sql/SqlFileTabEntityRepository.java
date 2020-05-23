package com.varsql.web.repository.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.sql.SqlFileTabEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface SqlFileTabEntityRepository extends DefaultJpaRepository, JpaRepository<SqlFileTabEntity, String>, JpaSpecificationExecutor<SqlFileTabEntity>  {
	
	@Query(value = "update SqlFileTabEntity as ste set ste.viewYn= 'N' where ste.vconnid = :vconnid and ste.viewid = :viewid and ste.viewYn= 'Y'")
	void updateSqlFileTabDisable(@Param("vconnid") String vconnid,@Param("viewid") String viewid);

	@Query(value = "update SqlFileTabEntity as ste set ste.viewYn= 'Y' where ste.vconnid = :vconnid and ste.viewid = :viewid and ste.sqlId = :sqlId")
	void updateSqlFileTabEnable(@Param("vconnid") String vconnid, @Param("viewid") String viewid, @Param("sqlId") String sqlId);
	
	@Query(value = "delete SqlFileTabEntity ste where ste.vconnid = :vconnid and ste.viewid = :viewid and ste.sqlId = :sqlId")
	void deleteTabInfo(@Param("vconnid") String vconnid, @Param("viewid") String viewid, @Param("sqlId") String sqlId);

	@Query(value = "delete SqlFileTabEntity ste where ste.vconnid = :vconnid and ste.viewid = :viewid")
	void deleteAllSqlFileTabInfo(@Param("vconnid") String vconnid, @Param("viewid") String viewid);
}