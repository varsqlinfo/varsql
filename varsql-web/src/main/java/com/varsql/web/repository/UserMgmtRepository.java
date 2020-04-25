package com.varsql.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.user.UserEntity;

@Repository
public interface UserMgmtRepository extends DefaultJpaRepository, JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity>  {
	UserEntity findByViewid(String viewId);
}
