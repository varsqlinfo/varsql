package com.varsql.web.security.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.user.RememberMeEntity;

@Repository
public interface RememberMeEntityRepository extends JpaRepository<RememberMeEntity, String> {

	RememberMeEntity findBySeries(String seriesId);

	void deleteByUsername(String username);
	
	@Modifying
	@Query(value = "update RememberMeEntity as ste set ste.tokenValue = :tokenValue, ste.lastUsed = :lastUsed where ste.series= :series")
	void updateToken(@Param("series")String series, @Param("tokenValue") String tokenValue, @Param("lastUsed") Date lastUsed);
}