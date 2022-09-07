package com.varsql.web.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface UserAuthRepository extends DefaultJpaRepository, JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {
	UserEntity findByUid(String uname);
}