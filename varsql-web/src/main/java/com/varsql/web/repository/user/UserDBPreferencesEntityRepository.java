package com.varsql.web.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.varsql.web.model.entity.user.UserDBPreferencesEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface UserDBPreferencesEntityRepository extends DefaultJpaRepository, JpaRepository<UserDBPreferencesEntity, String>, JpaSpecificationExecutor<UserDBPreferencesEntity>  {

	
}
