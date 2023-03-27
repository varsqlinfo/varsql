package com.varsql.web.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.user.EmailTokenEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface EmailTokenEntityRepository extends DefaultJpaRepository, JpaRepository<EmailTokenEntity, Long>, JpaSpecificationExecutor<EmailTokenEntity>  {

	EmailTokenEntity findByToken(String token);
	
	@Modifying
	@Query(value = "delete from EmailTokenEntity ste where ste.token = :token and ste.tokenType = :tokenType")
	void deleteTokenInfo(@Param("token")String token, @Param("tokenType")String tokenType);
}
