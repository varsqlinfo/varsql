package com.varsql.web.repository.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.db.DBConnTabEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface DBConnTabEntityRepository extends DefaultJpaRepository, JpaRepository<DBConnTabEntity, Long> {

	// tab disable
	@Modifying
	@Query(value = "update DBConnTabEntity as ste set ste.viewYn= 'N' where ste.viewid = :viewid and ste.viewYn= 'Y'")
	void updateConnTabDisable(@Param("viewid") String viewid);

	// tab enable
	@Modifying
	@Query(value = "update DBConnTabEntity as ste set ste.viewYn= 'Y' where ste.vconnid = :vconnid and ste.viewid = :viewid")
	void updateConnTabEnable(@Param("vconnid") String vconnid, @Param("viewid") String viewid);

	// remove tab
	@Modifying
	@Query(value = "delete from DBConnTabEntity ste where ste.vconnid = :vconnid and ste.viewid = :viewid")
	void deleteConnTabInfo(@Param("vconnid") String vconnid, @Param("viewid") String viewid);

	// get item
	DBConnTabEntity findByViewidAndVconnid(String viewid, String vconnid);

	// update next tab item prevVonnid info
	@Modifying
	@Query(value = "update DBConnTabEntity as ste set ste.prevVconnid= :prevVconnid where ste.prevVconnid = :vconnid and ste.viewid = :viewid")
	void updateNextTabPrevVconnid(@Param("vconnid")String vconnid, @Param("viewid") String viewid, @Param("prevVconnid") String prevVconnid);

	List<DBConnTabEntity> findAllByViewid(String viewid);

	@Modifying
	@Query(value = "delete from DBConnTabEntity ste where ste.viewid = :viewid and ste.vconnid in :vconnids")
	void deleteAllTabInfo(@Param("viewid") String viewid, @Param("vconnids")List<String> vconnids);

}

